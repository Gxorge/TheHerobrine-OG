package uk.hotten.herobrine.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Message {

    public static String format(String body) {
        return "" + ChatColor.DARK_GRAY + "▍ " + ChatColor.DARK_AQUA + "TheHerobrine " + ChatColor.DARK_GRAY + "▏ " + ChatColor.RESET + body;
    }

    public static void broadcast(GameLobby lobby, String message) {
        for (Player p : lobby.getPlayers()) {
            p.sendMessage(message);
        }
    }

    public static void broadcast(GameLobby lobby, String message, String permission) {
        for (Player p : lobby.getPlayers()) {
            if (p.hasPermission(permission)) {
                p.sendMessage(message);
            }
        }
    }

    public static void broadcast(String lobbyId, String message) {
        broadcast(LobbyManager.getInstance().getLobby(lobbyId), message);
    }

    public static void broadcast(String lobbyId, String message, String permission) {
        broadcast(LobbyManager.getInstance().getLobby(lobbyId), message, permission);
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

    public static ArrayList<String> createArray(String... lines) {
        return new ArrayList<>(Arrays.asList(lines));
    }

    public static ArrayList<String> addLinebreaks(String input, String toAppendAfterNewline) {
        return addLinebreaks(input, 20, toAppendAfterNewline);
    }

    public static ArrayList<String> addLinebreaks(String input, int maxLineLength, String toAppendAfterNewline) {
        ArrayList<String> result = new ArrayList<>();

        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder();
        output.insert(0, toAppendAfterNewline);
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            if (lineLen + word.length() > maxLineLength) {
                result.add(output.toString());
                output = new StringBuilder();
                output.append(toAppendAfterNewline);
                lineLen = 0;
            }
            output.append(word).append(" ");
            lineLen += word.length();
        }
        result.add(output.toString());

        return result;
    }

}
