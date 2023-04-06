package uk.hotten.herobrine.kit.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.KitAbility;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.PlayerUtil;

public class ProtSpiritAbility extends KitAbility {

    public int slot;
    public int amount;
    public Player player;

    public ProtSpiritAbility(GameManager gm, int slot, int amount) {
        super(gm, "Protection Spirit!");
        this.slot = slot;
        this.amount = amount;
    }

    @Override
    public void apply(Player player) {
        this.player = player;
        GUIItem item = new GUIItem(Material.ENDER_PEARL).displayName(ChatColor.AQUA + "Protection " + ChatColor.BOLD + "Spirit!").amount(amount);

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
            if (player.getInventory().getItemInMainHand().getType() == Material.ENDER_PEARL) {
                event.setCancelled(true);

                if (isOnCooldown(player))
                    return;

                PlayerUtil.removeAmountOfItem(player, player.getInventory().getItemInMainHand(), 1);
                new ProtSpiritHandler(player).runTaskTimerAsynchronously(gm.getPlugin(), 0, 10);
                startCooldown(player);
            }
        }
    }
}
