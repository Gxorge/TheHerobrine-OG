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

    private Block block;
    private Player placer;
    private Player herobrine;
    private int time = 0;
    private GameManager gm;

    public HarmingTotemHandler(Block block, Player placer, GameManager gm) {
        this.block = block;
        this.placer = placer;
        this.gm = gm;
        this.herobrine = gm.getHerobrine();
    }

    @Override
    public void run() {
        if (time > 30) {
            Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), () -> block.setType(Material.AIR));
            cancel();
            return;
        }

        PlayerUtil.playSoundAt(block.getLocation(), Sound.ENTITY_BAT_HURT, 1f, 1f);
        for (Player p : gm.getGameLobby().getPlayers()) {
            p.spawnParticle(Particle.VILLAGER_ANGRY, block.getLocation().add(0, 1, 0), 1);
        }

        if (PlayerUtil.getDistance(herobrine, block.getLocation()) <= 6) {
            Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), () -> {
                PlayerUtil.decreaseHealth(herobrine, 2, placer);
                PlayerUtil.animateHbHit(gm.getGameLobby(), herobrine.getLocation());
            });
        }

        time++;
    }
}
