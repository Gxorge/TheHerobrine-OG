package moe.gabriella.herobrine.game;

import lombok.Getter;
import lombok.Setter;
import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.events.GameStateUpdateEvent;
import moe.gabriella.herobrine.events.ShardStateUpdateEvent;
import moe.gabriella.herobrine.game.runnables.*;
import moe.gabriella.herobrine.kit.Kit;
import moe.gabriella.herobrine.kit.KitAbility;
import moe.gabriella.herobrine.kit.kits.ArcherKit;
import moe.gabriella.herobrine.redis.RedisManager;
import moe.gabriella.herobrine.utils.*;
import moe.gabriella.herobrine.world.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {

    @Getter private JavaPlugin plugin;

    @Getter private static GameManager instance;
    private WorldManager worldManager;
    private RedisManager redis;

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

    public int startTimer = 15; //todo set to 90
    public boolean stAlmost = false;
    public boolean stFull = false;

    @Getter private Kit[] kits;
    @Getter private Kit defaultKit;
    @Getter private HashMap<Player, Kit> playerKits;

    public GameManager(JavaPlugin plugin, WorldManager worldManager, RedisManager redis) {
        Console.info("Starting Game Manager...");
        this.plugin = plugin;
        instance = this;
        this.worldManager = worldManager;
        this.redis = redis;
        plugin.getServer().getPluginManager().registerEvents(new GMListener(this), plugin);

        gameState = GameState.BOOTING;
        shardState = ShardState.WAITING;

        requiredToStart = plugin.getConfig().getInt("minPlayers");
        maxPlayers = plugin.getConfig().getInt("maxPlayers");
        allowOverfill = plugin.getConfig().getBoolean("allowOverfill");
        networkName = plugin.getConfig().getString("networkName");

        shardCount = 0;
        survivors = new ArrayList<>();

        kits = new Kit[] {
                new ArcherKit(this)
        };

        for (Kit k : kits) {
            if (k.getInternalName().equals("archer"))
                defaultKit = k;
        }

        playerKits = new HashMap<>();

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
        plugin.getServer().getScheduler().runTask(plugin, this::setupHerobrine);
        new HerobrineSetup().runTaskAsynchronously(plugin);
        plugin.getServer().getScheduler().runTask(plugin, this::setupSurvivors);
        for (Player p : survivors) {
            //todo items, tp
            new SurvivorSetup(p).runTaskAsynchronously(plugin);
        }
        new ShardHandler().runTaskTimer(plugin, 0, 20);
        new HerobrineItemHider().runTaskTimer(plugin, 0, 1);
        new HerobrineSmokeRunnable().runTaskTimer(plugin, 0, 20); //todo this needs working on, its very tps heavy
    }

    public void setupHerobrine() {
        PlayerUtil.addEffect(herobrine, PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
        PlayerUtil.addEffect(herobrine, PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, false);
        PlayerUtil.addEffect(herobrine, PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false);

        herobrine.teleport(worldManager.herobrineSpawn);
    }

    public void setupSurvivors() {
        setupKits();
        applyKits();
        for (Player p : survivors) {
            // TODO: kit items
            p.teleport(worldManager.survivorSpawn);
            PlayerUtil.addEffect(p, PotionEffectType.BLINDNESS, 60, 1, false, false);
        }
    }

    public void end(WinType type) {
        setGameState(GameState.ENDING);
        setShardState(ShardState.INACTIVE);
        voidKits();
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

    public double getSurvivorHitDamage(Material item, boolean payedKit) {
        double finalDamage = 0;
        double shardModifier = 0;
        boolean normal = false;
        switch (item) {
            case IRON_AXE:
                finalDamage = (payedKit ? 1.7 : 1.4);
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

    public double getHerobrineHitDamage(Material item) {
        double finalDamage = 0;
        switch (item) {
            case STONE_AXE:
                finalDamage = 1.5;
                break;
            case IRON_AXE:
                finalDamage = 2;
                break;
            case IRON_SWORD:
                finalDamage = 2.5;
                break;
            default:
                return -1;
        }

        return finalDamage;
    }
    
    public void hubInventory(Player player) {
        PlayerUtil.clearEffects(player);
        PlayerUtil.clearInventory(player);

        GUIItem kitItem = new GUIItem(Material.COMPASS);
        kitItem.displayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Choose " + ChatColor.AQUA + ChatColor.BOLD + "Class");
        player.getInventory().setItem(0, kitItem.build());
    }

    // Kits
    public void setupKits() {
        for (Kit kit : kits) {
            plugin.getServer().getPluginManager().registerEvents(kit, plugin);

            for (KitAbility ability : kit.getAbilities()) {
                plugin.getServer().getPluginManager().registerEvents(ability, plugin);
                ability.initialize();
            }
        }
    }

    public void voidKits() {
        for (Kit kit : kits) {
            HandlerList.unregisterAll(kit);

            for (KitAbility ability : kit.getAbilities())
                HandlerList.unregisterAll(ability);
        }
    }

    public void applyKits() {
        for (Player p : survivors) {
            Kit k = getLocalKit(p);
            k.apply(p);
        }
    }

    public void setKit(Player player, Kit kit, boolean inform) {
        playerKits.remove(player);
        playerKits.put(player, kit);
        saveKit(player, kit);

        if (inform)
            player.sendMessage(Message.format(ChatColor.YELLOW + "Set your class to " + kit.getDisplayName()));
    }

    public void saveKit(Player player, Kit kit) {
        redis.setKey("hb:kit:" + player.getUniqueId().toString(), kit.getInternalName());
    }

    public Kit getSavedKit(Player player) {
        String key = "hb:kit:" + player.getUniqueId().toString();
        if (!redis.exists(key))
            return defaultKit;

        String result = redis.getKey(key);
        for (Kit k : kits)
            if (k.getInternalName().equals(result))
                return k;

        return defaultKit;
    }

    public Kit getLocalKit(Player player) {
        if (!playerKits.containsKey(player)) {
            setKit(player, defaultKit, false);
            return defaultKit;
        } else {
            return playerKits.get(player);
        }
    }

}
