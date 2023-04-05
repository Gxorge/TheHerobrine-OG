package uk.hotten.herobrine.world.data;

import lombok.Getter;

public class VotingMap {

    @Getter private int id;
    @Getter private MapData mapData;
    @Getter private String internalName;
    @Getter private int votes = 0;

    public VotingMap(int id, MapData mapData, String internalName) {
        this.id = id;
        this.mapData = mapData;
        this.internalName = internalName;
    }

    public void incrementVotes() {
        votes++;
    }

    public void decrementVotes() {
        votes--;
    }
}
