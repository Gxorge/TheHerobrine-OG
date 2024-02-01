package uk.hotten.herobrine.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;

public class SetHerobrineCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.send(sender, Message.format("&cYou are unable to use this command."));
            return true;
        }

        GameManager gm;
        GameLobby gl = LobbyManager.getInstance().getLobby((Player) sender);
        if (gl == null) {
            Message.send(sender, Message.format("&cYou must be in a lobby to do this."));
            return true;
        }

        gm = gl.getGameManager();

        if (gm.getGameState() != GameState.WAITING && gm.getGameState() != GameState.STARTING) {
            Message.send(sender, Message.format("&cYou cannot run this command right now."));
            return true;
        }

        if (args == null || args.length == 0) {
            if (gm.getPassUser() == null)
                Message.send(sender, Message.format("&cCorrect Usage: /hbsetherobrine <player>"));
            else {
                Message.send(sender, Message.format("&e" + gm.getPassUser().getName() + "&r will no-longer be Herobrine."));
                gm.setPassUser(null);
            }
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            Message.send(sender, Message.format("&c" + args[0] + " is not online."));
            return true;
        }

        if (!gm.getGameLobby().getPlayers().contains(player)) {
            Message.send(sender, Message.format("&c" + player.getName() + " is not in your lobby."));
            return true;
        }

        gm.setPassUser(player);
        Message.send(sender, Message.format("&e" + player.getName() + "&r will be Herobrine."));
        return true;
    }
}
