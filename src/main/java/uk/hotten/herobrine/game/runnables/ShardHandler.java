package uk.hotten.herobrine.game.runnables;

import org.bukkit.*;
import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.*;
import uk.hotten.herobrine.world.WorldManager;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ShardHandler extends BukkitRunnable {

    int timer = 30;
    int despawnTimer = 300; // 5 minutes
    Random random = new Random();
    static Item shard;
    static GameManager gm = GameManager.get();

    @Override
    public void run() {
        if (gm.getGameState() != GameState.LIVE || gm.getShardState() == ShardState.INACTIVE) {
            cancel();
            return;
        }

        switch (gm.getShardState()) {
            case WAITING: {
                despawnTimer = 300;
                timer--;
                for (Player p : Bukkit.getServer().getOnlinePlayers()) p.setCompassTarget(WorldManager.getInstance().alter);
                if (timer == 0)
                    spawn();
                break;
            }
            case SPAWNED: {
                despawnTimer -= 1;
                Random r = new Random();
                int n = r.nextInt(10 - 1 + 1) + 1;
                if (n < 3) shard.getLocation().getWorld().strikeLightningEffect(shard.getLocation().clone().add(0, 1, 0));
                if (despawnTimer % 2 == 0) PlayerUtil.playSoundAt(shard.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
                if (despawnTimer == 0) {
                    shard.remove();
                    gm.setShardState(ShardState.WAITING);
                    Message.broadcast(Message.format(ChatColor.GRAY + "The shard has been DESTROYED! Work faster next time..."));
                }
                break;
            }
        }
    }


    private void spawn() {
        Random rand = new Random();
        Location spawn = WorldManager.getInstance().shardSpawns.get(rand.nextInt(WorldManager.getInstance().shardSpawns.size()));

        shard = spawn.getWorld().dropItem(spawn.add(0, 1, 0), createShard());

        spawn.getWorld().strikeLightningEffect(spawn.add(0, 1, 0));
        gm.setShardState(ShardState.SPAWNED);
        timer = random.nextInt(16) + 30; // random number between 30 and 45 for the next shard spawn
        Console.debug("Next shard time to be " + timer);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) p.setCompassTarget(spawn);

        PlayerUtil.broadcastTitle("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "A Shard has spawned!", ChatColor.AQUA + "Use your compass to find it!", 10, 60, 10);
        Message.broadcast(Message.format(ChatColor.LIGHT_PURPLE  + "A new shard has " + ChatColor.AQUA + ChatColor.BOLD + "been SUMMONED!"));
    }

    public static void drop(Location loc) {
        shard = loc.getWorld().dropItem(loc.add(0, 1, 0), createShard());
        loc.getWorld().strikeLightningEffect(loc.add(0, 1, 0));
        gm.setShardState(ShardState.SPAWNED);
        PlayerUtil.broadcastTitle("", ChatColor.AQUA + "The shard has been " + ChatColor.RED + ChatColor.BOLD + "dropped!", 10, 60, 10);
    }

    private static ItemStack createShard() {
        GUIItem shardItem = new GUIItem(Material.NETHER_STAR);
        shardItem.displayName(ChatColor.RED + "Shard of " + ShardName.getRandom().getName());
        return shardItem.build();
    }
}
