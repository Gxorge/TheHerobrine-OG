package uk.hotten.herobrine.world;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import uk.hotten.herobrine.events.GameStateUpdateEvent;
import uk.hotten.herobrine.game.runnables.MapVotingRunnable;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.Console;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.world.data.Datapoint;
import uk.hotten.herobrine.world.data.VotingMap;
import uk.hotten.herobrine.world.data.MapBase;
import uk.hotten.herobrine.world.data.MapData;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class WorldManager implements Listener {

    private JavaPlugin plugin;
    private GameLobby gameLobby;
    @Getter private MVWorldManager mvWorldManager;

    private String fileBase;
    @Getter private MapBase availableMaps;
    @Getter private MapData gameMapData;
    @Getter private MultiverseWorld gameWorld;
    @Getter private MultiverseWorld hubWorld;

    @Getter private HashMap<Integer, VotingMap> votingMaps;
    @Getter private HashMap<Player, Integer> playerVotes;
    private int maxVotingMaps;
    @Getter private int endVotingAt;
    @Getter private boolean votingRunning = false;

    public Location herobrineSpawn;
    public Location survivorSpawn;
    public Location alter;
    public ArrayList<Location> shardSpawns;

    private ArrayList<Chunk> noUnload;


    public WorldManager(JavaPlugin plugin, GameLobby gameLobby) {
        Console.info(gameLobby, "Loading World Manager...");
        this.plugin = plugin;
        this.gameLobby = gameLobby;
        mvWorldManager = LobbyManager.getInstance().getMultiverseCore().getMVWorldManager();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        fileBase = plugin.getConfig().getString("mapBase");
        maxVotingMaps = plugin.getConfig().getInt("votingMaps");
        endVotingAt = plugin.getConfig().getInt("endVotingAt");

        votingMaps = new HashMap<>();
        playerVotes = new HashMap<>();

        shardSpawns = new ArrayList<>();

        noUnload = new ArrayList<>();

        loadHubMap();
        loadMapBase();
        Console.info(gameLobby, "World manager is ready!");
    }

    public void loadMapBase() {
        File file = new File(fileBase + "/maps.yaml");
        if (!file.exists()) {
            Console.error(gameLobby, "No maps.yaml found at " + file.getPath() + "!");
            plugin.getServer().shutdown();
            return;
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            availableMaps = mapper.readValue(file, MapBase.class);
        } catch (Exception e) {
            e.printStackTrace();
            Console.error(gameLobby, "Error parsing maps.yaml! Is it correctly formatted?");
            plugin.getServer().shutdown();
            return;
        }
        Console.info(gameLobby, "Map base loaded!");
        pickVotingMaps(false);
    }

    public void pickVotingMaps(boolean startRunnable) {
        List<String> maps = availableMaps.getMaps();
        int reps = 0;
        while (reps < maxVotingMaps) {
            Random rand = new Random();
            String map = maps.get(rand.nextInt(maps.size()));
            Console.debug(gameLobby, "Map -> " + map);

            // Parse the map
            File file = new File(fileBase + "/" + map + "/mapdata.yaml");
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            MapData mapData;
            try {
                mapData = mapper.readValue(file, MapData.class);
                Console.debug(gameLobby, "Parsed map data id " + (reps+1));
            } catch (Exception e) {
                e.printStackTrace();
                Console.error(gameLobby, "Error parsing mapdata.yaml! Is it correctly formatted?");
                return;
            }

            votingMaps.put(reps+1, new VotingMap((reps+1), mapData, map));
            maps.remove(map);

            reps++;
        }
        votingRunning = true;
        if (startRunnable)
            new MapVotingRunnable(gameLobby).runTaskTimerAsynchronously(plugin, 0, 20);
        Console.info(gameLobby, "Picked voting maps!");
    }

    public void sendVotingMessage(Player player) {
        ArrayList<Player> toSend = new ArrayList<>();
        if (player == null)
            toSend.addAll(gameLobby.getPlayers());
        else
            toSend.add(player);

        for (Player p : toSend) {
            p.sendMessage(Message.format(ChatColor.GOLD + "Vote for a map with /hbv #."));
            p.sendMessage(Message.format(ChatColor.GOLD + "Map choices up for voting:"));
            int current = 1;
            for (Map.Entry<Integer, VotingMap> e : votingMaps.entrySet()) {
                TextComponent textComponent = new TextComponent(Message.format("" + ChatColor.GOLD + ChatColor.BOLD + current + ". "
                        + ChatColor.GOLD + e.getValue().getMapData().getName() + " (" + ChatColor.AQUA + e.getValue().getVotes() + ChatColor.GOLD + " votes)"));
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Click here to vote for " + ChatColor.AQUA + e.getValue().getMapData().getName())));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hbv " + current));
                p.spigot().sendMessage(textComponent);
                current++;
            }
            p.sendMessage(" ");
        }
    }

    public void selectAndLoadMapFromVote() {
        VotingMap highest = null;
        int highestInt = -1;
        for (Map.Entry<Integer, VotingMap> e : votingMaps.entrySet()) {
            if (e.getValue().getVotes() > highestInt) {
                highest = e.getValue();
                highestInt = e.getValue().getVotes();
            }
        }

        Console.debug(gameLobby, "Selected highest voted map -> " + highest.getMapData().getName());
        Message.broadcast(gameLobby, Message.format(ChatColor.GOLD + "Voting has ended! The map " + ChatColor.AQUA + highest.getMapData().getName() + ChatColor.GOLD + " has won!"));
        votingRunning = false;

        loadMap(highest);
    }

    public void loadMap(VotingMap map) {
        Console.info(gameLobby, "Loading map " + map.getMapData().getName());
        File currentDir;
        File toCopy;

        toCopy = new File(fileBase + "/" + map.getInternalName());
        currentDir = new File(gameLobby.getLobbyId() + "-" + map.getInternalName());
        currentDir.mkdir();
        try {
            FileUtils.copyDirectory(toCopy, currentDir);
        } catch (Exception e) {
            e.printStackTrace();
            Console.error(gameLobby, "Error copying directory!");
        }

        mvWorldManager.addWorld(
                gameLobby.getLobbyId() + "-" + map.getInternalName(),
                World.Environment.NORMAL,
                null,
                WorldType.NORMAL,
                false,
                null
        );
        gameWorld = mvWorldManager.getMVWorld(gameLobby.getLobbyId() + "-" + map.getInternalName());

        gameWorld.setAllowAnimalSpawn(false);
        gameWorld.setAllowMonsterSpawn(false);
        gameWorld.setDifficulty(Difficulty.NORMAL);
        gameWorld.setTime("midnight");
        gameWorld.getCBWorld().setGameRule(GameRule.DO_FIRE_TICK, false);
        gameWorld.getCBWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        gameWorld.getCBWorld().setGameRule(GameRule.SHOW_DEATH_MESSAGES, false); // Hides wolf death messages

        // Load data points
        gameMapData = map.getMapData();

        for (Datapoint dp : gameMapData.getDatapoints()) {
            Location dLoc = new Location(gameWorld.getCBWorld(), dp.getX(), dp.getY(), dp.getZ());
            switch (dp.getType()) {
                case SURVIVOR_SPAWN:
                    survivorSpawn = dLoc;
                    dLoc.getChunk().load(true);
                    //dLoc.getChunk().setForceLoaded(true);
                    noUnload.add(dLoc.getChunk());
                    break;
                case HEROBRINE_SPAWN:
                    herobrineSpawn = dLoc;
                    dLoc.getChunk().load(true);
                    //dLoc.getChunk().setForceLoaded(true);
                    noUnload.add(dLoc.getChunk());
                    break;
                case ALTER:
                    alter = dLoc;
                    break;
                case SHARD_SPAWN:
                    shardSpawns.add(dLoc);
                    break;
                default:
                    break;
            }
        }
        Console.info(gameLobby, "Finished loading!");
    }

    public void loadHubMap() {
        Console.info(gameLobby, "Loading hub for " + gameLobby.getLobbyId());
        File currentDir;
        File toCopy;

        toCopy = new File(fileBase + "/hub");
        currentDir = new File(gameLobby.getLobbyId() + "-hub");
        currentDir.mkdir();
        try {
            FileUtils.copyDirectory(toCopy, currentDir);
        } catch (Exception e) {
            e.printStackTrace();
            Console.error(gameLobby, "Error copying directory!");
        }

        mvWorldManager.addWorld(
                gameLobby.getLobbyId() + "-hub",
                World.Environment.NORMAL,
                null,
                WorldType.NORMAL,
                false,
                null
        );
        hubWorld = mvWorldManager.getMVWorld(gameLobby.getLobbyId() + "-hub");

        hubWorld.setAllowAnimalSpawn(false);
        hubWorld.setAllowMonsterSpawn(false);
        hubWorld.setDifficulty(Difficulty.PEACEFUL);
        hubWorld.setTime("midnight");
        Console.info(gameLobby, "Finished loading!");
    }

    @EventHandler
    public void gameStart(GameStateUpdateEvent event) {
        if (!gameLobby.getLobbyId().equals(event.getLobbyId()))
            return;

        if (event.getNewState() == GameState.LIVE)
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (Chunk c : noUnload)
                    c.setForceLoaded(false);
                noUnload.clear();
            }, 100);
    }

    public void clean() { clean(true); }

    public void clean(boolean clearVotes) {
        if (gameWorld == null)
            return;

        Console.info(gameLobby, "Cleaning the map...");
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getWorld() == gameWorld) {
                p.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
            }
        }

        mvWorldManager.deleteWorld(gameWorld.getName());

        gameWorld = null;
        gameMapData = null;
        survivorSpawn = null;
        herobrineSpawn = null;
        alter = null;
        shardSpawns = new ArrayList<>();
        noUnload = new ArrayList<>();
        if (clearVotes) {
            votingMaps = new HashMap<>();
            playerVotes = new HashMap<>();
        } else {
            if (gameLobby.getGameManager().getGameState() == GameState.WAITING) {
                votingRunning = true;
                new MapVotingRunnable(gameLobby).runTaskTimerAsynchronously(plugin, 0, 20);
            }
        }
        Console.info(gameLobby, "Cleaned!");
    }


    public void cleanHub() {
        if (hubWorld == null)
            return;

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getWorld() == gameWorld) {
                p.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
            }
        }

        mvWorldManager.deleteWorld(hubWorld.getName());
    }
}
