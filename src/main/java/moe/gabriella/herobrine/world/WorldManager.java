package moe.gabriella.herobrine.world;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import moe.gabriella.herobrine.utils.Console;
import moe.gabriella.herobrine.utils.Message;
import moe.gabriella.herobrine.world.data.Datapoint;
import moe.gabriella.herobrine.world.data.MapBase;
import moe.gabriella.herobrine.world.data.MapData;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class WorldManager {

    private JavaPlugin plugin;
    @Getter private static WorldManager instance;

    private String fileBase;
    @Getter private MapBase availableMaps;
    @Getter private MapData gameMapData;
    @Getter private World gameWorld;

    public HashMap<String, Integer> votingMaps;
    private int maxVotingMaps;

    public Location herobrineSpawn;
    public Location survivorSpawn;
    public Location alter;
    public ArrayList<Location> shardSpawns;


    public WorldManager(JavaPlugin plugin) {
        this.plugin = plugin;
        fileBase = plugin.getConfig().getString("mapBase");
        maxVotingMaps = plugin.getConfig().getInt("votingMaps");

        votingMaps = new HashMap<>();

        shardSpawns = new ArrayList<>();

        loadMapBase();
    }

    public void loadMapBase() {
        File file = new File(fileBase + "/maps.yaml");
        if (!file.exists()) {
            Console.error("No maps.yaml found at " + file.getPath() + "!");
            plugin.getServer().shutdown();
            return;
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            availableMaps = mapper.readValue(file, MapBase.class);
        } catch (Exception e) {
            e.printStackTrace();
            Console.error("Error parsing maps.yaml! Is it correctly formatted?");
            plugin.getServer().shutdown();
        }
    }

    public void pickVotingMaps() {
        List<String> maps = availableMaps.getMaps();
        int reps = 0;
        while (reps <= maxVotingMaps) {
            Random rand = new Random();
            String map = maps.get(rand.nextInt(maps.size()));
            votingMaps.put(map, 0);
            maps.remove(map);
        }
    }

    public void selectAndLoadMapFromVote() {
        String highest = "";
        int highestInt = -1;
        for (Map.Entry<String, Integer> e : votingMaps.entrySet()) {
            if (e.getValue() > highestInt) {
                highest = e.getKey();
                highestInt = e.getValue();
            }
        }

        Message.broadcast(Message.format(ChatColor.GOLD + "Voting has ended! The map " + ChatColor.AQUA + highest + ChatColor.GOLD + " has won!"));

        loadMap(highest);
    }

    public void loadMap(String map) {
        File currentDir;
        File toCopy;

        toCopy = new File(fileBase + "/" + map);
        currentDir = new File(map);
        currentDir.mkdir();
        try {
            FileUtils.copyDirectory(toCopy, currentDir);
        } catch (Exception e) {
            e.printStackTrace();
            Console.error("Error copying directory!");
        }

        gameWorld = Bukkit.getServer().createWorld(new WorldCreator(map));

        // Load data points
        File file = new File(map + "/mapdata.yaml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            gameMapData = mapper.readValue(file, MapData.class);
        } catch (Exception e) {
            e.printStackTrace();
            Console.error("Error parsing mapdata.yaml! Is it correctly formatted?");
            return;
        }

        for (Datapoint dp : gameMapData.getDatapoints()) {
            Location dLoc = new Location(gameWorld, dp.getX(), dp.getY(), dp.getZ());
            switch (dp.getType()) {
                case SURVIVOR_SPAWN:
                    survivorSpawn = dLoc;
                    break;
                case HEROBRINE_SPAWN:
                    herobrineSpawn = dLoc;
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
    }

    public void clean() {
        if (gameWorld != null) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.getWorld() == gameWorld) {
                    p.teleport(Bukkit.getServer().getWorld("world").getSpawnLocation());
                }
            }

            Bukkit.getServer().unloadWorld(gameWorld, false);
            try {
                FileUtils.deleteDirectory(new File(gameWorld.getName()));
            } catch (Exception e) {
                e.printStackTrace();
                Console.error("Error deleting world folder. Please do it manually.");
            }

            gameWorld = null;
            gameMapData = null;
            votingMaps = new HashMap<>();
            survivorSpawn = null;
            herobrineSpawn = null;
            alter = null;
            shardSpawns = null;
        }
    }
}
