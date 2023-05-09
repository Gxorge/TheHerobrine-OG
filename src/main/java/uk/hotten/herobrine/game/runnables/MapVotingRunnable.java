package uk.hotten.herobrine.game.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.world.WorldManager;

public class MapVotingRunnable extends BukkitRunnable {

    private int time = 0;
    private GameManager gm;
    private WorldManager wm;

    public MapVotingRunnable(GameLobby gameLobby) {
        this.gm = gameLobby.getGameManager();
        this.wm = gameLobby.getWorldManager();
    }

    @Override
    public void run() {

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
