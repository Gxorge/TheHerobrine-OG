package uk.hotten.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.lobby.data.LobbyConfig;
import uk.hotten.herobrine.utils.Message;

public class CreateLobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args == null || args.length == 0) {
            sender.sendMessage(Message.format(ChatColor.RED + "Correct Usage: /hbcreatelobby <configuration id>"));
            sendAvailable(sender);
            return true;
        }

        LobbyConfig lobbyConfig = LobbyManager.getInstance().getLobbyConfig(args[0]);
        if (lobbyConfig == null) {
            sender.sendMessage(Message.format(ChatColor.RED + args[0] + " is not a valid configuration."));
            sendAvailable(sender);
            return true;
        }

        sender.sendMessage(Message.format("Creating lobby from config '" + lobbyConfig.getId() + "'..."));
        String lobby = LobbyManager.getInstance().createLobby(lobbyConfig);
        if (lobby == null) {
            sender.sendMessage(Message.format(ChatColor.RED + "Failed to create lobby, please contact your administrator."));
            return true;
        }

        sender.sendMessage(Message.format(ChatColor.GREEN + "Lobby " + lobby + " created successfully."));
        return true;
    }

    private void sendAvailable(CommandSender sender) {
        sender.sendMessage(Message.format(ChatColor.RED + "The following types are available:"));
        LobbyManager.getInstance().getLobbyConfigsIds().forEach(id -> sender.sendMessage(Message.format(ChatColor.RED + " - " + id)));
    }
}
