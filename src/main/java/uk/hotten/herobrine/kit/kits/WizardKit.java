package uk.hotten.herobrine.kit.kits;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.abilities.DreamweaverAbility;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import uk.hotten.herobrine.utils.Message;

public class WizardKit extends Kit {

    public WizardKit(GameManager gm, boolean requirePermission) {
        super(gm,
                "wizard",
                "&5Wizard",
                "theherobrine.kit.classic.wizzard",
                requirePermission,
                Message.createArray(
                        "&8- &aBlade of Heroism &8&o(weapon)",
                        "",
                        "&8- &aElixir: Speed &8&o(x1)",
                        "&8- &aElixir: Strength &8&o(x1)",
                        "",
                        "&8- &aDreamweaver Bandage &8&o(x2)",
                        "   &7&oBandage yourself to full health"
                ),
                new GUIItem(Material.SPLASH_POTION).displayName("&5Wizard"));
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
        addAbilityToPlayer(player, new DreamweaverAbility(gm, 3, 2));
    }

    @Override
    public void setupPlayer(Player player) {
        // Items
        GUIItem blade = new GUIItem(Material.STONE_SWORD).displayName("&aBlade of Heroism").unbreakable(true);

        ItemStack swift = new ItemStack(Material.SPLASH_POTION);
        PotionMeta pmSwift = (PotionMeta) swift.getItemMeta();
        pmSwift.setBasePotionData(new PotionData(PotionType.SPEED, false, false));
        pmSwift.displayName(Message.legacySerializerAnyCase("&aElixir: Speed"));
        swift.setItemMeta(pmSwift);

        ItemStack strength = new ItemStack(Material.SPLASH_POTION);
        PotionMeta pmStrength = (PotionMeta) strength.getItemMeta();
        pmStrength.setBasePotionData(new PotionData(PotionType.STRENGTH, false, false));
        pmStrength.displayName(Message.legacySerializerAnyCase("&aElixir: Strength"));
        strength.setItemMeta(pmStrength);

        player.getInventory().setItem(0, blade.build());
        player.getInventory().setItem(1, swift);
        player.getInventory().setItem(2, strength);

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.PURPLE);
        helMeta.setUnbreakable(true);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);
        
        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).unbreakable(true).build());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).unbreakable(true).build());
        player.getInventory().setBoots(new GUIItem(Material.LEATHER_BOOTS).unbreakable(true).build());
    }
}
