package uk.hotten.herobrine.game.runnables;

import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.PlayerUtil;
import uk.hotten.herobrine.utils.ShardState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NarrationRunnable extends BukkitRunnable {

    public double timer = 0;
    private GameManager gm;

    public NarrationRunnable(GameManager gm) {
        this.gm = gm;
    }

    @Override
    public void run() {
        if (gm.getGameState() != GameState.LIVE) {
            cancel();
            return;
        }

        if (gm.getShardCount() == 0 && gm.getShardState() == ShardState.WAITING && !gm.isShardPreviousDestroyed()) // I dunno i think it looks better without this at the start, and i think this was a bug on the hive too
            return;

        if (timer > 10)
            timer = 0;

        switch (gm.getShardState()) {
            case WAITING: {
                if (firstMsg())
                    all(ChatColor.AQUA + "Shard " + (gm.getShardCount() + 1) + " shall be summoned soon!");
                else
                    separate(ChatColor.RED + "BEWARE: The Herobrine is still a cloud of smoke!", ChatColor.AQUA + "Try eliminate all the Survivors", ChatColor.YELLOW + "You're out of the game! Left-click to spectate.");
                break;
            }
            case SPAWNED: {
                all(ChatColor.AQUA + "Use your compass to find the shard!");
                break;
            }
            case CARRYING: {
                if (firstMsg())
                    all("" + ChatColor.GREEN + ChatColor.BOLD + gm.getShardCarrier().getName() + ChatColor.DARK_AQUA + " has the shard!");
                else
                    separate(ChatColor.GREEN + "Protect your shard carrier", ChatColor.AQUA + "Kill the shard carrier first!", ChatColor.YELLOW + "You're out of the game! Left-click to spectate.");
                break;
            }
            case INACTIVE: {
                if (gm.getShardCount() != 3)
                    return;

                separate(ChatColor.RED + "Kill the Herobrine!", ChatColor.AQUA + "You are now visible!", ChatColor.YELLOW + "You're out of the game! Left-click to spectate.");
                break;
            }
        }
        timer += 0.5;
    }

    private boolean firstMsg() {
        return timer <= 5;
    }

    private void all(String msg) {
        PlayerUtil.broadcastActionbar(gm.getGameLobby(), msg);
    }

    private void separate(String survivors, String herobrine, String spectators) {
        for (Player p : gm.getSurvivors())
            PlayerUtil.sendActionbar(p, survivors);
        for (Player p : gm.getSpectators())
            PlayerUtil.sendActionbar(p, spectators);
        PlayerUtil.sendActionbar(gm.getHerobrine(), herobrine);
    }
}
