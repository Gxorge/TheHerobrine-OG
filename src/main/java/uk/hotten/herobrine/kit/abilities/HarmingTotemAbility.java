package uk.hotten.herobrine.kit.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.KitAbility;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.PlayerUtil;

public class HarmingTotemAbility extends KitAbility {

    public int slot;
    public Player player;

    public HarmingTotemAbility(GameManager gm, int slot) {
        super(gm, "Totem of Pain");
        this.slot = slot;
    }

    @Override
    public void apply(Player player) {
        this.player = player;
        GUIItem item = new GUIItem(Material.NETHER_BRICK_FENCE).displayName("" + ChatColor.YELLOW + ChatColor.BOLD + "Totem: " + ChatColor.GREEN + "Pain");

        player.getInventory().setItem(slot, item.build());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void use(BlockPlaceEvent event) {
        if (gm.getGameState() != GameState.LIVE)
            return;

        Player player = event.getPlayer();

        if (this.player != player)
            return;

        if (event.getBlock().getType() == Material.NETHER_BRICK_FENCE) {

            if (isOnCooldown(player))
                return;

            new HarmingTotemHandler(event.getBlock(), player).runTaskTimerAsynchronously(gm.getPlugin(), 0, 20);
            startCooldown(player);
        }
    }

}
