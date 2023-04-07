package uk.hotten.herobrine.stat.trackers;

import uk.hotten.herobrine.events.ShardCaptureEvent;
import uk.hotten.herobrine.stat.StatManager;
import uk.hotten.herobrine.stat.StatTracker;
import org.bukkit.event.EventHandler;

public class CaptureTracker extends StatTracker {

    public CaptureTracker(StatManager sm) {
        super(sm, "Captures", "captures", "How many shards you captured!");
    }

    @EventHandler
    public void capture(ShardCaptureEvent event) {
        increment(event.getPlayer().getUniqueId(), 1);
        sm.getPointsTracker().increment(event.getPlayer().getUniqueId(), 10);
    }
}
