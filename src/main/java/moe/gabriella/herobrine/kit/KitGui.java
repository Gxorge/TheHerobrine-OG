package moe.gabriella.herobrine.kit;

import me.gabriella.gabsgui.GUIBase;
import me.gabriella.gabsgui.GUIButton;
import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.game.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class KitGui extends GUIBase {

    public KitGui(JavaPlugin plugin, Player player) {
        super(plugin, player, ChatColor.GRAY + "Pick your class", 9, false);
    }

    @Override
    public void setupItems() {
        GameManager gm = GameManager.getInstance();
        int curr = 0;
        for (Kit kit : gm.getKits()) {
            GUIItem item = kit.getDisplayItem();
            item.button(new GUIButton() {
                @Override
                public boolean leftClick() {
                    gm.setKit(getPlayer(), kit, true);
                    getPlayer().closeInventory();
                    return true;
                }

                @Override public boolean leftClickShift() { return false; }
                @Override public boolean rightClick() { return false; }
                @Override public boolean rightClickShift() { return false; }
            });
            addItem(curr, item);
            curr = nextCurr(curr);
        }

    }

    private int nextCurr(int curr) {
        if (curr == 3)
            return 6;
        else
            return (curr += 1);
    }
}
