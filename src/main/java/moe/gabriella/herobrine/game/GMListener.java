package moe.gabriella.herobrine.game;

import moe.gabriella.herobrine.game.runnables.ShardHandler;
import moe.gabriella.herobrine.game.runnables.StartingRunnable;
import moe.gabriella.herobrine.kit.KitGui;
import moe.gabriella.herobrine.utils.*;
import moe.gabriella.herobrine.world.WorldManager;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.texture.ItemTexture;

public class GMListener implements Listener {

    private GameManager gm;

    public GMListener(GameManager gm) { this.gm = gm; }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (gm.getGameState() == GameState.LIVE) {
            gm.makeSpectator(event.getPlayer());
            return;
        }

        event.setJoinMessage("");
        Message.broadcast(Message.format("" + ChatColor.AQUA + event.getPlayer().getName() + " " + ChatColor.YELLOW + "has joined!"));
        gm.getSurvivors().add(event.getPlayer());
        if (gm.getSurvivors().size() >= gm.getRequiredToStart()) {
            if (gm.getGameState() != GameState.STARTING) {
                gm.setGameState(GameState.STARTING);
                new StartingRunnable().runTaskTimerAsynchronously(gm.getPlugin(), 0, 20);
            } else {
                if (gm.getSurvivors().size() >= gm.getMaxPlayers()-3 && !gm.stAlmost && gm.startTimer > 30) {
                    Message.broadcast(Message.format("" + ChatColor.GREEN + "We almost have a full server! Shortening timer to 30 seconds!"));
                    gm.stAlmost = true;
                    gm.startTimer = 30;
                } else if (gm.getSurvivors().size() >= gm.getMaxPlayers() && !gm.stFull && gm.startTimer > 10) {
                    Message.broadcast(Message.format("" + ChatColor.GREEN + "We have a full server! Starting in 10 seconds!"));
                    gm.stFull = true;
                    gm.startTimer = 10;
                }
            }
        }

        gm.hubInventory(event.getPlayer());
        gm.setKit(event.getPlayer(), gm.getSavedKit(event.getPlayer()), false);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
        gm.getSurvivors().remove(event.getPlayer());
        if (gm.getGameState() == GameState.LIVE) {
            if (event.getPlayer() == gm.getShardCarrier()) {
                ShardHandler.drop(event.getPlayer().getLocation());
            }

            gm.endCheck();
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();

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
        gm.setShardState(ShardState.CARRYING);
        gm.setShardCarrier(player);
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
        } else if (gm.getGameState() == GameState.WAITING || gm.getGameState() == GameState.STARTING) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS)
                    new KitGui(gm.getPlugin(), player).open(false);
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
                event.setDamage(4.5);
                animateHbHit(player.getLocation());
            }
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
            double damage = gm.getSurvivorHitDamage(attacker.getInventory().getItemInMainHand().getType(), attacker.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE));
            if (damage != -1) event.setDamage(damage);

            animateHbHit(player.getLocation());

            player.setVelocity(new Vector(0, 0, 0));
        } else if (attacker == gm.getHerobrine()) {
            PlayerUtil.playSoundAt(attacker.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1f, 0f);
            double damage = gm.getHerobrineHitDamage(attacker.getInventory().getItemInMainHand().getType());
            if (damage != -1) event.setDamage(damage);
        } else {
            event.setCancelled(true);
        }
    }

    private void animateHbHit(Location loc) {
        PlayerUtil.playSoundAt(loc, Sound.ENTITY_BLAZE_HURT, 1f, 1f);
        PlayerUtil.playSoundAt(loc, Sound.ENTITY_IRON_GOLEM_ATTACK, 1f, 1f);

        ParticleEffect.ITEM_CRACK.display(loc, 0, 1, 0, 0.5f, 5, new ItemTexture(new ItemStack(Material.BLAZE_POWDER)), Bukkit.getOnlinePlayers());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (gm.getGameState() != GameState.LIVE)
            return;

        if (gm.getSpectators().contains(player)) {
            event.setCancelled(true);
            return;
        }

        if (player == gm.getHerobrine() && event.getCause() == EntityDamageEvent.DamageCause.FALL)
            event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setDeathMessage("");
        event.getDrops().clear();

        if (gm.getGameState() != GameState.LIVE) {
            player.setHealth(20);
            return;
        }

        if (player == gm.getHerobrine()) {
            if (player.getKiller() != null)
                Message.broadcast(Message.format(ChatColor.AQUA + player.getKiller().getName() + ChatColor.GREEN + " has defeated " + ChatColor.RED + ChatColor.BOLD + "the HEROBRINE!"));
            PlayerUtil.playSoundAt(player.getLocation(), Sound.ENTITY_WITHER_HURT, 1f, 1f);
            gm.end(WinType.SURVIVORS);
        } else {
            gm.getSurvivors().remove(player);
            if (player.getKiller() != null && player.getKiller() == gm.getHerobrine())
                Message.broadcast(Message.format(ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.RED + ChatColor.BOLD + "the HEROBRINE!"));

            if (player == gm.getShardCarrier()) {
                ShardHandler.drop(player.getLocation());
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
                    event.setCancelled(true);
            }
        }
    }
}
