package uk.hotten.herobrine.game;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import org.bukkit.entity.*;
import uk.hotten.herobrine.kit.KitGui;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;
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

public class GMListener implements Listener {

    private GameManager gameManager;
    private GameLobby gameLobby;
    private MVWorldManager mvWorldManager;
    private ArrayList<Player> kitCooldown = new ArrayList<>();

    public GMListener(GameManager gm, GameLobby gl) {
        this.gameManager = gm;
        this.gameLobby = gl;
        mvWorldManager = LobbyManager.getInstance().getMultiverseCore().getMVWorldManager();
    }

    @EventHandler
    public void onJoinViaWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (!player.getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        onJoinLogic(player);
    }

    @EventHandler
    public void onJoinViaLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        onJoinLogic(player);
    }

    private void onJoinLogic(Player player) {
        if (!gameManager.canJoin(player)) {
            player.teleport(mvWorldManager.getSpawnWorld().getSpawnLocation());
            player.sendMessage(Message.format(ChatColor.RED + "This lobby is full."));
            return;
        }

        gameLobby.getPlayers().add(player);

        gameLobby.getStatManager().check(player.getUniqueId());

        gameManager.getScoreboards().put(player, ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler() {
            @Override
            public String getTitle(Player player) {
                return "" + ChatColor.YELLOW + ChatColor.BOLD + "Your Stats";
            }

            @Override
            public List<Entry> getEntries(Player player) {
                return new EntryBuilder()
                        .next(ChatColor.AQUA + "Points: " + ChatColor.RESET + gameLobby.getStatManager().getPoints().get(player.getUniqueId()))
                        .next(ChatColor.AQUA + "Captures: " + ChatColor.RESET + gameLobby.getStatManager().getCaptures().get(player.getUniqueId()))
                        .next(ChatColor.AQUA + "Kills: " + ChatColor.RESET + gameLobby.getStatManager().getKills().get(player.getUniqueId()))
                        .next(ChatColor.AQUA + "Deaths: " + ChatColor.RESET + gameLobby.getStatManager().getDeaths().get(player.getUniqueId()))
                        .build();
            }
        }).setUpdateInterval(1));
        gameManager.getScoreboards().get(player).activate();


        gameManager.updateTags(GameManager.ScoreboardUpdateAction.CREATE);
        gameManager.setTags(player, null, null, GameManager.ScoreboardUpdateAction.CREATE);

        if (gameManager.getGameState() == GameState.LIVE) {
            gameManager.makeSpectator(player);
            return;
        }

        Message.broadcast(gameLobby, Message.format("" + ChatColor.AQUA + player.getName() + " " + ChatColor.YELLOW + "has joined!"));
        gameManager.getSurvivors().add(player);
        gameManager.startCheck();

        gameLobby.getWorldManager().getPlayerVotes().put(player, 0);
        gameLobby.getWorldManager().sendVotingMessage(player);
        gameManager.hubInventory(player);
        gameManager.setKit(player, gameManager.getSavedKit(player), true);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(player.getWorld().getSpawnLocation());
    }

    @EventHandler
    public void onLeaveViaQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!gameLobby.getPlayers().contains(player))
            return;

        onLeaveLogic(player);
    }

    @EventHandler
    public void onLeaveViaWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld().getName().startsWith(gameLobby.getLobbyId()) && event.getFrom().getName().startsWith(gameLobby.getLobbyId()))
            return;

        if (player.getWorld().getName().startsWith(gameLobby.getLobbyId()) && !event.getFrom().getName().startsWith(gameLobby.getLobbyId()))
            return;

        if (!event.getFrom().getName().startsWith(gameLobby.getLobbyId()))
            return;

        onLeaveLogic(player);
    }

    private void onLeaveLogic(Player player) {
        gameLobby.getPlayers().remove(player);
        if (!gameManager.getSpectators().contains(player))
            Message.broadcast(gameLobby, Message.format("" + ChatColor.AQUA + player.getName() + " " + ChatColor.YELLOW + "has quit."));
        gameManager.getSurvivors().remove(player);
        gameManager.getSpectators().remove(player);

        if (gameManager.getScoreboards().containsKey(player))
            gameManager.getScoreboards().get(player).deactivate();
        gameManager.getScoreboards().remove(player);
        gameManager.getTeamPrefixes().remove(player);
        gameManager.getTeamColours().remove(player);

        WorldManager wm = gameLobby.getWorldManager();
        if (wm.getPlayerVotes().getOrDefault(player, 0) != 0)
            wm.getVotingMaps().get(wm.getPlayerVotes().get(player)).decrementVotes();
        wm.getPlayerVotes().remove(player);


        if (gameManager.getGameState() == GameState.LIVE) {
            if (player == gameManager.getShardCarrier()) {
                gameManager.getShardHandler().drop(player.getLocation());
            }

            // If ran straight away, it still thinks THB is online if they were the quitter
            Bukkit.getServer().getScheduler().runTaskLater(gameManager.getPlugin(), gameManager::endCheck, 1);
            return;
        }

        if (gameManager.getPassUser() == player) {
            gameManager.setPassUser(null);
            Message.broadcast(gameManager.getGameLobby(), Message.format(ChatColor.GOLD + player.getName() + " has left and will no-longer be Herobrine."), "theherobrine.command.setherobrine");
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!event.getEntity().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();

        if (gameManager.getGameState() != GameState.LIVE)
            return;

        event.getItem().getItemStack();
        if (event.getItem().getItemStack().getType() != Material.NETHER_STAR) {
            event.setCancelled(true);
            return;
        }

        if (!gameManager.getSurvivors().contains(player)) {
            event.setCancelled(true);
            return;
        }

        for (Player p : gameLobby.getPlayers()) {
            if (p == gameManager.getHerobrine()) continue;
            PlayerUtil.sendTitle(p, "" + ChatColor.GREEN + ChatColor.BOLD + player.getName() + ChatColor.DARK_AQUA + " has picked up the shard!", ChatColor.YELLOW + "Help them return it!", 5, 60, 5);
        }
        PlayerUtil.sendTitle(gameManager.getHerobrine(), "" + ChatColor.GREEN + ChatColor.BOLD + player.getName() + ChatColor.DARK_AQUA + " has picked up the shard!", ChatColor.YELLOW + "Maybe target them first", 5, 60, 5);
        gameManager.getShardHandler().getShardTitle().remove();
        gameManager.setShardState(ShardState.CARRYING);
        gameManager.setShardCarrier(player);
        gameManager.setTags(player, "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Shard: ", ChatColor.LIGHT_PURPLE, GameManager.ScoreboardUpdateAction.UPDATE);
        PlayerUtil.broadcastSound(gameLobby, Sound.ENTITY_BAT_DEATH, 1f, 0f);
        PlayerUtil.addEffect(player, PotionEffectType.BLINDNESS, 100, 1, false, false);
        PlayerUtil.addEffect(player, PotionEffectType.SLOW, 600, 2, false, false);
        PlayerUtil.addEffect(player, PotionEffectType.CONFUSION, 300, 1, false, false);
        player.sendMessage(Message.format(ChatColor.GOLD + "You have a shard! Take it to the alter (Enchanting Table)!"));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        Player player = event.getPlayer();

        if (gameManager.getGameState() == GameState.LIVE) {
            if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) && gameManager.getSpectators().contains(player)) {
                new SpectatorGui(gameManager.getPlugin(), player, gameManager).open(true);
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock() == null)
                    return;
                Material m = event.getClickedBlock().getType();

                if (m == Material.ENCHANTING_TABLE)
                    event.setCancelled(true);

                if (m == Material.ENCHANTING_TABLE && player == gameManager.getShardCarrier()) {
                    event.setCancelled(true);
                    player.getInventory().getItemInMainHand();
                    if (player.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                        gameManager.capture(player);
                    }
                } else if (m == Material.ITEM_FRAME)
                    event.setCancelled(true);
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                if (kitCooldown.contains(player))
                    return;

                if (gameManager.getGameState() == GameState.WAITING || gameManager.getGameState() == GameState.STARTING) {
                    new KitGui(gameManager.getPlugin(), player, gameManager).open(false);
                } else if (gameManager.getGameState() == GameState.LIVE && gameManager.getSpectators().contains(player)) {
                        new SpectatorGui(gameManager.getPlugin(), player, gameManager).open(true);
                } else {
                    return;
                }

                kitCooldown.add(player);
                Bukkit.getServer().getScheduler().runTaskLater(gameManager.getPlugin(), () -> kitCooldown.remove(player), 20);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        if (event.getPlayer() == gameManager.getShardCarrier()) {
            for (Player p : gameLobby.getPlayers()) {
                if (p != event.getPlayer())
                    p.setCompassTarget(event.getPlayer().getLocation());
                else
                    p.setCompassTarget(gameLobby.getWorldManager().alter);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        if (event.getBlock().getType() == Material.OAK_FENCE || event.getBlock().getType() == Material.NETHER_BRICK_FENCE) {
            event.setCancelled(false);
            return;
        }

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (!event.getEntity().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        event.setFoodLevel(20);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!event.getEntity().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        if (gameManager.getGameState() != GameState.LIVE) {
            event.setCancelled(true);
            return;
        }

        // Allows arrow damage
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) { // If the damaged is a player and damager is an arrow
            Player player = (Player) event.getEntity();
            Player attacker = (Player) ((Arrow) event.getDamager()).getShooter();

            if (!(gameManager.getSurvivors().contains(attacker) && player == gameManager.getHerobrine())) { // Evals to true if either a) the attacker isnt a survivor b) the damaged isnt herobrine
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
            if (player == gameManager.getHerobrine())
                event.setDamage(6);
            else
                event.setCancelled(true);
            return;
        }

        // TBH attacks hound
        if (event.getEntity() instanceof Wolf && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (player != gameManager.getHerobrine())
                event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (gameManager.getSurvivors().contains(attacker)) { // If attacker is a survivor
            if (gameManager.getSurvivors().contains(player)) { // If the person taking damage is also a survivor, cancel
                event.setCancelled(true);
                return;
            }

            // Attacking THB
            double damage = gameManager.getHerobrineHitDamage(attacker.getInventory().getItemInMainHand().getType(), attacker.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE));
            if (damage != -1) event.setDamage(damage);

            PlayerUtil.animateHbHit(gameLobby, player.getLocation());

            // Delay the velocity change by a tick so it actually works
            Bukkit.getServer().getScheduler().runTaskLater(gameManager.getPlugin(), () -> player.setVelocity(new Vector(0, 0, 0)), 1);
        } else if (attacker == gameManager.getHerobrine()) {
            PlayerUtil.playSoundAt(attacker.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1f, 0f);
            double damage = gameManager.getSurvivorHitDamage(attacker.getInventory().getItemInMainHand().getType());
            if (damage != -1) event.setDamage(damage);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event) {
        if (!event.getEntity().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (gameManager.getGameState() != GameState.LIVE) {
            event.setCancelled(true);
            return;
        }

        if (gameManager.getSpectators().contains(player)) {
            event.setCancelled(true);
            return;
        }

        if (player == gameManager.getHerobrine() && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
            return;
        }

        if (player == gameManager.getHerobrine() && (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) && gameManager.getShardCount() != 3) {
            event.setCancelled(true);
            gameManager.getHerobrine().setFireTicks(1);
            gameManager.getHerobrine().setVisualFire(false);
        }

        if (gameManager.getSurvivors().contains(player)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                gameManager.getHbLastHit().add(player);
                Bukkit.getServer().getScheduler().runTaskLater(gameManager.getPlugin(), () -> gameManager.getHbLastHit().remove(player), 120);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDeath(PlayerDeathEvent event) {
        if (!event.getEntity().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        Player player = event.getEntity();
        event.setDeathMessage("");
        event.getDrops().clear();

        if (gameManager.getGameState() != GameState.LIVE) {
            player.setHealth(20);
            return;
        }

        Bukkit.getServer().getScheduler().runTaskLater(gameManager.getPlugin(), () -> player.spigot().respawn(), 40);

        if (player == gameManager.getHerobrine()) {
            if (player.getKiller() != null) {
                Message.broadcast(gameLobby, Message.format(ChatColor.AQUA + player.getKiller().getName() + ChatColor.GREEN + " has defeated " + ChatColor.RED + ChatColor.BOLD + "the HEROBRINE!"));
                gameLobby.getStatManager().getPointsTracker().increment(player.getKiller().getUniqueId(), 30);
            }
            PlayerUtil.playSoundAt(player.getLocation(), Sound.ENTITY_WITHER_HURT, 1f, 1f);
            gameManager.end(WinType.SURVIVORS);
        } else {
            gameManager.getSurvivors().remove(player);
            if ((player.getKiller() != null && player.getKiller() == gameManager.getHerobrine()) || gameManager.getHbLastHit().contains(player)) {
                Message.broadcast(gameLobby, Message.format(ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.RED + ChatColor.BOLD + "the HEROBRINE!"));
                gameLobby.getStatManager().getPointsTracker().increment(gameManager.getHerobrine().getUniqueId(), 5);
            }

            if (player == gameManager.getShardCarrier()) {
                if (player.getLastDamageCause() != null && player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
                    gameManager.getShardHandler().destroy();
                } else {
                    gameManager.getShardHandler().drop(player.getLocation().add(0, 1, 0));
                }
            }

            gameManager.endCheck();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        if (!gameLobby.getPlayers().contains(event.getPlayer()))
            return;

        if (gameManager.getGameState() == GameState.LIVE || gameManager.getGameState() == GameState.ENDING) {
            event.setRespawnLocation(gameLobby.getWorldManager().survivorSpawn);
            gameManager.makeSpectator(event.getPlayer());
        }
    }

    @EventHandler
    public void onPotion(PotionSplashEvent event) {
        if (!event.getEntity().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        for (LivingEntity e : event.getAffectedEntities()) {
            if (e instanceof Player) {
                Player player = (Player) e;
                if (gameManager.getHerobrine() == player || !gameManager.getSurvivors().contains(player))
                    Bukkit.getServer().getScheduler().runTaskLater(gameManager.getPlugin(), () -> {
                        event.getPotion().getEffects().forEach(effect -> {
                            player.removePotionEffect(effect.getType());
                        });

                        if (gameManager.getHerobrine() == player) {
                            if (gameManager.getShardCount() != 3)
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
        if (!event.getEntity().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        if (!(event.getEntity() instanceof Arrow))
            return;

        Arrow arrow = (Arrow) event.getEntity();
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        event.setCancelled(true);

        StatManager sm = gameLobby.getStatManager();
        Player player = event.getPlayer();
        GameRank rank = sm.getGameRank(player.getUniqueId());
        int points = sm.getPoints().get(player.getUniqueId());

        String endMessage = ChatColor.BLUE + player.getDisplayName() + ChatColor.DARK_GRAY + " » " + ChatColor.RESET + event.getMessage();

        if (gameManager.getGameState() == GameState.WAITING || gameManager.getGameState() == GameState.STARTING) {
            Message.broadcast(gameLobby, "" + ChatColor.YELLOW + points + ChatColor.DARK_GRAY + " ▏ " + rank.getDisplay() + " " + endMessage);
        } else if (gameManager.getGameState() == GameState.LIVE || gameManager.getGameState() == GameState.ENDING) {
            if (player == gameManager.getHerobrine() || gameManager.getSurvivors().contains(player)) {
                Message.broadcast(gameLobby, rank.getDisplay() + " " + endMessage);
            } else {
                Message.broadcast(gameLobby, "" + ChatColor.YELLOW + points + ChatColor.DARK_GRAY + " ▍ " + ChatColor.DARK_RED + "DEAD " + ChatColor.DARK_GRAY + "▏ " + endMessage);
            }
        }
    }
}
