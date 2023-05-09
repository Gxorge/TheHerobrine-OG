package uk.hotten.herobrine.stat.trackers;

import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.stat.StatManager;
import uk.hotten.herobrine.stat.StatTracker;
import uk.hotten.herobrine.utils.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillsTracker extends StatTracker {

    private GameLobby gameLobby;

    public KillsTracker(StatManager sm, GameLobby gameLobby) {
        super(sm, "Kills", "kills", "How many kills you got!");
        this.gameLobby = gameLobby;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void death(PlayerDeathEvent event) {
        if (!event.getEntity().getWorld().getName().startsWith(gameLobby.getLobbyId()))
            return;

        Player player = event.getEntity();

        GameManager gm = gameLobby.getGameManager();
        if (gm.getGameState() != GameState.LIVE)
            return;

        if (player.getKiller() == null) {
            if (gm.getSurvivors().contains(player) && gm.getHbLastHit().contains(player)) {
                increment(gm.getHerobrine().getUniqueId(), 1);
            }
            return;
        }

        Player killer = player.getKiller();

        if (gm.getSurvivors().contains(player) || gm.getHerobrine() == player) {
            if (gm.getSurvivors().contains(killer) || gm.getHerobrine() == killer) {
                increment(killer.getUniqueId(), 1);
            }
        }
    }
}
