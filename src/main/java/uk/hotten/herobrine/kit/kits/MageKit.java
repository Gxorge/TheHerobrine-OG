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

public class MageKit extends Kit {

    public MageKit(GameManager gm) {
        super(gm,
                "mage",
                ChatColor.AQUA + "Mage",
                null,
                "",
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
        GUIItem sword = new GUIItem(Material.WOODEN_SWORD).displayName(ChatColor.GOLD + "Elder's Sword");
        GUIItem bow = new GUIItem(Material.BOW).displayName(ChatColor.YELLOW + "Phoenix Bow");
        GUIItem arrow = new GUIItem(Material.ARROW).displayName(ChatColor.GRAY + "Eagle Feather Quills").amount(32);

        ItemStack healing = new ItemStack(Material.POTION);
        PotionMeta pmHealing = (PotionMeta) healing.getItemMeta();
        pmHealing.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
        pmHealing.setDisplayName(ChatColor.LIGHT_PURPLE + "Elixir: Health II");
        healing.setItemMeta(pmHealing);

        player.getInventory().setItem(0, sword.build());
        player.getInventory().setItem(1, bow.build());
        player.getInventory().setItem(2, arrow.build());
        player.getInventory().setItem(4, healing);

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.AQUA);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
    }
}
