package moe.gabriella.herobrine.kit.abilities;

import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WisdomHandler extends BukkitRunnable {

    Location location;
    int time = 0;

    public WisdomHandler(Location location) {
        this.location = location;
    }

    @Override
    public void run() {
        if (time > 5) {
            cancel();
            return;
        }

        for (Player p : GameManager.get().getSurvivors()) {
            if (PlayerUtil.getDistance(p, location) <= 2.5) {
                if (p.getHealth() < 20) {
                    p.setHealth(p.getHealth() + 2);
                    PlayerUtil.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                }
            }
        }

        PlayerUtil.playSoundAt(location, Sound.ENTITY_CAT_PURREOW, 1f, 1f);
        PlayerUtil.playSoundAt(location, Sound.BLOCK_LAVA_POP, 1f, 1f);
        PlayerUtil.playSoundAt(location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.3f, 1f);
        location.getWorld().spawnParticle(Particle.REVERSE_PORTAL, location, 10);

        time++;
    }
}
