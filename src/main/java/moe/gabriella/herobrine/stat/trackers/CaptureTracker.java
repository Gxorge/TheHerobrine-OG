package moe.gabriella.herobrine.stat.trackers;

import moe.gabriella.herobrine.events.ShardCaptureEvent;
import moe.gabriella.herobrine.stat.StatManager;
import moe.gabriella.herobrine.stat.StatTracker;
import org.bukkit.event.EventHandler;

public class CaptureTracker extends StatTracker {

    public CaptureTracker(StatManager sm) {
        super(sm, "Captures", "captures", "How many shards you captured!");
    }

    @EventHandler
    public void capture(ShardCaptureEvent event) {
        increment(event.getPlayer().getUniqueId(), 1);
        sm.pointsTracker.increment(event.getPlayer().getUniqueId(), 10);
    }
}
