package uk.hotten.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.Message;

public class CreateLobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(Message.format("Creating lobby..."));
        String lobby = LobbyManager.getInstance().createLobby();
        if (lobby == null) {
            sender.sendMessage(Message.format(ChatColor.RED + "Failed to create lobby, please contact your administrator."));
            return true;
        }

        sender.sendMessage(Message.format(ChatColor.GREEN + "Lobby " + lobby + " created successfully."));
        return true;
    }
}
