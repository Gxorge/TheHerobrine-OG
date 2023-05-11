package uk.hotten.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;

public class SpectateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.format(ChatColor.RED + "You are unable to use this command."));
            return true;
        }

        Player player = (Player) sender;

        GameManager gm;
        GameLobby gl = LobbyManager.getInstance().getLobby(player);
        if (gl == null) {
            player.sendMessage(Message.format(ChatColor.RED + "You must be in a lobby to do this."));
            return true;
        }

        gm = gl.getGameManager();

        if (gm.getGameState() != GameState.WAITING && gm.getGameState() != GameState.STARTING) {
            player.sendMessage(Message.format(ChatColor.RED + "You cannot run this command right now."));
            return true;
        }

        if (gm.getSpectators().contains(player)) {
            gm.getSpectators().remove(player);
            gm.getSurvivors().add(player);
            player.sendMessage(Message.format("You are no-longer a spectator."));
        } else {
            gm.getSpectators().add(player);
            gm.getSurvivors().remove(player);
            player.sendMessage(Message.format("You are now a spectator."));
        }

        return true;
    }
}
