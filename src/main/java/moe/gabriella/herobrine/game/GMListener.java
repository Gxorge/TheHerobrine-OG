package moe.gabriella.herobrine.game;

import moe.gabriella.herobrine.game.runnables.StartingRunnable;
import moe.gabriella.herobrine.utils.GameState;
import moe.gabriella.herobrine.utils.Message;
import moe.gabriella.herobrine.utils.PlayerUtil;
import moe.gabriella.herobrine.utils.WinType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        Message.broadcast(Message.format("" + ChatColor.AQUA + event.getPlayer().getName() + " " + ChatColor.YELLOW + " has joined!"));
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
            if (gm.getSurvivors().size() == 0) {
                gm.end(WinType.HEROBRINE);
            } else if (!gm.getHerobrine().isOnline()) {
                gm.end(WinType.SURVIVORS);
            }
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

        if (player == gm.getHerobrine()) {
            event.setCancelled(true);
            return;
        }

        for (Player p : gm.getSurvivors()) {
            PlayerUtil.sendTitle(p, "" + ChatColor.GREEN + ChatColor.BOLD + player.getName() + ChatColor.DARK_AQUA + " has picked up the shard", ChatColor.YELLOW + "Help them return it!", 5, 60, 5);
        }
        PlayerUtil.sendTitle(gm.getHerobrine(), "" + ChatColor.GREEN + ChatColor.BOLD + player.getName() + ChatColor.DARK_AQUA + " has picked up the shard", ChatColor.YELLOW + "Maybe target them first", 5, 60, 5);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTED_BOOK && player == gm.getShardCarrier()) {
                player.getInventory().getItemInMainHand();
                if (player.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
                    player.getInventory().remove(Material.NETHER_STAR);
                    //todo shard capture logic
                }
            }
        }
    }

}
