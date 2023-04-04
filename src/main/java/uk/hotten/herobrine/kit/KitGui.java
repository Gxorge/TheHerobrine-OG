package uk.hotten.herobrine.kit;

import uk.hotten.gxui.GUIBase;
import uk.hotten.gxui.GUIButton;
import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.herobrine.utils.Message;

public class KitGui extends GUIBase {

    public KitGui(JavaPlugin plugin, Player player) {
        super(plugin, player, ChatColor.GRAY + "Pick your class", 9, false);
    }

    @Override
    public void setupItems() {
        GameManager gm = GameManager.get();
        int curr = 0;
        for (Kit kit : gm.getKits()) {
            GUIItem item = kit.getDisplayItem();
            item.button(new GUIButton() {
                @Override
                public boolean leftClick() {
                    if (kit.getPermission() == null || (!kit.isRequirePermission() || getPlayer().hasPermission(kit.getPermission())))
                        applyKit(kit);
                    else {
                        getPlayer().sendMessage(Message.format(ChatColor.RED + "You haven't unlocked this kit yet!"));
                        return false;
                    }
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

    private void applyKit(Kit kit) {
        GameManager.get().setKit(getPlayer(), kit, true);
        getPlayer().closeInventory();
    }

    private int nextCurr(int curr) {
        if (curr == 3)
            return 6;
        else
            return (curr += 1);
    }
}
