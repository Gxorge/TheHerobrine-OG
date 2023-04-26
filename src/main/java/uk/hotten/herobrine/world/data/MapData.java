package uk.hotten.herobrine.world.data;

import lombok.Getter;

import java.util.List;

public class MapData {

    @Getter private String name;
    @Getter private String builder;
    @Getter private double shardMin;
    @Getter private double shardMax;
    @Getter private List<Datapoint> datapoints;

    public MapData(String name, String builder, double shardMin, double shardMax, List<Datapoint> datapoints) {
        this.name = name;
        this.builder = builder;
        this.shardMin = shardMin;
        this.shardMax = shardMax;
        this.datapoints = datapoints;
    }

    public MapData() { }

}
