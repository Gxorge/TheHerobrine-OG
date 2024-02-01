package uk.hotten.herobrine.game.runnables;

import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.utils.PlayerUtil;
import uk.hotten.herobrine.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingRunnable extends BukkitRunnable {

    private boolean ignorePlayerCount;
    private GameManager gm;
    private WorldManager wm;

    public StartingRunnable(GameManager gm, WorldManager wm) {
        ignorePlayerCount = false;
        this.gm = gm;
        this.wm = wm;
    }

    public StartingRunnable(GameManager gm, WorldManager wm, boolean ignorePlayerCount) {
        this.ignorePlayerCount = ignorePlayerCount;
        this.gm = gm;
        this.wm = wm;
    }

    @Override
    public void run() {
        if ((gm.getRequiredToStart() > gm.getSurvivors().size() && !ignorePlayerCount) || (ignorePlayerCount && gm.getSurvivors().size() <= 1)) {
            Message.broadcast(gm.getGameLobby(), Message.format("&cStart cancelled! Waiting for players..."));
            gm.startWaiting();
            cancel();
            return;
        }

        if (gm.timerPaused) {
            Message.broadcast(gm.getGameLobby(), Message.format("&cStart cancelled! Waiting for server operator..."));
            gm.startWaiting();
            cancel();
            return;
        }

        gm.startTimer--;

        if (gm.startTimer == wm.getEndVotingAt())
            Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), () -> wm.selectAndLoadMapFromVote());

        if (gm.startTimer == 0) {
            Bukkit.getServer().getScheduler().runTask(gm.getPlugin(), gm::start);
            cancel();
            return;
        }

        String time = (gm.startTimer < 60 ? Message.formatTime(gm.startTimer) : Message.formatTimeFull(gm.startTimer));
        if (gm.startTimer > 5) {
            PlayerUtil.broadcastActionbar(gm.getGameLobby(), "&eGet ready! The game will start in &a" + time);
        } else {
            if (gm.startTimer <= 5)
                PlayerUtil.broadcastSound(gm.getGameLobby(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
            if (gm.startTimer <= 3)
                PlayerUtil.broadcastSound(gm.getGameLobby(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

            PlayerUtil.broadcastActionbar(gm.getGameLobby(), "&aGet ready! The game will start in &c" + time);
        }
    }
}
