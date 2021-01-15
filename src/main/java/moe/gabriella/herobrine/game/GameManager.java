package moe.gabriella.herobrine.game;

import com.comphenix.protocol.events.PacketAdapter;
import lombok.Getter;
import lombok.Setter;
import moe.gabriella.herobrine.events.GameStateUpdateEvent;
import moe.gabriella.herobrine.events.ShardStateUpdateEvent;
import moe.gabriella.herobrine.game.runnables.*;
import moe.gabriella.herobrine.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

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

    @Getter public int shardCount;
    @Getter @Setter private Player shardCarrier;

    public int startTimer = 10; //todo set to 90
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
        new BukkitRunnable() {
            @Override
            public void run() {
                GameState old = gameState;
                if (old == null) old = GameState.UNKNOWN;

                gameState = newState;
                Console.info("Game state updated to " + newState.toString() + "(from " + old.toString() + ")!");
                plugin.getServer().getPluginManager().callEvent(new GameStateUpdateEvent(old, newState));
            }
        }.runTask(plugin);
    }

    public void setShardState(ShardState newState) {
        new BukkitRunnable() {

            @Override
            public void run() {
                ShardState old = shardState;
                if (old == null) old = ShardState.UNKNOWN;

                shardState = newState;
                Console.info("Shard state updated to " + newState.toString() + "(from " + old.toString() + ")!");
                plugin.getServer().getPluginManager().callEvent(new ShardStateUpdateEvent(old, newState));
                if (gameState == GameState.LIVE)
                    NarrationRunnable.timer = 0;
            }
        }.runTask(plugin);
    }

    public void startWaiting() {
        setGameState(GameState.WAITING);
        new WaitingRunnable().runTaskTimerAsynchronously(plugin, 0, 10);
    }

    public void start() {
        setGameState(GameState.LIVE);
        new NarrationRunnable().runTaskTimerAsynchronously(plugin, 0, 10); // has to run before the shardstate updates
        setShardState(ShardState.WAITING);
        if (passUser != null) {
            herobrine = passUser;
            passUser = null;
        } else {
            herobrine = PlayerUtil.randomPlayer();
        }
        survivors.remove(herobrine);
        plugin.getServer().getScheduler().runTask(plugin, this::itemSetupHb);
        new HerobrineSetup().runTaskAsynchronously(plugin);
        for (Player p : survivors) {
            //todo items, tp
            new SurvivorSetup(p).runTaskAsynchronously(plugin);
        }
        new ShardHandler().runTaskTimer(plugin, 0, 20);
        new HerobrineItemHider().runTaskTimer(plugin, 0, 1);
    }

    public void itemSetupHb() {
        PlayerUtil.addEffect(herobrine, PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
        PlayerUtil.addEffect(herobrine, PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false);
        PlayerUtil.addEffect(herobrine, PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false);
    }

    public void end(WinType type) {
        setGameState(GameState.ENDING);
        setShardState(ShardState.INACTIVE);
        if (type == WinType.SURVIVORS) {
            PlayerUtil.broadcastTitle(ChatColor.GREEN + "SURVIVORS WIN!", "", 20, 60, 20);
            Message.broadcast(Message.format("" + ChatColor.GREEN + ChatColor.BOLD + "The Survivors " + ChatColor.YELLOW + "have defeated " + ChatColor.RED + ChatColor.BOLD + "The Herobrine!"));
            Message.broadcast(Message.format(type.getDesc()));
            PlayerUtil.broadcastSound(Sound.ENTITY_WITHER_DEATH, 1f, 1f);
        } else {
            PlayerUtil.broadcastTitle(ChatColor.RED + "HEROBRINE " + ChatColor.GREEN + " WINS!", "", 20, 60, 20);
            Message.broadcast(Message.format("" + ChatColor.RED + ChatColor.BOLD + "The Herobrine " + ChatColor.YELLOW + "has defeated all the survivors"));
            Message.broadcast(Message.format(type.getDesc()));
            PlayerUtil.broadcastSound(Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 1f);
        }
    }

    public void endCheck() {
        if (getSurvivors().size() == 0) {
            end(WinType.HEROBRINE);
        } else if (!getHerobrine().isOnline()) {
            end(WinType.SURVIVORS);
        }
    }

    public void capture(Player player) {
        player.getInventory().remove(Material.NETHER_STAR);
        shardCarrier = null;
        shardCount++;
        if (shardCount == 3) {
            setShardState(ShardState.INACTIVE);
            herobrine.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        else
            setShardState(ShardState.WAITING);
        new CaptureSequence(player).runTaskAsynchronously(plugin);
        //update hb inv
    }

    public double getHitDamage(Material item, boolean payedKit) {
        double finalDamage = 0;
        double shardModifier = 0;
        boolean normal = false;
        switch (item) {
            case IRON_AXE:
                finalDamage = (payedKit ? 1.7 : 1);
                shardModifier = (payedKit ? 0.4 : 0.5);
                break;
            case STONE_SWORD: // 0 - 1.5 | 1 - 2.0 | 2 - 2.5 | 3 - 3.0
                finalDamage = 1.5;
                shardModifier = 0.5;
                break;
            case WOODEN_SWORD: // 0 - 1.3 | 1 - 1.7 | 2 - 2.1 | 3 - 2.5
                finalDamage = 1.3;
                shardModifier = 0.4;
                break;
            case IRON_SWORD: // 0 - 1.8 | 1 - 2.5 | 2 - 3.2 | 3 - 3.9
                finalDamage = 1.8;
                shardModifier = 0.7;
                break;
            default:
                normal = true;
                break;
        }

        if (normal)
            return -1;

        if (shardCount > 0)
            finalDamage += (shardModifier * shardCount);

        return finalDamage;
    }

}
