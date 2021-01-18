package moe.gabriella.herobrine.kit;

import lombok.Getter;
import moe.gabriella.herobrine.game.GameManager;
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
