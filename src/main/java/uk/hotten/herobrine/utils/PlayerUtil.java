package uk.hotten.herobrine.utils;

import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import uk.hotten.herobrine.lobby.GameLobby;

import java.time.Duration;
import java.util.Random;

public class PlayerUtil {

    public static void sendActionbar(Player player, String message) {
        player.sendActionBar(Message.legacySerializerAnyCase(message));
    }

    public static void broadcastActionbar(GameLobby gameLobby, String message) {
        for (Player p : gameLobby.getPlayers()) {
            sendActionbar(p, message);
        }
    }

    public static void sendTitle(Player player, String top, String bottom, int fadeIn, int stay, int fadeOut) {
        Title.Times times = Title.Times.times(Duration.ofSeconds(fadeIn/20), Duration.ofSeconds(stay/20), Duration.ofSeconds(fadeOut/20));
        Title title = Title.title(Message.legacySerializerAnyCase(top), Message.legacySerializerAnyCase(bottom), times);
        player.showTitle(title);
    }

    public static void broadcastTitle(GameLobby gameLobby, String top, String bottom, int fadeIn, int stay, int fadeOut) {
        for (Player p : gameLobby.getPlayers()) {
            sendTitle(p, top, bottom, fadeIn, stay, fadeOut);
        }
    }

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void broadcastSound(GameLobby gameLobby, Sound sound, float volume, float pitch) {
        for (Player p : gameLobby.getPlayers()) {
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

    public static Player randomPlayer(GameLobby gameLobby) {

        Random random = new Random();
        Player player = (Player) gameLobby.getGameManager().getSurvivors().toArray()
                [random.nextInt(gameLobby.getGameManager().getSurvivors().size())];

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

    public static void animateHbHit(GameLobby gameLobby, Location loc) {
        PlayerUtil.playSoundAt(loc, Sound.ENTITY_BLAZE_HURT, 1f, 1f);
        PlayerUtil.playSoundAt(loc, Sound.ENTITY_IRON_GOLEM_ATTACK, 1f, 1f);

        for (Player p : gameLobby.getPlayers()) {
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
