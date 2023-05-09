package uk.hotten.herobrine.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.Message;

public class JoinLobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.format(ChatColor.RED + "You are unable to use this command."));
            return true;
        }

        Player player = (Player) sender;

        if (args == null || args.length == 0) {
            LobbyManager.getInstance().sendLobbyMessage(player);
            return true;
        }

        player.teleport(Bukkit.getWorld(args[0] + "-hub").getSpawnLocation());

        return true;
    }
}
