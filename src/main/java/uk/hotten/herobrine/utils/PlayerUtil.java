package uk.hotten.herobrine.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PlayerUtil {

    public static void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public static void broadcastActionbar(String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            sendActionbar(p, message);
        }
    }

    public static void sendTitle(Player player, String top, String bottom, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(top, bottom, fadeIn, stay, fadeOut);
    }

    public static void broadcastTitle(String top, String bottom, int fadeIn, int stay, int fadeOut) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.sendTitle(top, bottom, fadeIn, stay, fadeOut);
        }
    }

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void broadcastSound(Sound sound, float volume, float pitch) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            playSound(p, sound, volume, pitch);
        }
    }

    public static void playSoundAt(Location loc, Sound sound, float volume, float pitch) {
        loc.getWorld().playSound(loc, sound, volume, pitch);
    }

    public static void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public static void clearEffects(Player player) {
        for (PotionEffect e : player.getActivePotionEffects()) {
            player.removePotionEffect(e.getType());
        }
    }

    public static void addEffect(Player player, PotionEffectType type, int duration, int amplifier, boolean particles, boolean icon) {
        PotionEffect effect = new PotionEffect(type, duration, amplifier, true, particles, icon);
        player.addPotionEffect(effect);
    }

    public static Player randomPlayer() {

        Random random = new Random();
        Player player = (Player) Bukkit.getOnlinePlayers().toArray()
                [random.nextInt(Bukkit.getOnlinePlayers().size())];

        return player;
    }

    public static double getDistance(Player player, Location location) {
        return player.getLocation().distance(location);
    }

    public static void removeAmountOfItem(Player player, ItemStack item, int amount) {
        if (item.getAmount() - amount <= 0) player.getInventory().remove(item);
        else item.setAmount(item.getAmount() - amount);
    }

    public static void increaseHealth(Player player, double hearts) {
        double newHealth = player.getHealth() + hearts;
        player.setHealth((newHealth > 20 ? 20 : newHealth));
    }

}
