package uk.hotten.herobrine.game.runnables;

import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class WaitingRunnable extends BukkitRunnable {

    private double time = 0;
    private GameManager gm;

    public WaitingRunnable(GameManager gm) {
        this.gm = gm;
    }

    @Override
    public void run() {
        if (gm.getGameState() != GameState.WAITING) {
            cancel();
            return;
        }

        time += 0.5;

        if (time <= 4.5) {
            int required = gm.getRequiredToStart() - gm.getSurvivors().size();
            if (!gm.timerPaused)
                PlayerUtil.broadcastActionbar(gm.getGameLobby(), "&eWaiting for &b" + required + "&e player" + (required == 1 ? "" : "s"));
            else
                PlayerUtil.broadcastActionbar(gm.getGameLobby(), "&cWaiting for server operator");
        } else if (time == 5 || time == 6 || time == 6.5) {
            PlayerUtil.broadcastActionbar(gm.getGameLobby(), "&bPlaying &7» &eTheHerobrine &6on &b" + gm.getGameLobby().getLobbyId());
        } else if (time == 7 || time == 8 || time == 8.5) {
            PlayerUtil.broadcastActionbar(gm.getGameLobby(), "&d" + gm.getNetworkWeb());
        } else if (time >= 9){
            time = 0;
        }
    }
}
