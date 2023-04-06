package uk.hotten.herobrine.kit.abilities;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.KitAbility;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DreamweaverAbility extends KitAbility {

    public int slot;
    public int amount;
    public Player player;

    public DreamweaverAbility(GameManager gm, int slot, int amount) {
        super(gm, "Dreamweaver Bandage");
        this.slot = slot;
        this.amount = amount;
    }

    @Override
    public void apply(Player player) {
        this.player = player;
        GUIItem item = new GUIItem(Material.MAGMA_CREAM).displayName(ChatColor.GREEN + "Dreamweaver Bandage").amount(amount);

        if (slot == -1)
            player.getInventory().addItem(item.build());
        else
            player.getInventory().setItem(slot, item.build());
    }

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if (gm.getGameState() != GameState.LIVE)
            return;

        Player player = event.getPlayer();

        if (this.player != player)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getInventory().getItemInMainHand().getType() == Material.MAGMA_CREAM) {

                if (isOnCooldown(player))
                    return;

                player.setHealth(20);
                PlayerUtil.playSoundAt(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                PlayerUtil.removeAmountOfItem(player, player.getInventory().getItemInMainHand(), 1);
                startCooldown(player);
            }
        }
    }
}
