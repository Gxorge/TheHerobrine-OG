package uk.hotten.herobrine.events;

import lombok.Getter;
import uk.hotten.herobrine.utils.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter private GameState prevState;
    @Getter private GameState newState;

    public GameStateUpdateEvent(GameState prevState, GameState newState) {
        this.prevState = prevState;
        this.newState = newState;
    }


    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}
