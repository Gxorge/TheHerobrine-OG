package uk.hotten.herobrine;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import org.bukkit.ChatColor;
import uk.hotten.herobrine.commands.*;
import uk.hotten.herobrine.data.SqlManager;
import uk.hotten.herobrine.data.RedisManager;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.Console;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.herobrine.utils.Message;

public class HerobrinePluginOG extends JavaPlugin {

    @Override
    public void onEnable() {
        Console.info("Setting up The Herobrine!");

        this.saveDefaultConfig();

        Console.showDebug = getConfig().getBoolean("showDebugMessages");
        if (getConfig().getString("gamePrefix").toUpperCase().equals("DEFAULT"))
            Message.prefix = "" + ChatColor.DARK_GRAY + "▍ " + ChatColor.DARK_AQUA + "TheHerobrine " + ChatColor.DARK_GRAY + "▏";
        else
            Message.prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("gamePrefix"));

        SqlManager sqlManager = new SqlManager(this);
        RedisManager redisManager = new RedisManager(this);
        LobbyManager lobbyManager = new LobbyManager(this);

        getCommand("setherobrine").setExecutor(new SetHerobrineCommand());
        getCommand("forcestart").setExecutor(new ForceStartCommand());
        getCommand("dropshard").setExecutor(new DropShardCommand());
        getCommand("pausetimer").setExecutor(new PauseTimerCommand());
        getCommand("vote").setExecutor(new VoteCommand());
        getCommand("hbjoin").setExecutor(new JoinLobbyCommand());

        ScoreboardLib.setPluginInstance(this);

        Console.info("The Herobrine! is ready.");
    }

    @Override
    public void onDisable() {
        LobbyManager.getInstance().shutdown();
    }

}
