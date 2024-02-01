package uk.hotten.herobrine.kit.abilities;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.KitAbility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.utils.Message;

public class LocatorAbility extends KitAbility {

    public LocatorAbility(GameManager gm) {
        super(gm, "Objective Locator");
    }

    @Override
    public void apply(Player player) {
        GUIItem item = new GUIItem(Material.COMPASS).displayName("&7Objective Locator");
        item.lore(Message.addLinebreaks("&7&oUse this to locate the shard and the alter", "&7&o"));

        player.getInventory().setItem(8, item.build());
    }
}
