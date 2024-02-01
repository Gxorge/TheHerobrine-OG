package uk.hotten.herobrine.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.Message;

public class DeleteLobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args == null || args.length == 0) {
            Message.send(sender, Message.format("&cCorrect Usage: /hbdeletelobby <lobby>"));
            return true;
        }

        GameLobby gl = LobbyManager.getInstance().getLobby(args[0]);
        if (gl == null) {
            Message.send(sender, Message.format("&c" + args[0] + " does not exist."));
            return true;
        }

        Message.send(sender, Message.format("Shutting down " + gl.getLobbyId() + "..."));
        gl.shutdown(true, false);
        Message.send(sender, Message.format("&aLobby " + gl.getLobbyId() + " has been shutdown."));

        return true;
    }
}
