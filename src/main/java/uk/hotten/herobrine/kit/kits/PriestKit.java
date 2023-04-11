package uk.hotten.herobrine.kit.kits;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
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
import uk.hotten.herobrine.utils.Message;

public class PriestKit extends Kit {

    public PriestKit(GameManager gm, boolean requirePermission) {
        super(gm,
                "priest",
                ChatColor.WHITE + "Priest",
                "theherobrine.kit.classic.priest",
                requirePermission,
                Message.createArray(
                        ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Blade of Heroism" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (weapon)",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Dreamweaver Bandage" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x2)",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "Bandage yourself to full health",
                        "   ",
                        ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Notch's Wisdom" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x2)",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "Creates an aura of health to",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "heal survivors for 10 seconds",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.AQUA + "Summon Woofless" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x1)",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "It's dangerous to go alone, take a friend!"
                ),
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
        GUIItem blade = new GUIItem(Material.STONE_SWORD).displayName(ChatColor.GREEN + "Blade of Heroism").unbreakable(true);

        player.getInventory().setItem(0, blade.build());

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.WHITE);
        helMeta.setUnbreakable(true);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new GUIItem(Material.LEATHER_CHESTPLATE).unbreakable(true).build());
        player.getInventory().setLeggings(new GUIItem(Material.LEATHER_LEGGINGS).unbreakable(true).build());
        player.getInventory().setBoots(new GUIItem(Material.CHAINMAIL_BOOTS).unbreakable(true).build());
    }
}
