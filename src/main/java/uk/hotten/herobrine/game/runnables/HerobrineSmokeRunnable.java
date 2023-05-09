package uk.hotten.herobrine.game.runnables;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.GameState;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class HerobrineSmokeRunnable extends BukkitRunnable {

    private GameManager gm;
    private Player player;

    public HerobrineSmokeRunnable(GameManager gm) {
        this.gm = gm;
        this.player = gm.getHerobrine();
    }

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
