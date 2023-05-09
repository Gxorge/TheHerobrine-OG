package uk.hotten.herobrine.events;

import lombok.Getter;
import uk.hotten.herobrine.utils.ShardState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShardStateUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter private ShardState prevState;
    @Getter private ShardState newState;
    @Getter private String lobbyId;

    public ShardStateUpdateEvent(ShardState prevState, ShardState newState, String lobbyId) {
        this.prevState = prevState;
        this.newState = newState;
        this.lobbyId = lobbyId;
    }


    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}
