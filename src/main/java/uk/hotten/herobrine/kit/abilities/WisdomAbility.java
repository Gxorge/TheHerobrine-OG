package uk.hotten.herobrine.kit.abilities;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.KitAbility;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WisdomAbility extends KitAbility {

    public int slot;
    public int amount;
    public Player player;

    public WisdomAbility(GameManager gm, int slot, int amount) {
        super(gm, "Notch's Wisdom");
        this.slot = slot;
        this.amount = amount;
    }

    @Override
    public void apply(Player player) {
        this.player = player;
        GUIItem wiz = new GUIItem(Material.BLAZE_POWDER).displayName(ChatColor.GREEN + "Notch's Wisdom").amount(amount);
        wiz.lore(Message.addLinebreaks("" + ChatColor.GRAY + ChatColor.ITALIC + "Creates an aura of health to heal survivors for 10 seconds", "" + ChatColor.GRAY + ChatColor.ITALIC));

        player.getInventory().setItem(slot, wiz.build());
    }

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if (!event.getPlayer().getWorld().getName().startsWith(gm.getGameLobby().getLobbyId()))
            return;

        if (gm.getGameState() != GameState.LIVE)
            return;

        Player player = event.getPlayer();

        if (this.player != player)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getInventory().getItemInMainHand().getType() == Material.BLAZE_POWDER) {

                if (isOnCooldown(player))
                    return;

                PlayerUtil.removeAmountOfItem(player, player.getInventory().getItemInMainHand(), 1);
                new WisdomHandler(player.getLocation()).runTaskTimerAsynchronously(gm.getPlugin(), 0, 20);
                startCooldown(player);
            }
        }
    }
}
