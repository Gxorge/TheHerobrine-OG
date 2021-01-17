package moe.gabriella.herobrine.kit.abilities;

import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.kit.KitAbility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LocatorAbility extends KitAbility {

    public LocatorAbility(GameManager gm) {
        super(gm, "Objective Locator");
    }

    @Override
    public void apply(Player player) {
        GUIItem item = new GUIItem(Material.COMPASS).displayName(ChatColor.GRAY + "Objective Locator");

        player.getInventory().setItem(8, item.build());
    }
}
