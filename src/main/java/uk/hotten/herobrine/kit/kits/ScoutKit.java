package uk.hotten.herobrine.kit.kits;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

public class ScoutKit extends Kit {

    public ScoutKit(GameManager gm, boolean requirePermission) {
        super(gm,
                "scout",
                ChatColor.YELLOW + "Scout",
                "theherobrine.kit.classic.scout",
                requirePermission,
                "",
                new GUIItem(Material.FEATHER).displayName(ChatColor.YELLOW + "Scout")
        );
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
    }

    @Override
    public void setupPlayer(Player player) {
        // Effects
        PlayerUtil.addEffect(player, PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false);

        // Items
        GUIItem blade = new GUIItem(Material.STONE_SWORD).displayName(ChatColor.GREEN + "Blade of Heroism");
        GUIItem bow = new GUIItem(Material.BOW).displayName(ChatColor.GRAY + "Handcrafted Bow");
        GUIItem arrow = new GUIItem(Material.ARROW).displayName(ChatColor.GRAY + "Owl Arrows").amount(32);

        player.getInventory().setItem(0, blade.build());
        player.getInventory().setItem(1, bow.build());
        player.getInventory().setItem(2, arrow.build());

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.YELLOW);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
    }
}
