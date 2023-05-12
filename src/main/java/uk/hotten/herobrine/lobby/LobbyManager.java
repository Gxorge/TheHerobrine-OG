package uk.hotten.herobrine.lobby;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.onarandombox.MultiverseCore.MultiverseCore;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.lobby.data.LobbyConfig;
import uk.hotten.herobrine.utils.Console;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LobbyManager {

    private JavaPlugin plugin;
    @Getter private static LobbyManager instance;

    @Getter private MultiverseCore multiverseCore;

    private HashMap<String, LobbyConfig> lobbyConfigs;
    private HashMap<String, GameLobby> gameLobbies;

    public LobbyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;

        multiverseCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

        lobbyConfigs = new HashMap<>();
        gameLobbies = new HashMap<>();

        try {
            checkAndLoadConfigs(true);
        } catch (Exception e) {
            Console.error("Failed to load config files.");
            e.printStackTrace();
        }
    }

    public void checkAndLoadConfigs(boolean autoStart) throws Exception {
        Console.info("Loading lobby configs...");
        lobbyConfigs.clear();
        Path path = Path.of(plugin.getDataFolder() + File.separator + "lobbies");
        if (!Files.exists(path)) {
            Console.error("No lobbies file found, creating...");
            new File(path.toUri()).mkdir();
        }

        Collection<File> files = FileUtils.listFiles(path.toFile(), new RegexFileFilter("\\w+\\.yaml$"), DirectoryFileFilter.DIRECTORY);
        if (files.isEmpty()) {
            Console.error("No lobby config files detected. Creating the default...");

            LobbyConfig defaultConfig = new LobbyConfig("default", "HB", 8, 13, 90, false, 3, 10, 1);

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

            objectMapper.writeValue(new File(path + File.separator + "default.yaml"), defaultConfig);

            Console.info("Default config file written. Please change!!");

            files.add(new File(path + File.separator + "default.yaml"));
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        files.forEach(file -> {
            try {
                Console.info("Loading lobby config " + file.getName());
                LobbyConfig lobbyConfig = mapper.readValue(file, LobbyConfig.class);

                lobbyConfigs.put(lobbyConfig.getId(), lobbyConfig);

                if (autoStart) {
                    for (int i = 0; i < lobbyConfig.getAutoStartAmount(); i++) {
                        createLobby(lobbyConfig);
                    }
                }

                Console.info("Successfully loaded configuration ID " + lobbyConfig.getId());
            } catch (IOException e) {
                Console.error("Failed to load config file " + file.getName());
                throw new RuntimeException(e);
            }
        });
    }

    public String createLobby(LobbyConfig lobbyConfig) {
        String lobbyId = generateLobbyId(lobbyConfig);
        if (lobbyId == null) {
            Console.error("Failed to generate lobby ID.");
            return null;
        }

        GameLobby gl = new GameLobby(plugin, lobbyConfig, lobbyId);
        gl.initialize();
        gameLobbies.put(lobbyId, gl);
        return lobbyId;
    }

    private String generateLobbyId(LobbyConfig lobbyConfig) {
        for (int i = 1; i <= 100; i++) {
            if (gameLobbies.containsKey(lobbyConfig.getPrefix() + i))
                continue;

            return lobbyConfig.getPrefix() + i;
        }

        return null;
    }

    public GameLobby getLobby(String lobbyId) {
        return gameLobbies.getOrDefault(lobbyId, null);
    }

    public GameLobby getLobby(Player player) {
        for (Map.Entry<String, GameLobby> entry : gameLobbies.entrySet()) {
            if (entry.getValue().getPlayers().contains(player))
                return entry.getValue();
        }

        return null;
    }

    public List<String> getLobbyIds() {
        return gameLobbies.keySet().stream().toList();
    }

    public void removeLobby(String lobbyId) {
        gameLobbies.remove(lobbyId);
    }

    public LobbyConfig getLobbyConfig(String configId) {
        return lobbyConfigs.getOrDefault(configId, null);
    }

    public List<String> getLobbyConfigsIds() {
        return lobbyConfigs.keySet().stream().toList();
    }

    public void shutdown() {
        for (Map.Entry<String, GameLobby> entry : gameLobbies.entrySet()) {
            entry.getValue().shutdown(false, false);
        }
        gameLobbies.clear();
    }

    public void sendLobbyMessage(Player player) {
        player.sendMessage(Message.format(ChatColor.GOLD + "Join a lobby with /hbjoin <id>."));
        player.sendMessage(Message.format(ChatColor.GOLD + "Lobbies available to join: "));

        String fullOrOverfill = "" + ChatColor.RED + ChatColor.BOLD + "FULL " + ChatColor.RESET +
                (player.hasPermission("theherobrine.overfill") ? ChatColor.GREEN + "(JOIN)"
                        : "");

        int sent = 0;
        for (Map.Entry<String, GameLobby> entry : gameLobbies.entrySet()) {
            GameLobby gameLobby = entry.getValue();
            GameManager gm = gameLobby.getGameManager();

            if (gm.getGameState() == GameState.LIVE) {
                StringBuilder sb = new StringBuilder("" + ChatColor.AQUA + gameLobby.getLobbyId() + ": " +
                        ChatColor.YELLOW + gm.getSurvivors().size() + " remaining" + ChatColor.DARK_GRAY + " - " + ChatColor.AQUA + ChatColor.BOLD + "LIVE " + ChatColor.DARK_GREEN + "(SPECTATE)");

                TextComponent textComponent = new TextComponent(Message.format(sb.toString()));
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Click here to spectate " + ChatColor.AQUA + gameLobby.getLobbyId())));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hbjoin " + gameLobby.getLobbyId()));

                player.spigot().sendMessage(textComponent);
                sent++;
                continue;
            }

            if (gm.getGameState() != GameState.WAITING && gm.getGameState() != GameState.STARTING)
                continue;

            String startingStatus = (gm.getGameState() == GameState.WAITING ? "" + ChatColor.YELLOW + ChatColor.BOLD + "WAITING "
                    : "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "STARTING ") + ChatColor.RESET;

            StringBuilder sb = new StringBuilder("" + ChatColor.AQUA + gameLobby.getLobbyId() + ": " +
                    ChatColor.YELLOW + gm.getSurvivors().size() + "/" + gm.getMaxPlayers() + ChatColor.DARK_GRAY + " - " + ChatColor.RESET);

            if (gameLobby.getPlayers().size() < gm.getMaxPlayers())
                sb.append(startingStatus + ChatColor.GREEN + "(JOIN)");
            else
                sb.append(fullOrOverfill);

            TextComponent textComponent = new TextComponent(Message.format(sb.toString()));
            if (sb.toString().contains("JOIN")) {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Click here to join " + ChatColor.AQUA + gameLobby.getLobbyId())));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hbjoin " + gameLobby.getLobbyId()));
            } else {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "This lobby is full.")));
            }

            player.spigot().sendMessage(textComponent);
            sent++;
        }

        if (sent == 0) {
            player.sendMessage(Message.format(ChatColor.RED + "There are no lobbies available."));
        }
    }

}
