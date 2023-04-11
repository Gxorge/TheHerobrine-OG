package uk.hotten.herobrine.kit.abilities;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.KitAbility;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WooflessAbility extends KitAbility {

    public int slot;
    public Player player;

    public WooflessAbility(GameManager gm, int slot) {
        super(gm, "Woofless");
        this.slot = slot;
    }

    @Override
    public void apply(Player player) {
        this.player = player;

        GUIItem bone = new GUIItem(Material.BONE).displayName(ChatColor.AQUA + "Summon Woofless");
        bone.lore(Message.addLinebreaks("" + ChatColor.GRAY + ChatColor.ITALIC + "It's dangerous to go alone, take a friend!", "" + ChatColor.GRAY + ChatColor.ITALIC));

        player.getInventory().setItem(slot, bone.build());
    }

    @EventHandler
    public void use(PlayerInteractEvent event) {
        if (gm.getGameState() != GameState.LIVE)
            return;

        Player player = event.getPlayer();

        if (this.player != player)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getInventory().getItemInMainHand().getType() == Material.BONE) {

                if (isOnCooldown(player))
                    return;

                Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
                wolf.setTamed(true);
                wolf.setOwner(player);
                PlayerUtil.playSoundAt(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1f, 1f);
                player.getInventory().remove(Material.BONE);
                startCooldown(player);
            }
        }
    }
}
