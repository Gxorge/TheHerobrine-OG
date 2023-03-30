package uk.hotten.herobrine;

import uk.hotten.herobrine.data.SqlManager;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.data.RedisManager;
import uk.hotten.herobrine.stat.StatManager;
import uk.hotten.herobrine.utils.Console;
import uk.hotten.herobrine.world.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HerobrinePluginOG extends JavaPlugin {

    @Override
    public void onEnable() {
        Console.info("Setting up The Herobrine!");

        this.saveDefaultConfig();

        SqlManager sqlManager = new SqlManager(this);
        RedisManager redisManager = new RedisManager(this);

        WorldManager worldManager = new WorldManager(this);
        GameManager gameManager = new GameManager(this, worldManager, redisManager);
        StatManager statManager = new StatManager(this, gameManager);

        Console.info("The Herobrine! is ready.");
    }

    @Override
    public void onDisable() {
        WorldManager.getInstance().clean();
    }
}
