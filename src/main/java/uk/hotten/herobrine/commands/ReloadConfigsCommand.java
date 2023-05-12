package uk.hotten.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.Console;
import uk.hotten.herobrine.utils.Message;

public class ReloadConfigsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(Message.format(ChatColor.YELLOW + "Note: This will not affect already created lobbies!"));
        sender.sendMessage(Message.format("Reloading lobby configurations..."));
        try {
            LobbyManager.getInstance().checkAndLoadConfigs(false);

            sender.sendMessage(Message.format(ChatColor.GREEN + "Successfully loaded " + LobbyManager.getInstance().getLobbyConfigsIds().size() + " lobby configuration(s)."));
        } catch (Exception e) {
            sender.sendMessage(Message.format(ChatColor.RED + "Failed to reload lobby configuration files. Please contact your administrator."));
            Console.error("Failed to reload config files.");
            e.printStackTrace();
        }
        return true;
    }
}
