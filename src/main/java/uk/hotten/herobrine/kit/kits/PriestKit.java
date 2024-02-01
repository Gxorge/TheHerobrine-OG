package uk.hotten.herobrine.kit.kits;

import uk.hotten.gxui.GUIItem;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.kit.Kit;
import uk.hotten.herobrine.kit.abilities.DreamweaverAbility;
import uk.hotten.herobrine.kit.abilities.LocatorAbility;
import uk.hotten.herobrine.kit.abilities.WisdomAbility;
import uk.hotten.herobrine.kit.abilities.WooflessAbility;
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
                "&fPriest",
                "theherobrine.kit.classic.priest",
                requirePermission,
                Message.createArray(
                        "&8- &aBlade of Heroism &8&o(weapon)",
                        "",
                        "&8- &aDreamweaver Bandage &8&o(x2)",
                        "   &7&oBandage yourself to full health",
                        "   ",
                        "&8- &aNotch's Wisdom &8&o(x2)",
                        "   &7&oCreates an aura of health to",
                        "   &7&oheal survivors for 10 seconds",
                        "",
                        "&8- &bSummon Woofless &8&o(x1)",
                        "   &7&oIt's dangerous to go alone, take a friend!"
                ),
                new GUIItem(Material.BONE).displayName("&fPriest")
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
        GUIItem blade = new GUIItem(Material.STONE_SWORD).displayName("&aBlade of Heroism").unbreakable(true);

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
