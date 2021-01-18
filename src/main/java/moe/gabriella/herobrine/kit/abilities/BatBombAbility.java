package moe.gabriella.herobrine.kit.abilities;

import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.kit.KitAbility;
import moe.gabriella.herobrine.utils.GameState;
import moe.gabriella.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
        if (slot == -1)
            player.getInventory().addItem(bomb.build());
        else
            player.getInventory().setItem(slot, bomb.build());
    }

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if (gm.getGameState() != GameState.LIVE)
            return;

        Player player = event.getPlayer();
        Location l = player.getLocation();

        if (this.player != player)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getInventory().getItemInMainHand().getType() == Material.COAL) {
                Item coal = player.getWorld().dropItem(l, new ItemStack(Material.COAL));
                coal.setVelocity(l.getDirection().normalize().multiply(2f));
                new BatBombHandler(coal).runTaskAsynchronously(gm.getPlugin());
                PlayerUtil.removeAmountOfItem(player, player.getInventory().getItemInMainHand(), 1);
            }
        }
    }
}
