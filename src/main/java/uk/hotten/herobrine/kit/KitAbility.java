package uk.hotten.herobrine.kit;

import lombok.Getter;
import uk.hotten.herobrine.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class KitAbility implements Listener {

    public GameManager gm;

    @Getter private String displayName;

    public KitAbility(GameManager gm, String displayName) {
        this.gm = gm;
        this.displayName = displayName;
    }

    /**
     * @implNote Runs when the game starts, called from (apply in Kit)
     * @param player
     */
    public void apply(Player player) {

    }

}
