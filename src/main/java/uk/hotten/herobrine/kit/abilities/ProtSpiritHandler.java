package uk.hotten.herobrine.kit.abilities;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.PlayerUtil;

import java.util.Random;

public class ProtSpiritHandler extends BukkitRunnable {

    private Player player;
    private int time = 0;
    private Random random = new Random();
    private GameManager gm;

    public ProtSpiritHandler(Player player, GameManager gm) {
        this.player = player;
        this.gm = gm;
    }

    @Override
    public void run() {
        if (!gm.getSurvivors().contains(player)) {
            cancel();
            return;
        }

        if (time > 24) {
            cancel();
            return;
        }

        // 1/3 for 1hp, 2hp or 3hp
        int rand = random.nextInt(3);
        if (rand == 0)
            PlayerUtil.increaseHealth(player, 1);
        else if (rand == 1)
            PlayerUtil.increaseHealth(player, 2);
        else
            PlayerUtil.increaseHealth(player, 3);

        PlayerUtil.playSoundAt(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1f, 0.5f);

        for (Player p : gm.getGameLobby().getPlayers()) {
            p.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 20);
        }

        time++;
    }
}
