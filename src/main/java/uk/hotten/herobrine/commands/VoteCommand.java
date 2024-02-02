package uk.hotten.herobrine.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.world.WorldManager;

public class VoteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (! (sender instanceof Player)) {
			Message.send(sender, Message.format("&cYou are unable to use this command."));
			return true;
		}

		WorldManager wm;
		GameLobby gl = LobbyManager.getInstance().getLobby((Player) sender);
		if (gl == null) {
			Message.send(sender, Message.format("&cYou must be in a lobby to do this."));
			return true;
		}

		wm = gl.getWorldManager();

		Player player = (Player) sender;

		if (!wm.isVotingRunning()) {
			Message.send(player, Message.format("&cYou cannot run this command right now."));
			return true;
		}

		if (args == null || args.length == 0) {
			wm.sendVotingMessage(player);
			return true;
		}

		int map;
		try {
			map = Integer.parseInt(args[0]);
		}
		catch (Exception error) {
			Message.send(player, Message.format("&cCorrect Usage: /hbvote <map number>"));
			wm.sendVotingMessage(player);
			return true;
		}

		if (! wm.getVotingMaps().containsKey(map)) {
			Message.send(player, Message.format("&cInvalid map!"));
			wm.sendVotingMessage(player);
			return true;
		}

		if (wm.getPlayerVotes().get(player) == map) {
			Message.send(player, Message.format("&cYou have already voted for this map!"));
		}
		else {
			if (wm.getPlayerVotes().get(player) != 0) {
				wm.getVotingMaps().get(wm.getPlayerVotes().get(player)).decrementVotes();
			}
			wm.getPlayerVotes().remove(player);
			wm.getPlayerVotes().put(player, map);
			wm.getVotingMaps().get(map).incrementVotes();

			Message.send(player, Message.format("&6Vote received. &b" + wm.getVotingMaps().get(map).getMapData().getName() + "&6 now has &b" + wm.getVotingMaps().get(map).getVotes() + "&6 votes."));
		}

		return true;
	}

}