package moe.gabriella.herobrine.events;

import lombok.Getter;
import moe.gabriella.herobrine.utils.ShardState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShardStateUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter private ShardState prevState;
    @Getter private ShardState newState;

    public ShardStateUpdateEvent(ShardState prevState, ShardState newState) {
        this.prevState = prevState;
        this.newState = newState;
    }


    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}
