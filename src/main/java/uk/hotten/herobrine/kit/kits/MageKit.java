package uk.hotten.herobrine.kit.kits;

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
                "&bMage",
                "theherobrine.kit.unlockable.mage",
                requirePermission,
                Message.createArray(
                        "&8- &6Elder's Sword &8&o(weapon)",
                        "",
                        "&8- &aWater of &lReckoning &8&o(bow)",
                        "&8- &bMana &lArrow &8&o(x32)",
                        "",
                        "&8- &cOverwhelming &lLove &8&o(x1)",
                        "   &7&oHeal all your survivors 3 hearts"
                ),
                new GUIItem(Material.WOODEN_SWORD).displayName("&bMage")
        );
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
        addAbilityToPlayer(player, new LoveAbility(gm, 3));
    }

    @Override
    public void setupPlayer(Player player) {
        GUIItem sword = new GUIItem(Material.WOODEN_SWORD).displayName("&6Elder's Sword").unbreakable(true);
        GUIItem bow = new GUIItem(Material.BOW).displayName("&aWater of &lReckoning").unbreakable(true);
        GUIItem arrow = new GUIItem(Material.ARROW).displayName("&bMana &lArrow").amount(32);

        ItemStack healing = new ItemStack(Material.POTION);
        PotionMeta pmHealing = (PotionMeta) healing.getItemMeta();
        pmHealing.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
        pmHealing.displayName(Message.legacySerializerAnyCase("&e&lPotion: &aSelf Healing"));
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
