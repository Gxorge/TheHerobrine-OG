package moe.gabriella.herobrine.kit.abilities;

import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.kit.KitAbility;
import moe.gabriella.herobrine.utils.GameState;
import moe.gabriella.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WisdomAbility extends KitAbility {

    public int slot;
    public Player player;

    public WisdomAbility(GameManager gm, int slot) {
        super(gm, "Notch's Wisdom");
        this.slot = slot;
    }

    @Override
    public void apply(Player player) {
        GUIItem wiz = new GUIItem(Material.BLAZE_POWDER).displayName(ChatColor.LIGHT_PURPLE + "Notch's Wisdom");

        player.getInventory().setItem(slot, wiz.build());
    }

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if (gm.getGameState() != GameState.LIVE)
            return;

        Player player = event.getPlayer();

        if (this.player != player)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getInventory().getItemInMainHand().getType() == Material.BLAZE_POWDER) {
                PlayerUtil.removeAmountOfItem(player, player.getInventory().getItemInMainHand(), 1);
                new WisdomHandler(player.getLocation());
            }
        }
    }
}
