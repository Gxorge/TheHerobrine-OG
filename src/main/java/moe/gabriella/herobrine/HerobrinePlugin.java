package moe.gabriella.herobrine;

import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.redis.RedisManager;
import moe.gabriella.herobrine.utils.Console;
import moe.gabriella.herobrine.world.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HerobrinePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Console.info("Setting up The Herobrine!");

        this.saveDefaultConfig();

        RedisManager redisManager = new RedisManager(this);

        WorldManager worldManager = new WorldManager(this);
        GameManager gameManager = new GameManager(this, worldManager, redisManager);

        Console.info("The Herobrine! is ready.");
    }

    @Override
    public void onDisable() {
        WorldManager.getInstance().clean();
    }
}
