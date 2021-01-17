package moe.gabriella.herobrine.game.runnables;

import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.GameState;
import moe.gabriella.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class WaitingRunnable extends BukkitRunnable {

    double time = 0;

    @Override
    public void run() {
        if (GameManager.getInstance().getGameState() != GameState.WAITING) {
            cancel();
            return;
        }

        time += 0.5;

        if (time <= 4.5) {
            int required = GameManager.getInstance().getRequiredToStart() - GameManager.getInstance().getSurvivors().size();
            PlayerUtil.broadcastActionbar("" + ChatColor.YELLOW + "Waiting for " + ChatColor.AQUA + required + ChatColor.YELLOW + " player" + (required == 1 ? "" : "s"));
        } else if (time == 5 || time == 6 || time == 6.5) {
            PlayerUtil.broadcastActionbar(ChatColor.AQUA + "Playing " + ChatColor.GRAY + "» " + ChatColor.YELLOW + "TheHerobrine");
        } else if (time == 7 || time == 8 || time == 8.5) {
            PlayerUtil.broadcastActionbar(ChatColor.LIGHT_PURPLE + GameManager.getInstance().getNetworkName());
        } else if (time >= 9){
            time = 0;
        }
    }
}
