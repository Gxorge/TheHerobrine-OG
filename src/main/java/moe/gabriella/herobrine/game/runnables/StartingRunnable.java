package moe.gabriella.herobrine.game.runnables;

import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.GameState;
import moe.gabriella.herobrine.utils.Message;
import moe.gabriella.herobrine.utils.PlayerUtil;
import moe.gabriella.herobrine.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingRunnable extends BukkitRunnable {

    @Override
    public void run() {
        GameManager gm = GameManager.get();

        if (gm.getRequiredToStart() > gm.getSurvivors().size()) {
            Message.broadcast(Message.format("" + ChatColor.RED + "Cancelled! Waiting for players..."));
            gm.startWaiting();
            cancel();
            return;
        }

        gm.startTimer--;

        if (gm.startTimer == 10)
            Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), () -> WorldManager.getInstance().selectAndLoadMapFromVote());

        if (gm.startTimer == 0) {
            Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), gm::start);
            cancel();
            return;
        }

        String time = (gm.startTimer < 60 ? Message.formatTime(gm.startTimer) : Message.formatTimeFull(gm.startTimer));
        if (gm.startTimer > 5) {
            PlayerUtil.broadcastActionbar("" + ChatColor.YELLOW + "Get ready! The game will start in " + ChatColor.GREEN + time);
        } else {
            if (gm.startTimer <= 5)
                PlayerUtil.broadcastSound(Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
            if (gm.startTimer <= 3)
                PlayerUtil.broadcastSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

            PlayerUtil.broadcastActionbar("" + ChatColor.GREEN + "Get ready! The game will start in " + ChatColor.RED + time);
        }
    }
}
