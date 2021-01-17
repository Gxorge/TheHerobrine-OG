package moe.gabriella.herobrine.kit;

import lombok.Getter;
import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class Kit implements Listener {

    private GameManager gm;

    @Getter private String internalName; // don't change this later, it will break things
    @Getter private String displayName;
    @Getter private String permission;
    @Getter private String desc;
    @Getter private GUIItem displayItem;
    @Getter private KitAbility[] abilities;

    public Kit(GameManager gm, String internalName, String displayName, String permission, String desc, GUIItem displayItem, KitAbility[] abilities) {
        this.gm = gm;

        this.internalName = internalName;
        this.displayName = displayName;
        this.permission = permission;
        this.desc = desc;
        this.displayItem = displayItem;
        this.abilities = abilities;
    }

    public void apply(Player player) {
        PlayerUtil.clearInventory(player);
        PlayerUtil.clearEffects(player);

        setupPlayer(player);
        for (KitAbility a : abilities)
            a.apply(player);
    }

    public boolean ownsKit(Player player) {
        if (permission == null)
            return true;

        return player.hasPermission(permission);
    }

    public abstract void setupPlayer(Player player);

}
