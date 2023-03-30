package uk.hotten.herobrine.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Message {

    public static String format(String body) {
        return "" + ChatColor.DARK_GRAY + "▍ " + ChatColor.DARK_AQUA + "TheHerobrine " + ChatColor.DARK_GRAY + "▏ " + ChatColor.RESET + body;
    }

    public static void broadcast(String message) {
        Bukkit.getServer().broadcastMessage(message);
    }


    public static String formatTime(int seconds) {
        if (seconds < 60)
            return "" + seconds + " second" + (seconds != 1 ? "s" : "");
        return formatTimeFull(seconds);
    }

    public static String formatTimeFull(int seconds) {
        int s = seconds % 60;
        int h = seconds / 60;
        int m = h % 60;
        h = h / 60;
        if (h == 0)
            return (m < 10 ? "0" + m : "" + m) + ":" + (s < 10 ? "0" + s : "" + s);
        else
            return (h < 10 ? "0" + h : "" + h) + ":" + (m < 10 ? "0" + m : "" + m) + ":" + (s < 10 ? "0" + s : "" + s);
    }

}
