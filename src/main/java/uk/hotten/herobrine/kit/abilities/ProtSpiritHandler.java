package uk.hotten.herobrine.kit.abilities;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import uk.hotten.herobrine.utils.PlayerUtil;

public class ProtSpiritHandler extends BukkitRunnable {

    Player player;
    int time = 0;

    public ProtSpiritHandler(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (time > 24) {
            cancel();
            return;
        }

        PlayerUtil.increaseHealth(player, 1); // half a heart every half a second
        PlayerUtil.playSoundAt(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1f, 1f);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 20);
        }

        time++;
    }
}
