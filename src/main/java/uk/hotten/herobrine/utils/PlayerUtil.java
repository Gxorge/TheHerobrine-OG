package uk.hotten.herobrine.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
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
            p.sendTitle((top.equals("") ? ChatColor.RESET + "" : top), bottom, fadeIn, stay, fadeOut);
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

    public static void increaseHealth(Player player, double hp) {
        double newHealth = player.getHealth() + hp;
        player.setHealth((newHealth > 20 ? 20 : newHealth));
    }

    public static void decreaseHealth(Player player, double hp, Player damager) {
        double newHealth = player.getHealth() - hp;
        if (newHealth <= 0) {
            if (damager != null)
                player.damage(Integer.MAX_VALUE, damager);
            else
                player.damage(Integer.MAX_VALUE);
        } else {
            player.setHealth(newHealth);
        }
    }

    public static void animateHbHit(Location loc) {
        PlayerUtil.playSoundAt(loc, Sound.ENTITY_BLAZE_HURT, 1f, 1f);
        PlayerUtil.playSoundAt(loc, Sound.ENTITY_IRON_GOLEM_ATTACK, 1f, 1f);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.spawnParticle(Particle.BLOCK_DUST, loc.add(0, 0.75, 0), 25, Material.ORANGE_WOOL.createBlockData());
        }
    }

    public static void spawnFirework(Location location, Color colour) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(colour).flicker(true).build());

        fw.setFireworkMeta(fwm);
        //fw.detonate();
    }

}
