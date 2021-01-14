package moe.gabriella.herobrine;

import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.Console;
import org.bukkit.plugin.java.JavaPlugin;

public class HerobrinePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Console.info("Setting up The Herobrine!");

        this.saveDefaultConfig();

        GameManager gameManager = new GameManager(this);

        Console.info("The Herobrine! is ready.");
    }
}
