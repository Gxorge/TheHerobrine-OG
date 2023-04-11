package uk.hotten.herobrine.kit.kits;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import uk.hotten.herobrine.utils.Message;
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
                Message.createArray(
                        ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Blade of Heroism" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (weapon)",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Handcrafted Bow" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (bow)",
                        ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Owl Arrows" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x32)",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.AQUA + "Speed I"
                ),
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
        GUIItem blade = new GUIItem(Material.STONE_SWORD).displayName(ChatColor.GREEN + "Blade of Heroism").unbreakable(true);
        GUIItem bow = new GUIItem(Material.BOW).displayName(ChatColor.GRAY + "Handcrafted Bow").unbreakable(true);
        GUIItem arrow = new GUIItem(Material.ARROW).displayName(ChatColor.GRAY + "Owl Arrows").amount(32);

        player.getInventory().setItem(0, blade.build());
        player.getInventory().setItem(1, bow.build());
        player.getInventory().setItem(2, arrow.build());

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.YELLOW);
        helMeta.setUnbreakable(true);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).unbreakable(true).build());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).unbreakable(true).build());
        player.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).unbreakable(true).build());
    }
}
