package uk.hotten.herobrine.game.runnables;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
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
    public static ArmorStand shardTitle;
    private static Location spawnLoc;
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
                checkShardLoc();
                if (despawnTimer == 0) {
                    shard.remove();
                    shardTitle.remove();
                    gm.setShardState(ShardState.WAITING);
                    Message.broadcast(Message.format(ChatColor.GRAY + "The shard has been DESTROYED! Work faster next time..."));
                }
                break;
            }
        }
    }


    private void spawn() {
        Random rand = new Random();
        spawnLoc = WorldManager.getInstance().shardSpawns.get(rand.nextInt(WorldManager.getInstance().shardSpawns.size()));

        shard = spawnLoc.getWorld().dropItem(spawnLoc.add(0, 1, 0), createShard());
        shard.setInvulnerable(true);
        spawnShardTitle();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) p.setCompassTarget(shard.getLocation());

        spawnLoc.getWorld().strikeLightningEffect(spawnLoc.add(0, 1, 0));
        gm.setShardState(ShardState.SPAWNED);
        timer = random.nextInt(16) + 30; // random number between 30 and 45 for the next shard spawn
        Console.debug("Next shard time to be " + timer);

        PlayerUtil.broadcastTitle("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "A Shard has spawned!", ChatColor.AQUA + "Use your compass to find it!", 10, 60, 10);
        Message.broadcast(Message.format(ChatColor.LIGHT_PURPLE  + "A new shard has " + ChatColor.AQUA + ChatColor.BOLD + "been SUMMONED!"));
    }

    public static void drop(Location loc) {
        shard = loc.getWorld().dropItem(loc.add(0, 1, 0), createShard());
        spawnLoc = shard.getLocation();
        shard.setInvulnerable(true);
        spawnShardTitle();
        loc.getWorld().strikeLightningEffect(loc.add(0, 1, 0));
        for (Player p : Bukkit.getServer().getOnlinePlayers()) p.setCompassTarget(shard.getLocation());
        gm.setTags(gm.getShardCarrier(), null, ChatColor.DARK_GREEN, GameManager.ScoreboardUpdateAction.UPDATE);

        gm.setShardState(ShardState.SPAWNED);
        gm.setShardCarrier(null);
        PlayerUtil.broadcastTitle("", ChatColor.AQUA + "The shard has been " + ChatColor.RED + ChatColor.BOLD + "dropped!", 10, 60, 10);
    }

    public static void destroy() {
        gm.setShardPreviousDestroyed(true);
        gm.setShardState(ShardState.WAITING);
        gm.setShardCarrier(null);

        if (shard != null)
            shard.remove();

        if (shardTitle != null)
            shardTitle.remove();

        PlayerUtil.broadcastTitle("", ChatColor.AQUA + "The shard has been " + ChatColor.RED + ChatColor.BOLD + "destroyed!", 10, 60, 10);
        Message.broadcast(Message.format(ChatColor.GRAY + "The shard has been DESTROYED! A new one shall be summoned soon..."));
    }

    private static ItemStack createShard() {
        GUIItem shardItem = new GUIItem(Material.NETHER_STAR);
        shardItem.displayName(ChatColor.RED + "Shard of " + ShardName.getRandom().getName());
        return shardItem.build();
    }

    private static void spawnShardTitle() {
        shardTitle = (ArmorStand) shard.getWorld().spawnEntity(shard.getLocation().subtract(0, 1.5, 0), EntityType.ARMOR_STAND);
        shardTitle.setVisible(false);
        shardTitle.setCustomName(ChatColor.AQUA + "The Shard");
        shardTitle.setCustomNameVisible(true);
        shardTitle.setGravity(false);
    }

    private void checkShardLoc() {
        if (shard.getLocation().getY() < WorldManager.getInstance().getGameMapData().getShardMin() || shard.getLocation().getY() > WorldManager.getInstance().getGameMapData().getShardMax()) {
            destroy();
            return;
        }

        if (shard.getLocation().getX() != spawnLoc.getX() || shard.getLocation().getZ() != spawnLoc.getZ()) {
            shard.teleport(new Location(spawnLoc.getWorld(), spawnLoc.getX(), shard.getLocation().getY(), spawnLoc.getZ()));
        }
        shardTitle.teleport(shard.getLocation().subtract(0, 1.5, 0));
    }
}
