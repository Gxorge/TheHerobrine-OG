package uk.hotten.herobrine.game.runnables;

import org.bukkit.Effect;
import org.bukkit.block.BlockFace;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

public class HerobrineSmokeRunnable extends BukkitRunnable {

    GameManager gm = GameManager.get();

    @Override
    public void run() {
        if (gm.getGameState() != GameState.LIVE) {
            cancel();
            return;
        }

        Location loc = gm.getHerobrine().getLocation().clone().add(0, 2, 0);
        loc.getWorld().playEffect(loc, Effect.SMOKE, 0);
    }
}
