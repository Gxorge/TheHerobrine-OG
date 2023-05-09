package uk.hotten.herobrine.kit.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.PlayerUtil;

public class HealingTotemHandler extends BukkitRunnable {

    private Block block;
    private int time = 0;
    private BukkitTask wah;
    private GameManager gm;

    public HealingTotemHandler(Block block, GameManager gm) {
        this.block = block;
        this.gm = gm;
        wah = new WisdomAnimationHandler(block.getLocation().add(0, 1, 0)).runTaskTimer(gm.getPlugin(), 0, 5);
    }

    @Override
    public void run() {
        if (time > 30) {
            Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), () -> block.setType(Material.AIR));
            wah.cancel();
            cancel();
            return;
        }

        PlayerUtil.playSoundAt(block.getLocation(), Sound.ENTITY_CAT_PURREOW, 1f, 1f);

        for (Player p : gm.getSurvivors()) {
            if (PlayerUtil.getDistance(p, block.getLocation()) <= 6) {
                PlayerUtil.increaseHealth(p, 2);
                PlayerUtil.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }

        time++;
    }
}
