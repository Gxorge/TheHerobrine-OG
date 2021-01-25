package moe.gabriella.herobrine.stat.trackers;

import moe.gabriella.herobrine.stat.StatManager;
import moe.gabriella.herobrine.stat.StatTracker;

public class PointsTracker extends StatTracker {

    public PointsTracker(StatManager sm) {
        super(sm, "Points", "points", "Your points!");
    }

}
