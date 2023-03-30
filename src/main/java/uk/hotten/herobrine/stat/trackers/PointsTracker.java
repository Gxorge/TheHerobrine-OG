package uk.hotten.herobrine.stat.trackers;

import uk.hotten.herobrine.stat.StatManager;
import uk.hotten.herobrine.stat.StatTracker;

public class PointsTracker extends StatTracker {

    public PointsTracker(StatManager sm) {
        super(sm, "Points", "points", "Your points!");
    }

}
