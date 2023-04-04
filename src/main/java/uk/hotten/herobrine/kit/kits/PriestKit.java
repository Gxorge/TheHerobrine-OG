package uk.hotten.herobrine.kit.kits;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.KitAbility;
import uk.hotten.herobrine.kit.abilities.DreamweaverAbility;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import uk.hotten.herobrine.kit.abilities.WisdomAbility;
import uk.hotten.herobrine.kit.abilities.WooflessAbility;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class PriestKit extends Kit {

    public PriestKit(GameManager gm, boolean requirePermission) {
        super(gm,
                "priest",
                ChatColor.WHITE + "Priest",
                "theherobrine.kit.classic.priest",
                requirePermission,
                "",
                new GUIItem(Material.BONE).displayName(ChatColor.WHITE + "Priest")
        );
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
        addAbilityToPlayer(player, new DreamweaverAbility(gm, 1, 2));
        addAbilityToPlayer(player, new WisdomAbility(gm, 2, 2));
        addAbilityToPlayer(player, new WooflessAbility(gm, 3));
    }

    @Override
    public void setupPlayer(Player player) {
        // Items
        GUIItem blade = new GUIItem(Material.STONE_SWORD).displayName(ChatColor.GREEN + "Blade of Heroism");

        player.getInventory().setItem(0, blade.build());

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.WHITE);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
    }
}
