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

public class PaladinKit extends Kit {

    public PaladinKit(GameManager gm) {
        super(gm,
                "paladin",
                ChatColor.GOLD + "Paladin",
                null,
                "",
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
        GUIItem sword = new GUIItem(Material.IRON_SWORD).displayName(ChatColor.GREEN + "Paladin's Might");

        player.getInventory().setItem(0, sword.build());

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.ORANGE);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
    }
}
