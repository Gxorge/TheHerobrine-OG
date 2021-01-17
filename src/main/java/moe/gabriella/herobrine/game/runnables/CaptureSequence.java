package moe.gabriella.herobrine.game.runnables;

import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.Message;
import moe.gabriella.herobrine.utils.PlayerUtil;
import moe.gabriella.herobrine.world.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class CaptureSequence extends BukkitRunnable {

    Player player;
    GameManager gm = GameManager.getInstance();

    public CaptureSequence(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        Location l = WorldManager.getInstance().alter;

        PlayerUtil.broadcastTitle("" + ChatColor.AQUA + ChatColor.BOLD + "Shard Captured", ChatColor.YELLOW + "by " + ChatColor.BOLD + player.getName(), 10, 60, 20);
        if (gm.getShardCount() == 3)
            Message.broadcast(Message.format("" + ChatColor.RED + ChatColor.BOLD + "Herobrine " + ChatColor.YELLOW + "is now " + ChatColor.GREEN + "Visible!"));
        else
            Message.broadcast(Message.format(ChatColor.AQUA + "" + gm.getShardCount() + ChatColor.GRAY + "/3 Shards Captured!"));
        PlayerUtil.playSoundAt(l, Sound.BLOCK_PORTAL_TRAVEL, 1f, 1f);
        PlayerUtil.playSoundAt(l, Sound.ENTITY_WITHER_DEATH, 0.5f, 1f);

        try { TimeUnit.SECONDS.sleep(4); } catch (Exception e) { e.printStackTrace(); }

        PlayerUtil.playSoundAt(l, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        new BukkitRunnable() {
            @Override
            public void run() {
                l.getWorld().strikeLightningEffect(l);
            }
        }.runTask(gm.getPlugin());

        //todo particles
    }
}
