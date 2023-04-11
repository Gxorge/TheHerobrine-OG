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
import uk.hotten.herobrine.utils.Message;

public class HealingTotemAbility extends KitAbility {

    public int slot;
    public Player player;

    public HealingTotemAbility(GameManager gm, int slot) {
        super(gm, "Totem of Healing");
        this.slot = slot;
    }

    @Override
    public void apply(Player player) {
        this.player = player;
        GUIItem item = new GUIItem(Material.OAK_FENCE).displayName("" + ChatColor.YELLOW + ChatColor.BOLD + "Totem: " + ChatColor.GREEN + "Healing");
        item.lore(Message.addLinebreaks("" + ChatColor.GRAY + ChatColor.ITALIC + "Creates an aura of health to heal survivors for 60 seconds", "" + ChatColor.GRAY + ChatColor.ITALIC));

        player.getInventory().setItem(slot, item.build());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void use(BlockPlaceEvent event) {
        if (gm.getGameState() != GameState.LIVE)
            return;

        Player player = event.getPlayer();

        if (this.player != player)
            return;

        if (event.getBlock().getType() == Material.OAK_FENCE) {

            if (isOnCooldown(player))
                return;

            new HealingTotemHandler(event.getBlock()).runTaskTimerAsynchronously(gm.getPlugin(), 0, 20);
            startCooldown(player);
        }
    }

}
