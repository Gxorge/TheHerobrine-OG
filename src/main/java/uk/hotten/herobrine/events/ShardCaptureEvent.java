package uk.hotten.herobrine.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShardCaptureEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter private Player player;
    @Getter private String lobbyId;

    public ShardCaptureEvent(Player player, String lobbyId) {
        this.player = player;
        this.lobbyId = lobbyId;
    }


    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}