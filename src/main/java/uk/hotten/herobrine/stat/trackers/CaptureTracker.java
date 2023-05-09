package uk.hotten.herobrine.stat.trackers;

import uk.hotten.herobrine.events.ShardCaptureEvent;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.stat.StatManager;
import uk.hotten.herobrine.stat.StatTracker;
import org.bukkit.event.EventHandler;

public class CaptureTracker extends StatTracker {

    private GameLobby gameLobby;

    public CaptureTracker(StatManager sm, GameLobby gameLobby) {
        super(sm, "Captures", "captures", "How many shards you captured!");
        this.gameLobby = gameLobby;
    }

    @EventHandler
    public void capture(ShardCaptureEvent event) {
        if (!event.getLobbyId().equals(gameLobby.getLobbyId()))
            return;

        increment(event.getPlayer().getUniqueId(), 1);
        sm.getPointsTracker().increment(event.getPlayer().getUniqueId(), 10);
    }
}
