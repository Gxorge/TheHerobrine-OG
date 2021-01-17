package moe.gabriella.herobrine.kit.kits;

import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.kit.Kit;
import moe.gabriella.herobrine.kit.KitAbility;
import moe.gabriella.herobrine.kit.abilities.LocatorAbility;
import moe.gabriella.herobrine.utils.PlayerUtil;
import net.minecraft.server.v1_16_R3.PacketListener;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

public class ScoutKit extends Kit {

    public ScoutKit(GameManager gm) {
        super(gm,
                "scout",
                ChatColor.YELLOW + "Scout",
                null,
                "",
                new GUIItem(Material.FEATHER).displayName(ChatColor.YELLOW + "Scout"),
                new KitAbility[] {
                        new LocatorAbility(gm)
                }
        );
    }

    @Override
    public void setupPlayer(Player player) {
        // Effects
        PlayerUtil.addEffect(player, PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false);

        // Items
        GUIItem blade = new GUIItem(Material.STONE_SWORD).displayName(ChatColor.GREEN + "Blade of Heroism");
        GUIItem bow = new GUIItem(Material.BOW).displayName(ChatColor.GRAY + "Handcrafted Bow");
        GUIItem arrow = new GUIItem(Material.ARROW).displayName(ChatColor.GRAY + "Owl Arrows");

        player.getInventory().setItem(0, blade.build());
        player.getInventory().setItem(1, bow.build());
        player.getInventory().setItem(2, arrow.build());

        // Armour
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helMeta.setColor(Color.YELLOW);
        helmet.setItemMeta(helMeta);
        player.getInventory().setHelmet(helmet);

        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
    }
}
