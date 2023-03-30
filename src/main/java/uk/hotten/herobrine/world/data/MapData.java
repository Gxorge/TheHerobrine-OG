package uk.hotten.herobrine.world.data;

import lombok.Getter;

import java.util.List;

public class MapData {

    @Getter private String name;
    @Getter private String builder;
    @Getter private List<Datapoint> datapoints;

    public MapData(String name, String builder, List<Datapoint> datapoints) {
        this.name = name;
        this.builder = builder;
        this.datapoints = datapoints;
    }

    public MapData() { }

}
