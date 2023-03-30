package uk.hotten.herobrine.kit.kits;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArcherKit extends Kit {

    public ArcherKit(GameManager gm) {
        super(gm,
                "archer",
                ChatColor.GREEN + "Archer",
                null,
                "",
                new GUIItem(Material.BOW).displayName(ChatColor.GREEN + "Archer")
        );
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
    }

    @Override
    public void setupPlayer(Player player) {
        // Items
        GUIItem bow = new GUIItem(Material.BOW).displayName(ChatColor.YELLOW + "Phoenix Bow").enchantment(Enchantment.ARROW_KNOCKBACK, 1);
        GUIItem arrow = new GUIItem(Material.ARROW).displayName(ChatColor.GRAY + "Eagle Feather Quills").amount(64);
        GUIItem axe = new GUIItem(Material.IRON_AXE).displayName(ChatColor.GREEN + "Hatchet of War");

        player.getInventory().setItem(0, bow.build());
        player.getInventory().setItem(1, arrow.build());
        player.getInventory().setItem(2, axe.build());
        
        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.GREEN);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
    }
}
