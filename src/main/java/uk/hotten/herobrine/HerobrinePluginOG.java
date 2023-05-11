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

        getCommand("hbsetherobrine").setExecutor(new SetHerobrineCommand());
        getCommand("hbforcestart").setExecutor(new ForceStartCommand());
        getCommand("hbdropshard").setExecutor(new DropShardCommand());
        getCommand("hbpausetimer").setExecutor(new PauseTimerCommand());
        getCommand("hbvote").setExecutor(new VoteCommand());
        getCommand("hbvote").setTabCompleter(new VoteCompleter());
        getCommand("hbjoin").setExecutor(new JoinLobbyCommand());
        getCommand("hbjoin").setTabCompleter(new JoinLobbyCompleter());
        getCommand("hbcreatelobby").setExecutor(new CreateLobbyCommand());
        getCommand("hbdeletelobby").setExecutor(new DeleteLobbyCommand());
        getCommand("hbdeletelobby").setTabCompleter(new DeleteLobbyCompleter());
        getCommand("hbspectate").setExecutor(new SpectateCommand());

        ScoreboardLib.setPluginInstance(this);

        Console.info("The Herobrine! is ready.");
    }

    @Override
    public void onDisable() {
        LobbyManager.getInstance().shutdown();
    }

}
