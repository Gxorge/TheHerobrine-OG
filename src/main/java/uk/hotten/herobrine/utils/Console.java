package uk.hotten.herobrine.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Console {

    public static boolean showDebug = false;

    public static void info(String message) { Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.WHITE + "[The Herobrine] " + message); }
    public static void error(String message) { Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "[The Herobrine: ERROR] " + message); }
    public static void debug(String message) { if (!showDebug) return; Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.YELLOW + "[The Herobrine: DEBUG] " + message); }

}
