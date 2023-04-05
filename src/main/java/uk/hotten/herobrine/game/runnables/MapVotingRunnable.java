package uk.hotten.herobrine.game.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.world.WorldManager;

public class MapVotingRunnable extends BukkitRunnable {

    private int time = 0;

    @Override
    public void run() {
        GameManager gm = GameManager.get();
        WorldManager wm = WorldManager.getInstance();

        if ((gm.getGameState() != GameState.WAITING && gm.getGameState() != GameState.STARTING) || !wm.isVotingRunning()) {
            cancel();
            return;
        }

        time++;

        if (time == 30) {
            wm.sendVotingMessage(null);
            time = 0;
        }
    }
}
