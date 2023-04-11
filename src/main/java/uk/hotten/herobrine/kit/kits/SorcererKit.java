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
import uk.hotten.herobrine.utils.Message;

public class SorcererKit extends Kit {

    public SorcererKit(GameManager gm, boolean requirePermission) {
        super(gm,
                "sorcerer",
                ChatColor.RED + "Sorcerer",
                "theherobrine.kit.unlockable.sorcerer",
                requirePermission,
                Message.createArray(
                        ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Axe of Death" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (weapon)",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.AQUA + "Summon Woofless" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x1)",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "It's dangerous to go alone, take a friend!",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + ChatColor.BOLD + "Totem: " + ChatColor.GREEN + "Healing" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x1)",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "Creates an aura of health to heal",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "survivors for 60 seconds",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + ChatColor.BOLD + "Totem: " + ChatColor.RED + "Pain" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x1)",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "Creates an aura of pain to damage",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "The Herobrine for 60 seconds"
                ),
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
        GUIItem axe = new GUIItem(Material.IRON_AXE).displayName(ChatColor.GREEN + "Axe of Death").unbreakable(true);

        player.getInventory().setItem(0, axe.build());

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.RED);
        helMeta.setUnbreakable(true);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).unbreakable(true).build());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).unbreakable(true).build());
        player.getInventory().setBoots(new GUIItem(Material.GOLDEN_BOOTS).unbreakable(true).build());
    }
}
