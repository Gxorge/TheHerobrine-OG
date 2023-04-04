package uk.hotten.herobrine.kit.kits;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.abilities.HarmingTotemAbility;
import uk.hotten.herobrine.kit.abilities.HealingTotemAbility;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import uk.hotten.herobrine.kit.abilities.WooflessAbility;

public class SorcererKit extends Kit {

    public SorcererKit(GameManager gm, boolean requirePermission) {
        super(gm,
                "sorcerer",
                ChatColor.RED + "Sorcerer",
                "theherobrine.kit.unlockable.sorcerer",
                requirePermission,
                "",
                new GUIItem(Material.GOLDEN_BOOTS).displayName(ChatColor.RED + "Sorcerer")
        );
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
        addAbilityToPlayer(player, new WooflessAbility(gm, 1));
        addAbilityToPlayer(player, new HealingTotemAbility(gm, 2));
        addAbilityToPlayer(player, new HarmingTotemAbility(gm, 3));
    }

    @Override
    public void setupPlayer(Player player) {
        GUIItem axe = new GUIItem(Material.IRON_AXE).displayName(ChatColor.GREEN + "Axe of Death");

        player.getInventory().setItem(0, axe.build());

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.RED);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
    }
}
