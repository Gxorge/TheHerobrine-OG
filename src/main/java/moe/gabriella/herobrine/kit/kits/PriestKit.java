package moe.gabriella.herobrine.kit.kits;

import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.kit.Kit;
import moe.gabriella.herobrine.kit.KitAbility;
import moe.gabriella.herobrine.kit.abilities.DreamweaverAbility;
import moe.gabriella.herobrine.kit.abilities.LocatorAbility;
import moe.gabriella.herobrine.kit.abilities.WisdomAbility;
import moe.gabriella.herobrine.kit.abilities.WooflessAbility;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class PriestKit extends Kit {

    public PriestKit(GameManager gm) {
        super(gm,
                "priest",
                ChatColor.WHITE + "Priest",
                null,
                "",
                new GUIItem(Material.BONE).displayName(ChatColor.WHITE + "Priest"),
                new KitAbility[]{
                        new LocatorAbility(gm),
                        new DreamweaverAbility(gm, 1, 2),
                        new WisdomAbility(gm, 2),
                        new WooflessAbility(gm, 3)
                }
        );
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
