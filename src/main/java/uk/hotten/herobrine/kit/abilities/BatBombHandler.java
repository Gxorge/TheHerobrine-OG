package uk.hotten.herobrine.kit.abilities;

import uk.hotten.herobrine.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BatBombHandler extends BukkitRunnable {

    private Item coal;
    private ArrayList<Entity> bats;
    private GameManager gm;

    public BatBombHandler(Item coal, GameManager gm) {
        this.coal = coal;
        bats = new ArrayList<>();
        this.gm = gm;
    }

    @Override
    public void run() {
        try { TimeUnit.SECONDS.sleep(1); } catch (Exception e) { e.printStackTrace(); }
        Location loc = coal.getLocation();
        Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), () -> coal.remove());
        for (int i = 0; i<15; i++) {
            Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), () -> bats.add(loc.getWorld().spawnEntity(loc, EntityType.BAT)));
        }
        try { TimeUnit.SECONDS.sleep(2); } catch (Exception e) { e.printStackTrace(); }
        Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), () -> {
            for (Entity bat : bats) {
                Location batLoc = bat.getLocation();
                bat.remove();
                batLoc.getWorld().createExplosion(batLoc, 3f, false, false);
            }
        });
    }
}
