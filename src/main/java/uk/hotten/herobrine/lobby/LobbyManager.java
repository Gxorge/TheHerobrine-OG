package uk.hotten.herobrine.lobby;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.herobrine.utils.Console;

import java.util.HashMap;
import java.util.Map;

public class LobbyManager {

    private JavaPlugin plugin;
    @Getter private static LobbyManager instance;

    private String lobbyPrefix;
    private HashMap<String, GameLobby> gameLobbies;

    public LobbyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;

        lobbyPrefix = plugin.getConfig().getString("lobbyPrefix");
        gameLobbies = new HashMap<>();

        int autoStartup = plugin.getConfig().getInt("lobbyAutoStartAmount");
        for (int i = 0; i < autoStartup; i++) {
            String lobbyId = generateLobbyId();
            if (lobbyId == null) {
                Console.error("Failed to generate lobby ID.");
                return;
            }

            GameLobby gl = new GameLobby(plugin, lobbyId);
            gl.initialize();
            gameLobbies.put(lobbyId, gl);
        }
    }

    private String generateLobbyId() {
        for (int i = 1; i <= 100; i++) {
            if (gameLobbies.containsKey(lobbyPrefix + i))
                continue;

            return lobbyPrefix + i;
        }

        return null;
    }

    public void shutdown() {
        for (Map.Entry<String, GameLobby> entry : gameLobbies.entrySet()) {
            entry.getValue().getWorldManager().clean();
        }
    }

}
