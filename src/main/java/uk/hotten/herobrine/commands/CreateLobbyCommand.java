package uk.hotten.herobrine.commands;

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
            Message.send(sender, Message.format("&cCorrect Usage: /hbcreatelobby <configuration id>"));
            sendAvailable(sender);
            return true;
        }

        LobbyConfig lobbyConfig = LobbyManager.getInstance().getLobbyConfig(args[0]);
        if (lobbyConfig == null) {
            Message.send(sender, Message.format("&c" + args[0] + " is not a valid configuration."));
            sendAvailable(sender);
            return true;
        }

        Message.send(sender, "Creating lobby from config '" + lobbyConfig.getId() + "'...");
        String lobby = LobbyManager.getInstance().createLobby(lobbyConfig);
        if (lobby == null) {
            Message.send(sender, Message.format("&cFailed to create lobby, please contact your administrator."));
            return true;
        }

        Message.send(sender, Message.format("&aLobby " + lobby + " created successfully."));
        return true;
    }

    private void sendAvailable(CommandSender sender) {
        Message.send(sender, Message.format("&cThe following types are available:"));
        LobbyManager.getInstance().getLobbyConfigsIds().forEach(id -> Message.send(sender, Message.format("&c - " + id)));
    }
}
