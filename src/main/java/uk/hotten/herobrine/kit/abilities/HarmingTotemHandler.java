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

public class HarmingTotemHandler extends BukkitRunnable {

    Block block;
    Player placer;
    Player herobrine = GameManager.get().getHerobrine();
    int time = 0;

    public HarmingTotemHandler(Block block, Player placer) {
        this.block = block;
        this.placer = placer;
    }

    @Override
    public void run() {
        if (time > 30) {
            Bukkit.getServer().getScheduler().runTask(GameManager.get().getPlugin(), () -> block.setType(Material.AIR));
            cancel();
            return;
        }

        PlayerUtil.playSoundAt(block.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.spawnParticle(Particle.VILLAGER_ANGRY, block.getLocation().add(0, 1, 0), 1);
        }

        if (PlayerUtil.getDistance(herobrine, block.getLocation()) <= 5) {
            Bukkit.getServer().getScheduler().runTask(GameManager.get().getPlugin(), () -> herobrine.damage(2, placer));
            PlayerUtil.playSound(herobrine, Sound.ENTITY_ENDERMITE_HURT, 1f, 1f);
        }

        time++;
    }
}
