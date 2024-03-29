package uk.hotten.herobrine.game;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import org.bukkit.entity.*;
import uk.hotten.herobrine.game.runnables.ShardHandler;
import uk.hotten.herobrine.kit.KitGui;
import uk.hotten.herobrine.stat.GameRank;
import uk.hotten.herobrine.stat.StatManager;
import uk.hotten.herobrine.utils.*;
import uk.hotten.herobrine.world.WorldManager;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GMListener implements Listener {

    private GameManager gm;
    private ArrayList<Player> kitCooldown = new ArrayList<>();
    private ArrayList<UUID> silentLeave = new ArrayList<>();

    public GMListener(GameManager gm) { this.gm = gm; }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");

        if (gm.getSurvivors().size() >= gm.getMaxPlayers()) {
            if (gm.isAllowOverfill()) {
                if (!player.hasPermission("theherobrine.overfill")) {
                    silentLeave.add(player.getUniqueId());
                    player.kickPlayer(Message.format(ChatColor.RED + "This lobby is full."));
                    return;
                }
            } else {
                silentLeave.add(player.getUniqueId());
                player.kickPlayer(Message.format(ChatColor.RED + "This lobby is full."));
                return;
            }
        }

        StatManager.get().check(player.getUniqueId());

        gm.getScoreboards().put(player, ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler() {
            @Override
            public String getTitle(Player player) {
                return "" + ChatColor.YELLOW + ChatColor.BOLD + "Your Stats";
            }

            @Override
            public List<Entry> getEntries(Player player) {
                return new EntryBuilder()
                        .next(ChatColor.AQUA + "Points: " + ChatColor.RESET + StatManager.get().getPoints().get(player.getUniqueId()))
                        .next(ChatColor.AQUA + "Captures: " + ChatColor.RESET + StatManager.get().getCaptures().get(player.getUniqueId()))
                        .next(ChatColor.AQUA + "Kills: " + ChatColor.RESET + StatManager.get().getKills().get(player.getUniqueId()))
                        .next(ChatColor.AQUA + "Deaths: " + ChatColor.RESET + StatManager.get().getDeaths().get(player.getUniqueId()))
                        .build();
            }
        }).setUpdateInterval(1));
        gm.getScoreboards().get(player).activate();


        gm.updateTags(GameManager.ScoreboardUpdateAction.CREATE);
        gm.setTags(player, null, null, GameManager.ScoreboardUpdateAction.CREATE);

        if (gm.getGameState() == GameState.LIVE) {
            gm.makeSpectator(player);
            return;
        }

        Message.broadcast(Message.format("" + ChatColor.AQUA + player.getName() + " " + ChatColor.YELLOW + "has joined!"));
        gm.getSurvivors().add(player);
        gm.startCheck();

        WorldManager.getInstance().getPlayerVotes().put(player, 0);
        WorldManager.getInstance().sendVotingMessage(player);
        gm.hubInventory(player);
        gm.setKit(event.getPlayer(), gm.getSavedKit(player), true);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage("");
        if (!silentLeave.contains(player.getUniqueId())) {
            Message.broadcast(Message.format("" + ChatColor.AQUA + player.getName() + " " + ChatColor.YELLOW + "has quit."));
        }
        silentLeave.remove(player.getUniqueId());
        gm.getSurvivors().remove(player);

        if (gm.getScoreboards().containsKey(player))
            gm.getScoreboards().get(player).deactivate();
        gm.getScoreboards().remove(player);
        gm.getTeamPrefixes().remove(player);
        gm.getTeamColours().remove(player);

        WorldManager wm = WorldManager.getInstance();
        if (wm.getPlayerVotes().getOrDefault(player, 0) != 0)
            wm.getVotingMaps().get(wm.getPlayerVotes().get(player)).decrementVotes();
        wm.getPlayerVotes().remove(player);


        if (gm.getGameState() == GameState.LIVE) {
            if (player == gm.getShardCarrier()) {
                ShardHandler.drop(player.getLocation());
            }

            // If ran straight away, it still thinks THB is online if they were the quitter
            Bukkit.getServer().getScheduler().runTaskLater(gm.getPlugin(), gm::endCheck, 1);
            return;
        }

        if (gm.getPassUser() == player) {
            gm.setPassUser(null);
            Message.broadcast(Message.format(ChatColor.GOLD + player.getName() + " has left and will no-longer be Herobrine."), "theherobrine.command.setherobrine");
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();

        if (gm.getGameState() != GameState.LIVE)
            return;

        event.getItem().getItemStack();
        if (event.getItem().getItemStack().getType() != Material.NETHER_STAR) {
            event.setCancelled(true);
            return;
        }

        if (!gm.getSurvivors().contains(player)) {
            event.setCancelled(true);
            return;
        }

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p == gm.getHerobrine()) continue;
            PlayerUtil.sendTitle(p, "" + ChatColor.GREEN + ChatColor.BOLD + player.getName() + ChatColor.DARK_AQUA + " has picked up the shard!", ChatColor.YELLOW + "Help them return it!", 5, 60, 5);
        }
        PlayerUtil.sendTitle(gm.getHerobrine(), "" + ChatColor.GREEN + ChatColor.BOLD + player.getName() + ChatColor.DARK_AQUA + " has picked up the shard!", ChatColor.YELLOW + "Maybe target them first", 5, 60, 5);
        ShardHandler.shardTitle.remove();
        gm.setShardState(ShardState.CARRYING);
        gm.setShardCarrier(player);
        gm.setTags(player, "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Shard: ", ChatColor.LIGHT_PURPLE, GameManager.ScoreboardUpdateAction.UPDATE);
        PlayerUtil.broadcastSound(Sound.ENTITY_BAT_DEATH, 1f, 0f);
        PlayerUtil.addEffect(player, PotionEffectType.BLINDNESS, 100, 1, false, false);
        PlayerUtil.addEffect(player, PotionEffectType.SLOW, 600, 2, false, false);
        PlayerUtil.addEffect(player, PotionEffectType.CONFUSION, 300, 1, false, false);
        player.sendMessage(Message.format(ChatColor.GOLD + "You have a shard! Take it to the alter (Enchanting Table)!"));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (gm.getGameState() == GameState.LIVE) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock() == null)
                    return;
                Material m = event.getClickedBlock().getType();

                if (m == Material.ENCHANTING_TABLE)
                    event.setCancelled(true);

                if (m == Material.ENCHANTING_TABLE && player == gm.getShardCarrier()) {
                    event.setCancelled(true);
                    player.getInventory().getItemInMainHand();
                    if (player.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                        gm.capture(player);
                    }
                } else if (m == Material.ITEM_FRAME)
                    event.setCancelled(true);
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                if (kitCooldown.contains(player))
                    return;

                if (gm.getGameState() == GameState.WAITING || gm.getGameState() == GameState.STARTING) {
                    new KitGui(gm.getPlugin(), player).open(false);
                } else if (gm.getGameState() == GameState.LIVE && gm.getSpectators().contains(player)) {
                        new SpectatorGui(gm.getPlugin(), player).open(true);
                } else {
                    return;
                }

                kitCooldown.add(player);
                Bukkit.getServer().getScheduler().runTaskLater(gm.getPlugin(), () -> kitCooldown.remove(player), 20);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer() == gm.getShardCarrier()) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p != event.getPlayer())
                    p.setCompassTarget(event.getPlayer().getLocation());
                else
                    p.setCompassTarget(WorldManager.getInstance().alter);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.OAK_FENCE || event.getBlock().getType() == Material.NETHER_BRICK_FENCE) {
            event.setCancelled(false);
            return;
        }

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (gm.getGameState() != GameState.LIVE) {
            event.setCancelled(true);
            return;
        }

        // Allows arrow damage
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) { // If the damaged is a player and damager is an arrow
            Player player = (Player) event.getEntity();
            Player attacker = (Player) ((Arrow) event.getDamager()).getShooter();

            if (!(gm.getSurvivors().contains(attacker) && player == gm.getHerobrine())) { // Evals to true if either a) the attacker isnt a survivor b) the damaged isnt herobrine
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
                event.getDamager().remove();
                player.damage(4.5, attacker);
            }
            return;
        }

        // Hound attacks THB
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Wolf) {
            Player player = (Player) event.getEntity();
            if (player == gm.getHerobrine())
                event.setDamage(6);
            else
                event.setCancelled(true);
            return;
        }

        // TBH attacks hound
        if (event.getEntity() instanceof Wolf && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player != gm.getHerobrine())
                event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (gm.getSurvivors().contains(attacker)) { // If attacker is a survivor
            if (gm.getSurvivors().contains(player)) { // If the person taking damage is also a survivor, cancel
                event.setCancelled(true);
                return;
            }

            // Attacking THB
            double damage = gm.getHerobrineHitDamage(attacker.getInventory().getItemInMainHand().getType(), attacker.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE));
            if (damage != -1) event.setDamage(damage);

            PlayerUtil.animateHbHit(player.getLocation());

            // Delay the velocity change by a tick so it actually works
            Bukkit.getServer().getScheduler().runTaskLater(gm.getPlugin(), () -> player.setVelocity(new Vector(0, 0, 0)), 1);
        } else if (attacker == gm.getHerobrine()) {
            PlayerUtil.playSoundAt(attacker.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1f, 0f);
            double damage = gm.getSurvivorHitDamage(attacker.getInventory().getItemInMainHand().getType());
            if (damage != -1) event.setDamage(damage);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (gm.getGameState() != GameState.LIVE) {
            event.setCancelled(true);
            return;
        }

        if (gm.getSpectators().contains(player)) {
            event.setCancelled(true);
            return;
        }

        if (player == gm.getHerobrine() && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
            return;
        }

        if (player == gm.getHerobrine() && (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) && gm.getShardCount() != 3) {
            event.setCancelled(true);
            gm.getHerobrine().setFireTicks(1);
            gm.getHerobrine().setVisualFire(false);
        }

        if (gm.getSurvivors().contains(player)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                gm.getHbLastHit().add(player);
                Bukkit.getServer().getScheduler().runTaskLater(gm.getPlugin(), () -> gm.getHbLastHit().remove(player), 120);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setDeathMessage("");
        event.getDrops().clear();

        if (gm.getGameState() != GameState.LIVE) {
            player.setHealth(20);
            return;
        }

        Bukkit.getServer().getScheduler().runTaskLater(gm.getPlugin(), () -> player.spigot().respawn(), 40);

        if (player == gm.getHerobrine()) {
            if (player.getKiller() != null) {
                Message.broadcast(Message.format(ChatColor.AQUA + player.getKiller().getName() + ChatColor.GREEN + " has defeated " + ChatColor.RED + ChatColor.BOLD + "the HEROBRINE!"));
                StatManager.get().getPointsTracker().increment(player.getKiller().getUniqueId(), 30);
            }
            PlayerUtil.playSoundAt(player.getLocation(), Sound.ENTITY_WITHER_HURT, 1f, 1f);
            gm.end(WinType.SURVIVORS);
        } else {
            gm.getSurvivors().remove(player);
            if ((player.getKiller() != null && player.getKiller() == gm.getHerobrine()) || gm.getHbLastHit().contains(player)) {
                Message.broadcast(Message.format(ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.RED + ChatColor.BOLD + "the HEROBRINE!"));
                StatManager.get().getPointsTracker().increment(gm.getHerobrine().getUniqueId(), 5);
            }

            if (player == gm.getShardCarrier()) {
                if (player.getLastDamageCause() != null && player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
                    ShardHandler.destroy();
                } else {
                    ShardHandler.drop(player.getLocation().add(0, 1, 0));
                }
            }

            gm.endCheck();
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (gm.getGameState() == GameState.LIVE || gm.getGameState() == GameState.ENDING) {
            event.setRespawnLocation(WorldManager.getInstance().survivorSpawn);
            gm.makeSpectator(event.getPlayer());
        }
    }

    @EventHandler
    public void onPotion(PotionSplashEvent event) {
        for (LivingEntity e : event.getAffectedEntities()) {
            if (e instanceof Player) {
                Player player = (Player) e;
                if (gm.getHerobrine() == player || !gm.getSurvivors().contains(player))
                    Bukkit.getServer().getScheduler().runTaskLater(gm.getPlugin(), () -> {
                        event.getPotion().getEffects().forEach(effect -> {
                            player.removePotionEffect(effect.getType());
                        });

                        if (gm.getHerobrine() == player) {
                            if (gm.getShardCount() != 3)
                                PlayerUtil.addEffect(player, PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
                            PlayerUtil.addEffect(player, PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false);
                            PlayerUtil.addEffect(player, PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false);
                        }
                    }, 1);
            }
        }
    }

    @EventHandler
    public void onProjectile(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Arrow))
            return;

        Arrow arrow = (Arrow) event.getEntity();
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        StatManager sm = StatManager.get();
        Player player = event.getPlayer();
        GameRank rank = sm.getGameRank(player.getUniqueId());
        int points = sm.getPoints().get(player.getUniqueId());

        String endMessage = ChatColor.BLUE + player.getDisplayName() + ChatColor.DARK_GRAY + " » " + ChatColor.RESET + event.getMessage();

        if (gm.getGameState() == GameState.WAITING || gm.getGameState() == GameState.STARTING) {
            Message.broadcast("" + ChatColor.YELLOW + points + ChatColor.DARK_GRAY + " ▏ " + rank.getDisplay() + " " + endMessage);
        } else if (gm.getGameState() == GameState.LIVE || gm.getGameState() == GameState.ENDING) {
            if (player == gm.getHerobrine() || gm.getSurvivors().contains(player)) {
                Message.broadcast(rank.getDisplay() + " " + endMessage);
            } else {
                Message.broadcast("" + ChatColor.YELLOW + points + ChatColor.DARK_GRAY + " ▍ " + ChatColor.DARK_RED + "DEAD " + ChatColor.DARK_GRAY + "▏ " + endMessage);
            }
        }
    }
}
