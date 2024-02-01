package uk.hotten.herobrine.kit.abilities;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.KitAbility;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlindingAbility extends KitAbility {

    int slot;
    int amount;
    Player player;

    public BlindingAbility(GameManager gm, int slot, int amount) {
        super(gm, "Charge of Blinding");
        this.slot = slot;
        this.amount = amount;
    }

    @Override
    public void apply(Player player) {
        this.player = player;
        GUIItem charge = new GUIItem(Material.GOLD_NUGGET).displayName("&6Charge of &lBlinding!").amount(amount);
        charge.lore(Message.addLinebreaks("&7&oLaunches a charge that blinds survivors within a 6 block radius", "&7&o"));

        if (slot == -1)
            player.getInventory().addItem(charge.build());
        else
            player.getInventory().setItem(slot, charge.build());
    }

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith(gm.getGameLobby().getLobbyId()))
            return;

        if (gm.getGameState() != GameState.LIVE)
            return;

        Player player = event.getPlayer();
        Location l = player.getLocation();

        if (this.player != player)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getInventory().getItemInMainHand().getType() == Material.GOLD_NUGGET) {

                if (isOnCooldown(player))
                    return;

                Item nugget = player.getWorld().dropItem(l, new ItemStack(Material.GOLD_NUGGET));
                nugget.setVelocity(l.getDirection().normalize().multiply(2f));
                new BlindingHandler(nugget, gm).runTaskAsynchronously(gm.getPlugin());
                PlayerUtil.removeAmountOfItem(player, player.getInventory().getItemInMainHand(), 1);
                startCooldown(player);
            }
        }
    }
}
