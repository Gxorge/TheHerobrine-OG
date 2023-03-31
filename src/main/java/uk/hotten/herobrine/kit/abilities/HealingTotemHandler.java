package uk.hotten.herobrine.kit.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.PlayerUtil;

public class HealingTotemHandler extends BukkitRunnable {

    Block block;
    int time = 0;

    public HealingTotemHandler(Block block) {
        this.block = block;
    }

    @Override
    public void run() {
        if (time > 30) {
            Bukkit.getServer().getScheduler().runTask(GameManager.get().getPlugin(), () -> block.setType(Material.AIR));
            cancel();
            return;
        }

        PlayerUtil.playSoundAt(block.getLocation(), Sound.ENTITY_CAT_PURREOW, 1f, 1f);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.spawnParticle(Particle.HEART, block.getLocation().add(0, 1, 0), 1);
        }

        for (Player p : GameManager.get().getSurvivors()) {
            if (PlayerUtil.getDistance(p, block.getLocation()) <= 3.5) {
                PlayerUtil.increaseHealth(p, 2);
                PlayerUtil.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }

        time++;
    }
}
