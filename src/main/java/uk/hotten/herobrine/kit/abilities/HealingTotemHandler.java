package uk.hotten.herobrine.kit.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.PlayerUtil;

public class HealingTotemHandler extends BukkitRunnable {

    Block block;
    int time = 0;
    BukkitTask wah;

    public HealingTotemHandler(Block block) {
        this.block = block;
        wah = new WisdomAnimationHandler(block.getLocation().add(0, 1, 0)).runTaskTimer(GameManager.get().getPlugin(), 0, 5);
    }

    @Override
    public void run() {
        if (time > 30) {
            Bukkit.getServer().getScheduler().runTask(GameManager.get().getPlugin(), () -> block.setType(Material.AIR));
            wah.cancel();
            cancel();
            return;
        }

        PlayerUtil.playSoundAt(block.getLocation(), Sound.ENTITY_CAT_PURREOW, 1f, 1f);

        for (Player p : GameManager.get().getSurvivors()) {
            if (PlayerUtil.getDistance(p, block.getLocation()) <= 6) {
                PlayerUtil.increaseHealth(p, 2);
                PlayerUtil.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }

        time++;
    }
}
