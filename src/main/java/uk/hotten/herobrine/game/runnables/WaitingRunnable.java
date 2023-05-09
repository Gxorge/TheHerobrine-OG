package uk.hotten.herobrine.game.runnables;

import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
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
                PlayerUtil.broadcastActionbar(gm.getGameLobby(), "" + ChatColor.YELLOW + "Waiting for " + ChatColor.AQUA + required + ChatColor.YELLOW + " player" + (required == 1 ? "" : "s"));
            else
                PlayerUtil.broadcastActionbar(gm.getGameLobby(), ChatColor.RED + "Waiting for server operator");
        } else if (time == 5 || time == 6 || time == 6.5) {
            PlayerUtil.broadcastActionbar(gm.getGameLobby(), ChatColor.AQUA + "Playing " + ChatColor.GRAY + "» " + ChatColor.YELLOW + "TheHerobrine" + ChatColor.GOLD + " on " + ChatColor.AQUA + gm.getGameLobby().getLobbyId());
        } else if (time == 7 || time == 8 || time == 8.5) {
            PlayerUtil.broadcastActionbar(gm.getGameLobby(), ChatColor.LIGHT_PURPLE + gm.getNetworkWeb());
        } else if (time >= 9){
            time = 0;
        }
    }
}
