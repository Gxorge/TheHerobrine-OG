package uk.hotten.herobrine.kit.kits;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import uk.hotten.herobrine.utils.Message;

public class ArcherKit extends Kit {

    public ArcherKit(GameManager gm, boolean requirePermission) {
        super(gm,
                "archer",
                "&aArcher",
                "theherobrine.kit.classic.archer",
                requirePermission,
                Message.createArray(
                        "&8- &aHatchet of War &8&o(weapon)",
                        "",
                        "&8- &ePhoenix Bow &8&o(bow)",
                        "&8- &7Eagle Feather Quills &8&o(x64)"
                ),
                new GUIItem(Material.BOW).displayName("&aArcher")
        );
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
    }

    @Override
    public void setupPlayer(Player player) {
        // Items
        GUIItem bow = new GUIItem(Material.BOW).displayName("&ePhoenix Bow").enchantment(Enchantment.ARROW_KNOCKBACK, 1).unbreakable(true);
        GUIItem arrow = new GUIItem(Material.ARROW).displayName("&7Eagle Feather Quills").amount(64);
        GUIItem axe = new GUIItem(Material.IRON_AXE).displayName("&aHatchet of War").unbreakable(true);

        player.getInventory().setItem(0, bow.build());
        player.getInventory().setItem(1, arrow.build());
        player.getInventory().setItem(2, axe.build());
        
        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.GREEN);
        helMeta.setUnbreakable(true);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new GUIItem(Material.CHAINMAIL_CHESTPLATE).unbreakable(true).build());
        player.getInventory().setBoots(new GUIItem(Material.CHAINMAIL_BOOTS).unbreakable(true).build());
    }
}
