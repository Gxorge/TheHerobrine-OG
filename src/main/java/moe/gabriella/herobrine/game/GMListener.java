package moe.gabriella.herobrine.game;

import moe.gabriella.herobrine.game.runnables.ShardHandler;
import moe.gabriella.herobrine.game.runnables.StartingRunnable;
import moe.gabriella.herobrine.utils.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffectType;

public class GMListener implements Listener {

    private GameManager gm;

    public GMListener(GameManager gm) { this.gm = gm; }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (gm.getGameState() == GameState.LIVE) {
            //todo spectator
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
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
        gm.getSurvivors().remove(event.getPlayer());
        if (gm.getGameState() == GameState.LIVE) {
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
        PlayerUtil.addEffect(player, PotionEffectType.SLOW, 400, 2, false, false);
        PlayerUtil.addEffect(player, PotionEffectType.CONFUSION, 300, 1, false, false);
        player.sendMessage(Message.format(ChatColor.GOLD + "You have a shard! Take it to the alter (Enchanting Table)!"));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE && player == gm.getShardCarrier()) {
                player.getInventory().getItemInMainHand();
                if (player.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                    gm.capture(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer() == gm.getShardCarrier())
            for (Player p : Bukkit.getServer().getOnlinePlayers()) { p.setCompassTarget(event.getPlayer().getLocation()); }
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        // Allows arrow damage
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) { // If the damaged is a player and damager is an arrow
            Projectile proj = (Projectile) event.getEntity();
            if (proj.getShooter() instanceof Player) { // if the entity who shot the arrow is a player
                Player player = (Player) event.getEntity();
                Player attacker = (Player) proj.getShooter();

                if (!(gm.getSurvivors().contains(attacker) && player == gm.getHerobrine())) { // Evals to true if either a) the attacker isnt a survivor b) the damaged isnt herobrine
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
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
            double damage = gm.getHitDamage(attacker.getInventory().getItemInMainHand().getType(), false);
            if (damage != -1)
                event.setDamage(damage);
        } else if (attacker == gm.getHerobrine()) {
            PlayerUtil.playSoundAt(attacker.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1f, 0f);
        } else {
            event.setCancelled(true);
        }
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
}
