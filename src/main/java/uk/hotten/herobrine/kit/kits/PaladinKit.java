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
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import uk.hotten.herobrine.kit.abilities.ProtSpiritAbility;
import uk.hotten.herobrine.kit.abilities.WisdomAbility;
import uk.hotten.herobrine.utils.Message;

public class PaladinKit extends Kit {

    public PaladinKit(GameManager gm, boolean requirePermission) {
        super(gm,
                "paladin",
                ChatColor.GOLD + "Paladin",
                "theherobrine.kit.unlockable.paladin",
                requirePermission,
                Message.createArray(
                        ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Paladin's Might" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (weapon)",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.AQUA + "Protection " + ChatColor.BOLD + "Spirit!" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x3)",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "Protect yourself with a spirit of",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "rapid healing for 12 seconds",
                        "",
                        ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Notch's Wisdom" + ChatColor.DARK_GRAY + ChatColor.ITALIC + " (x3)",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "Creates an aura of health to",
                        "   " + ChatColor.GRAY + ChatColor.ITALIC + "heal survivors for 10 seconds"
                ),
                new GUIItem(Material.ENDER_PEARL).displayName(ChatColor.GOLD + "Paladin")
        );
    }

    @Override
    public void setupAbilities(Player player) {
        addAbilityToPlayer(player, new LocatorAbility(gm));
        addAbilityToPlayer(player, new ProtSpiritAbility(gm, 1, 3));
        addAbilityToPlayer(player, new WisdomAbility(gm, 2, 3));
    }

    @Override
    public void setupPlayer(Player player) {
        GUIItem sword = new GUIItem(Material.IRON_SWORD).displayName(ChatColor.GREEN + "Paladin's Might").unbreakable(true);

        player.getInventory().setItem(0, sword.build());

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.ORANGE);
        helMeta.setUnbreakable(true);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new GUIItem(Material.CHAINMAIL_CHESTPLATE).unbreakable(true).build());
        player.getInventory().setBoots(new GUIItem(Material.CHAINMAIL_BOOTS).unbreakable(true).build());
    }
}
