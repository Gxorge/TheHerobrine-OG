package uk.hotten.herobrine.kit.abilities;

import org.bukkit.scheduler.BukkitTask;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WisdomHandler extends BukkitRunnable {

    Location location;
    int time = 0;
    BukkitTask wah;

    public WisdomHandler(Location location) {
        this.location = location;
        wah = new WisdomAnimationHandler(location).runTaskTimer(GameManager.get().getPlugin(), 0, 5);
    }

    @Override
    public void run() {
        if (time > 10) {
            cancel();
            wah.cancel();
            return;
        }

        for (Player p : GameManager.get().getSurvivors()) {
            if (PlayerUtil.getDistance(p, location) <= 3) {
                PlayerUtil.increaseHealth(p, 2);
                PlayerUtil.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }

        PlayerUtil.playSoundAt(location, Sound.ENTITY_CAT_PURREOW, 1f, 1f);
        PlayerUtil.playSoundAt(location, Sound.BLOCK_LAVA_POP, 1f, 1f);
        PlayerUtil.playSoundAt(location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.3f, 1f);

        time++;
    }
}
