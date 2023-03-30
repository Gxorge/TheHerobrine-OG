package uk.hotten.herobrine.world.data;

import lombok.Getter;

import java.util.List;

public class MapBase {

    @Getter private List<String> maps;

    public MapBase(List<String> maps) {
        this.maps = maps;
    }

    public MapBase() { }

}
