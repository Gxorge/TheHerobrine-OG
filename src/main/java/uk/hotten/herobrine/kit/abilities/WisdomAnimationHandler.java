package uk.hotten.herobrine.kit.abilities;

import org.bukkit.Location;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

public class WisdomAnimationHandler extends BukkitRunnable {

    private Location loc;

    public WisdomAnimationHandler(Location loc) {
        this.loc = loc;
    }

    @Override
    public void run() {
        EnderSignal es = (EnderSignal) loc.getWorld().spawnEntity(loc, EntityType.ENDER_SIGNAL);
        es.setTargetLocation(loc);
        es.setDropItem(false);
        es.setDespawnTimer(80);
    }
}
