package moe.gabriella.herobrine.kit;

import lombok.Getter;
import moe.gabriella.herobrine.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class KitAbility implements Listener {

    private GameManager gm;

    @Getter private String displayName;

    public KitAbility(GameManager gm, String displayName) {
        this.gm = gm;
        this.displayName = displayName;
    }

    /**
     * @implNote Runs during the loading of the game (called from setupKits)
     */
    public void initialize() {

    }

    /**
     * @implNote Runs when the game starts, called from (apply in Kit)
     * @param player
     */
    public void apply(Player player) {

    }

}
