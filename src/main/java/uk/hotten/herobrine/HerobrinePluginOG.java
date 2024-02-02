package uk.hotten.herobrine;

import org.bukkit.plugin.java.JavaPlugin;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import uk.hotten.herobrine.commands.CreateLobbyCommand;
import uk.hotten.herobrine.commands.CreateLobbyCompleter;
import uk.hotten.herobrine.commands.DeleteLobbyCommand;
import uk.hotten.herobrine.commands.DeleteLobbyCompleter;
import uk.hotten.herobrine.commands.DropShardCommand;
import uk.hotten.herobrine.commands.ForceStartCommand;
import uk.hotten.herobrine.commands.JoinLobbyCommand;
import uk.hotten.herobrine.commands.JoinLobbyCompleter;
import uk.hotten.herobrine.commands.PauseTimerCommand;
import uk.hotten.herobrine.commands.ReloadConfigsCommand;
import uk.hotten.herobrine.commands.SetHerobrineCommand;
import uk.hotten.herobrine.commands.SpectateCommand;
import uk.hotten.herobrine.commands.VoteCommand;
import uk.hotten.herobrine.commands.VoteCompleter;
import uk.hotten.herobrine.data.RedisManager;
import uk.hotten.herobrine.data.SqlManager;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.Console;
import uk.hotten.herobrine.utils.Message;

public class HerobrinePluginOG extends JavaPlugin {

	@Override
	public void onEnable() {

		Console.info("Setting up The Herobrine!");

		this.saveDefaultConfig();

		Console.showDebug = getConfig().getBoolean("showDebugMessages");
		if (getConfig().getString("gamePrefix").toUpperCase().equals("DEFAULT"))
			Message.prefix = "&8▍ &3TheHerobrine &8▏";
		else
			Message.prefix = getConfig().getString("gamePrefix");

		new SqlManager(this);
		new RedisManager(this);
		new LobbyManager(this);

		getCommand("hbsetherobrine").setExecutor(new SetHerobrineCommand());
		getCommand("hbforcestart").setExecutor(new ForceStartCommand());
		getCommand("hbdropshard").setExecutor(new DropShardCommand());
		getCommand("hbpausetimer").setExecutor(new PauseTimerCommand());
		getCommand("hbvote").setExecutor(new VoteCommand());
		getCommand("hbvote").setTabCompleter(new VoteCompleter());
		getCommand("hbjoin").setExecutor(new JoinLobbyCommand());
		getCommand("hbjoin").setTabCompleter(new JoinLobbyCompleter());
		getCommand("hbcreatelobby").setExecutor(new CreateLobbyCommand());
		getCommand("hbcreatelobby").setTabCompleter(new CreateLobbyCompleter());
		getCommand("hbdeletelobby").setExecutor(new DeleteLobbyCommand());
		getCommand("hbdeletelobby").setTabCompleter(new DeleteLobbyCompleter());
		getCommand("hbspectate").setExecutor(new SpectateCommand());
		getCommand("hbreloadconfigs").setExecutor(new ReloadConfigsCommand());

		ScoreboardLib.setPluginInstance(this);

		Console.info("The Herobrine! is ready.");

	}

	@Override
	public void onDisable() {

		LobbyManager.getInstance().shutdown();

	}

}