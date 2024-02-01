package uk.hotten.herobrine.kit.kits;

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
                "&cSorcerer",
                "theherobrine.kit.unlockable.sorcerer",
                requirePermission,
                Message.createArray(
                        "&8- &aAxe of Death &8&o(weapon)",
                        "",
                        "&8- &bSummon Woofless &8&o(x1)",
                        "   &7&oIt's dangerous to go alone, take a friend!",
                        "",
                        "&8- &e&lTotem: &aHealing &8&o(x1)",
                        "   &7&oCreates an aura of health to heal",
                        "   &7&osurvivors for 60 seconds",
                        "",
                        "&8- &e&lTotem: &cPain &8&o(x1)",
                        "   &7&oCreates an aura of pain to damage",
                        "   &7&oThe Herobrine for 60 seconds"
                ),
                new GUIItem(Material.GOLDEN_BOOTS).displayName("&cSorcerer")
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
        GUIItem axe = new GUIItem(Material.IRON_AXE).displayName("&aAxe of Death").unbreakable(true);

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
