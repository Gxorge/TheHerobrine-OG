package uk.hotten.herobrine.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;

import java.util.ArrayList;
import java.util.List;

public class VoteCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        GameLobby gl = LobbyManager.getInstance().getLobby((Player) sender);
        if (gl == null) {
            return null;
        }

        List<String> toReturn = new ArrayList<>();
        gl.getWorldManager().getVotingMaps().keySet().forEach(i -> toReturn.add(i.toString()));

        return toReturn;

    }
}
