package uk.hotten.herobrine.kit.abilities;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.KitAbility;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BatBombAbility extends KitAbility {

    public int slot;
    public int amount;
    public Player player;
    
    public BatBombAbility(GameManager gm, int slot, int amount) {
        super(gm, "Bat Bomb");
        this.slot = slot;
        this.amount = amount;
    }

    @Override
    public void apply(Player player) {
        this.player = player;
        GUIItem bomb = new GUIItem(Material.COAL).displayName(ChatColor.DARK_GREEN + "Bat Bomb").amount(amount);
        bomb.lore(Message.addLinebreaks("" + ChatColor.GRAY + ChatColor.ITALIC + "Launches a pack of exploding bats, don't let them kill you...", "" + ChatColor.GRAY + ChatColor.ITALIC));

        if (slot == -1)
            player.getInventory().addItem(bomb.build());
        else
            player.getInventory().setItem(slot, bomb.build());
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
            if (player.getInventory().getItemInMainHand().getType() == Material.COAL) {

                if (isOnCooldown(player))
                    return;

                Item coal = player.getWorld().dropItem(l, new ItemStack(Material.COAL));
                coal.setVelocity(l.getDirection().normalize().multiply(2f));
                new BatBombHandler(coal).runTaskAsynchronously(gm.getPlugin());
                PlayerUtil.removeAmountOfItem(player, player.getInventory().getItemInMainHand(), 1);
                startCooldown(player);
            }
        }
    }
}
