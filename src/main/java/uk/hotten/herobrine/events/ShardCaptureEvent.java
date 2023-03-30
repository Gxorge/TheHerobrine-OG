package uk.hotten.herobrine.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShardCaptureEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter private Player player;

    public ShardCaptureEvent(Player player) {
        this.player = player;
    }


    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}