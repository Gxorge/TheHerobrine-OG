package moe.gabriella.herobrine.world.data;

import lombok.Getter;
import org.bukkit.ChatColor;

public class Datapoint {

    @Getter private DatapointType type;
    @Getter private Integer x;
    @Getter private Integer y;
    @Getter private Integer z;

    public Datapoint(DatapointType type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Datapoint() { }

}
