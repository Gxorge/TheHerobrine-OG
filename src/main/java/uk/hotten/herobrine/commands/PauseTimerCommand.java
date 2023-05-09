package uk.hotten.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;

public class PauseTimerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.format(ChatColor.RED + "You are unable to use this command."));
            return true;
        }

        GameManager gm;
        GameLobby gl = LobbyManager.getInstance().getLobby((Player) sender);
        if (gl == null) {
            sender.sendMessage(Message.format(ChatColor.RED + "You must be in a lobby to do this."));
            return true;
        }

        gm = gl.getGameManager();

        if (gm.getGameState() != GameState.WAITING && gm.getGameState() != GameState.STARTING) {
            sender.sendMessage(Message.format(ChatColor.RED + "You cannot run this command right now."));
            return true;
        }

        if (!gm.timerPaused) {
            sender.sendMessage(Message.format("The timer has been paused."));
            // If the timer was running, the starting runnable will stop it.
        } else {
            sender.sendMessage(Message.format("The timer has been un-paused."));
            // Start check occurs below to restart the timer if there is enough players
        }

        gm.timerPaused = !gm.timerPaused;
        gm.startCheck(); // This will check the pause status, so it won't run if the timer has been paused.

        return true;
    }
}
