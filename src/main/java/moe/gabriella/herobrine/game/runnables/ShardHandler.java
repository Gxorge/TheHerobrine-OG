package moe.gabriella.herobrine.game.runnables;

import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.GameState;
import moe.gabriella.herobrine.utils.PlayerUtil;
import moe.gabriella.herobrine.utils.ShardState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ShardHandler extends BukkitRunnable {

    int timer = 30;
    int despawnTimer = 240; // 4 minutes
    Item shard;
    GameManager gm = GameManager.getInstance();

    @Override
    public void run() {
        if (gm.getGameState() != GameState.LIVE || gm.getShardState() == ShardState.INACTIVE)
            return;

        switch (gm.getShardState()) {
            case WAITING: {
                despawnTimer = 240;
                timer--;
                if (timer == 0)
                    spawn();
                break;
            }
            case SPAWNED: {
                despawnTimer -= 1;
                Random r = new Random();
                int n = r.nextInt(10 - 1 + 1) + 1;
                if (n < 4) shard.getLocation().getWorld().strikeLightningEffect(shard.getLocation());
                if (despawnTimer == 0) {
                    //kill it
                }
                break;
            }
        }
    }


    private void spawn() {
        //todo decide where to spawn in
        Location spawn = gm.getHerobrine().getLocation();

        GUIItem shardItem = new GUIItem(Material.NETHER_STAR);
        shardItem.displayName(ChatColor.RED + "Shard of " + ChatColor.BOLD + "Fury");
        shard = spawn.getWorld().dropItem(spawn.add(0, 1, 0), shardItem.build());

        spawn.getWorld().strikeLightningEffect(spawn.add(0, 1, 0));
        gm.setShardState(ShardState.SPAWNED);
        timer = 45;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) { p.setCompassTarget(spawn); }

        PlayerUtil.broadcastTitle("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "A Shard has spawned!", ChatColor.AQUA + "Use your compass to find it!", 10, 60, 10);
    }
}
