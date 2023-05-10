package uk.hotten.herobrine.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import uk.hotten.herobrine.lobby.LobbyManager;

import java.util.List;

public class JoinLobbyCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            return LobbyManager.getInstance().getLobbyIds();
        }

        return null;
    }
}
