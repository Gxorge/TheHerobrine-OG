package uk.hotten.herobrine.kit.kits;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.abilities.DreamweaverAbility;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class WizardKit extends Kit {

    public WizardKit(GameManager gm) {
        super(gm, "wizard", ChatColor.DARK_PURPLE + "Wizard", null, "", new GUIItem(Material.SPLASH_POTION).displayName(ChatColor.DARK_PURPLE + "Wizard"));
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
        addAbilityToPlayer(player, new DreamweaverAbility(gm, 3, 2));
    }

    @Override
    public void setupPlayer(Player player) {
        // Items
        GUIItem blade = new GUIItem(Material.STONE_SWORD).displayName(ChatColor.GREEN + "Blade of Heroism");

        ItemStack swift = new ItemStack(Material.SPLASH_POTION);
        PotionMeta pmSwift = (PotionMeta) swift.getItemMeta();
        pmSwift.setBasePotionData(new PotionData(PotionType.SPEED, false, false));
        pmSwift.setDisplayName(ChatColor.GREEN + "Elixir: Speed");
        swift.setItemMeta(pmSwift);

        ItemStack strength = new ItemStack(Material.SPLASH_POTION);
        PotionMeta pmStrength = (PotionMeta) strength.getItemMeta();
        pmStrength.setBasePotionData(new PotionData(PotionType.STRENGTH, false, false));
        pmStrength.setDisplayName(ChatColor.GREEN + "Elixir: Strength");
        strength.setItemMeta(pmStrength);

        player.getInventory().setItem(0, blade.build());
        player.getInventory().setItem(1, swift);
        player.getInventory().setItem(2, strength);

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.PURPLE);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);
        
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
    }
}
