package moe.gabriella.herobrine.game;

import lombok.Getter;
import moe.gabriella.herobrine.events.GameStateUpdateEvent;
import moe.gabriella.herobrine.events.ShardStateUpdateEvent;
import moe.gabriella.herobrine.game.runnables.HerobrineSetup;
import moe.gabriella.herobrine.game.runnables.ShardHandler;
import moe.gabriella.herobrine.game.runnables.SurvivorSetup;
import moe.gabriella.herobrine.game.runnables.WaitingRunnable;
import moe.gabriella.herobrine.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class GameManager {

    @Getter private JavaPlugin plugin;

    @Getter private static GameManager instance;

    @Getter private GameState gameState;
    @Getter private ShardState shardState;

    @Getter private int requiredToStart;
    @Getter private int maxPlayers;
    @Getter private boolean allowOverfill;
    @Getter private int startingDuration;

    @Getter private String networkName;

    @Getter private Player herobrine;
    @Getter private ArrayList<Player> survivors;
    @Getter private Player passUser;

    @Getter private int shardCount;
    @Getter private Player shardCarrier;

    public int startTimer = 90;
    public boolean stAlmost = false;
    public boolean stFull = false;

    public GameManager(JavaPlugin plugin) {
        Console.info("Starting Game Manager...");
        this.plugin = plugin;
        instance = this;
        plugin.getServer().getPluginManager().registerEvents(new GMListener(this), plugin);

        gameState = GameState.BOOTING;
        shardState = ShardState.WAITING;

        requiredToStart = plugin.getConfig().getInt("minPlayers");
        maxPlayers = plugin.getConfig().getInt("maxPlayers");
        allowOverfill = plugin.getConfig().getBoolean("allowOverfill");
        networkName = plugin.getConfig().getString("networkName");

        shardCount = 0;
        survivors = new ArrayList<>();


        startWaiting();
        Console.info("Game Manager is ready!");
    }

    public void setGameState(GameState newState) {
        GameState old = gameState;
        if (old == null) old = GameState.UNKNOWN;

        gameState = newState;
        Console.info("Game state updated to " + newState.toString() + "(from " + old.toString() + ")!");
        plugin.getServer().getPluginManager().callEvent(new GameStateUpdateEvent(old, newState));
    }

    public void setShardState(ShardState newState) {
        ShardState old = shardState;
        if (old == null) old = ShardState.UNKNOWN;

        shardState = newState;
        Console.info("Shard state updated to " + newState.toString() + "(from " + old.toString() + ")!");
        plugin.getServer().getPluginManager().callEvent(new ShardStateUpdateEvent(old, newState));
    }

    public void startWaiting() {
        setGameState(GameState.WAITING);
        new WaitingRunnable().runTaskTimerAsynchronously(plugin, 0, 10);
    }

    public void start() {
        setGameState(GameState.LIVE);
        setShardState(ShardState.WAITING);
        if (passUser != null) {
            herobrine = passUser;
            passUser = null;
        } else {
            herobrine = PlayerUtil.randomPlayer();
        }
        survivors.remove(herobrine);
        //todo items, tp
        new HerobrineSetup().runTaskAsynchronously(plugin);
        for (Player p : survivors) {
            //todo items, tp
            new SurvivorSetup(p).runTaskAsynchronously(plugin);
        }
        new ShardHandler().runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public void end(WinType type) {
        setGameState(GameState.ENDING);
        setShardState(ShardState.INACTIVE);
        if (type == WinType.SURVIVORS) {
            PlayerUtil.broadcastTitle(ChatColor.GREEN + "SURVIVORS WIN!", "", 20, 60, 20);
            Message.broadcast(Message.format("" + ChatColor.GREEN + ChatColor.BOLD + "The Survivors " + ChatColor.YELLOW + " have defeated " + ChatColor.RED + ChatColor.BOLD + "The Herobrine!"));
            Message.broadcast(Message.format(type.getDesc()));
            PlayerUtil.broadcastSound(Sound.ENTITY_WITHER_DEATH, 1f, 1f);
        } else {
            PlayerUtil.broadcastTitle(ChatColor.RED + "HEROBRINE " + ChatColor.GREEN + " WINS!", "", 20, 60, 20);
            Message.broadcast(Message.format("" + ChatColor.RED + ChatColor.BOLD + "The Herobrine " + ChatColor.YELLOW + " has defeated all the survivors"));
            Message.broadcast(Message.format(type.getDesc()));
            PlayerUtil.broadcastSound(Sound.ENTITY_WITHER_HURT, 1f, 1f);
        }
    }

}
