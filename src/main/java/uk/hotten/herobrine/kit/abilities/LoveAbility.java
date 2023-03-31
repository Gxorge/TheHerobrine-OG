package uk.hotten.herobrine.kit.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.KitAbility;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.PlayerUtil;

public class LoveAbility extends KitAbility {

    public int slot;
    public Player player;

    public LoveAbility(GameManager gm, int slot) {
        super(gm, "Overwhelming Love");
        this.slot = slot;
    }

    @Override
    public void apply(Player player) {
        this.player = player;
        GUIItem item = new GUIItem(Material.FEATHER).displayName(ChatColor.RED + "Overwhelming " + ChatColor.BOLD + "Love");

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
            if (player.getInventory().getItemInMainHand().getType() == Material.FEATHER) {
                PlayerUtil.removeAmountOfItem(player, player.getInventory().getItemInMainHand(), 1);
                for (Player p : gm.getSurvivors()) {
                    PlayerUtil.increaseHealth(p, 6); // increase by 3 hearts
                    PlayerUtil.playSoundAt(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                }
            }
        }
    }
}
