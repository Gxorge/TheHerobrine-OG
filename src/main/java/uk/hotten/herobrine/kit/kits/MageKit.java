package uk.hotten.herobrine.kit.kits;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import uk.hotten.herobrine.kit.abilities.LoveAbility;
import uk.hotten.herobrine.utils.Message;

public class MageKit extends Kit {

    public MageKit(GameManager gm, boolean requirePermission) {
        super(gm,
                "mage",
                ChatColor.AQUA + "Mage",
                "theherobrine.kit.unlockable.mage",
                requirePermission,
                Message.createArray(
                        ChatColor.DARK_GRAY + "- " + ChatColor.GOLD + "Elder's Sword" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (weapon)",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Water of " + ChatColor.BOLD + "Reckoning" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (bow)",
                        ChatColor.DARK_GRAY + "- " + ChatColor.AQUA + "Mana " + ChatColor.BOLD + "Arrow" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x32)",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.RED + "Overwhelming " + ChatColor.BOLD + "Love" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x1)",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "Heal all your survivors 3 hearts"
                ),
                new GUIItem(Material.WOODEN_SWORD).displayName(ChatColor.AQUA + "Mage")
        );
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
        addAbilityToPlayer(player, new LoveAbility(gm, 3));
    }

    @Override
    public void setupPlayer(Player player) {
        GUIItem sword = new GUIItem(Material.WOODEN_SWORD).displayName(ChatColor.GOLD + "Elder's Sword").unbreakable(true);
        GUIItem bow = new GUIItem(Material.BOW).displayName(ChatColor.GREEN + "Water of " + ChatColor.BOLD + "Reckoning").unbreakable(true);
        GUIItem arrow = new GUIItem(Material.ARROW).displayName(ChatColor.AQUA + "Mana " + ChatColor.BOLD + "Arrow").amount(32);

        ItemStack healing = new ItemStack(Material.POTION);
        PotionMeta pmHealing = (PotionMeta) healing.getItemMeta();
        pmHealing.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
        pmHealing.setDisplayName("" + ChatColor.YELLOW + ChatColor.BOLD + "Potion: " + ChatColor.AQUA + "Self Healing");
        healing.setItemMeta(pmHealing);

        player.getInventory().setItem(0, sword.build());
        player.getInventory().setItem(1, bow.build());
        player.getInventory().setItem(2, arrow.build());
        player.getInventory().setItem(4, healing);

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.AQUA);
        helMeta.setUnbreakable(true);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).unbreakable(true).build());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).unbreakable(true).build());
        player.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).unbreakable(true).build());
    }
}
