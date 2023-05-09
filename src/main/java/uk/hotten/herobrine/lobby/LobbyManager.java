package uk.hotten.herobrine.lobby;

import com.onarandombox.MultiverseCore.MultiverseCore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.Console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyManager {

    private JavaPlugin plugin;
    @Getter private static LobbyManager instance;

    @Getter private MultiverseCore multiverseCore;

    private String lobbyPrefix;
    private HashMap<String, GameLobby> gameLobbies;

    public LobbyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;

        multiverseCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

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

    public GameLobby getLobby(String lobbyId) {
        return gameLobbies.get(lobbyId);
    }

    public void removeLobby(String lobbyId) {
        gameLobbies.remove(lobbyId);
    }

    public void shutdown() {
        for (Map.Entry<String, GameLobby> entry : gameLobbies.entrySet()) {
            entry.getValue().shutdown();
        }
    }

}
