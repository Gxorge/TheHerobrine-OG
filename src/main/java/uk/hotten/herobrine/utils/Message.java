package uk.hotten.herobrine.utils;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Message {

    public static String prefix;

    public static String format(String body) {
        return prefix + " &r" + body;
    }

    public static void broadcast(GameLobby lobby, String message) {
        for (Player p : lobby.getPlayers()) {
            send(p, message);
        }
    }

    public static void broadcast(GameLobby lobby, String message, String permission) {
        for (Player p : lobby.getPlayers()) {
            if (p.hasPermission(permission)) {
                send(p, message);
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

    // Sends a formatted message to the player (including name replacement).
    public static void send(CommandSender p, String message) {

        p.sendMessage(legacySerializerAnyCase(message).asComponent());

    }

    public static TextComponent legacySerializerAnyCase(String subject) {

        int count = 0;
        // Count the number of '&' characters to determine the size of the array
        for (char c : subject.toCharArray()) {

            if (c == '&') {

                count++;

            }

        }

        // Create an array to store the positions of '&' characters
        int[] positions = new int[count];
        int index = 0;
        // Find the positions of '&' characters and store in the array
        for (int i = 0; i < subject.length(); i++) {

            if (subject.charAt(i) == '&') {

                if (isUpperBukkitCode(subject.charAt(i + 1))) {

                    subject = replaceCharAtIndex(subject, (i + 1), Character.toLowerCase(subject.charAt(i + 1)));

                }

                positions[index++] = i;

            }

        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(subject);

    }

    private static boolean isUpperBukkitCode(char input) {

        char[] bukkitColorCodes = {'A', 'B', 'C', 'D', 'E', 'F', 'K', 'L', 'M', 'N', 'O', 'R'};
        boolean match = false;

        // Loop through each character in the array.
        for (char c : bukkitColorCodes) {
            // Check if the current character in the array is equal to the input character.
            if (c == input) {

                match = true;

            }

        }

        return match;

    }

    private static String replaceCharAtIndex(String original, int index, char newChar) {

        // Check if the index is valid
        if (index >= 0 && index < original.length()) {

            // Create a new string with the replaced character
            return original.substring(0, index) + newChar + original.substring(index + 1);

        }

        // If the index is invalid, return the original string
        return original;

    }

}
