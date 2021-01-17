package moe.gabriella.herobrine.kit.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BatBombHandler extends BukkitRunnable {

    Item coal;
    ArrayList<Entity> bats;

    public BatBombHandler(Item coal) {
        this.coal = coal;
        bats = new ArrayList<>();
    }

    @Override
    public void run() {
        try { TimeUnit.SECONDS.sleep(1); } catch (Exception e) { e.printStackTrace(); }
        Location loc = coal.getLocation();
        coal.remove();
        for (int i = 0; i<15; i++) {
            bats.add(loc.getWorld().spawnEntity(loc, EntityType.BAT));
        }
        try { TimeUnit.SECONDS.sleep(3); } catch (Exception e) { e.printStackTrace(); }
        for (Entity bat : bats) {
            Location batLoc = bat.getLocation();
            bat.remove();
            batLoc.getWorld().createExplosion(batLoc, 2f, false, false);
        }
    }
}
