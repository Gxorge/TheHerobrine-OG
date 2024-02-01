package uk.hotten.herobrine.kit.kits;

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
                "&6Paladin",
                "theherobrine.kit.unlockable.paladin",
                requirePermission,
                Message.createArray(
                        "&8- &aPaladin's Might &8&o(weapon)",
                        "",
                        "&8- &bProtection &lSpirit! &8&o(x3)",
                        "   &7&oProtect yourself with a spirit of",
                        "   &7&orapid healing for 12 seconds",
                        "",
                        "&8- &aNotch's Wisdom &8&o(x3)",
                        "   &7&oCreates an aura of health to",
                        "   &7&oheal survivors for 10 seconds"
                ),
                new GUIItem(Material.ENDER_PEARL).displayName("&6Paladin")
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
        GUIItem sword = new GUIItem(Material.IRON_SWORD).displayName("&aPaladin's Might").unbreakable(true);

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
