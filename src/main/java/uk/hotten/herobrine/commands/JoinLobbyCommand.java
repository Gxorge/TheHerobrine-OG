package uk.hotten.herobrine.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;

public class JoinLobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.send(sender, Message.format("&cYou are unable to use this command."));
            return true;
        }

        Player player = (Player) sender;
        LobbyManager lm = LobbyManager.getInstance();

        if (args == null || args.length == 0) {
            lm.sendLobbyMessage(player);
            return true;
        }

        GameLobby gl = lm.getLobby(args[0]);
        if (gl == null) {
            Message.send(player, Message.format("&a" + args[0] + " does not exist."));
            return true;
        }

        GameState currentState = gl.getGameManager().getGameState();
        if (currentState == GameState.ENDING || currentState == GameState.DEAD || currentState == GameState.BOOTING || currentState == GameState.UNKNOWN) {
            Message.send(player, Message.format("&cThis lobby cannot be joined right now."));
            return true;
        }

        if (!gl.getGameManager().canJoin(player)) {
            Message.send(player, Message.format("&cThis lobby is full."));
            return true;
        }

        GameLobby playerLobby = LobbyManager.getInstance().getLobby(player);
        if (playerLobby != null && playerLobby == gl) {
            Message.send(player, Message.format("&cYou're already in this lobby!"));
            return true;
        }

        Message.send(player, Message.format("&aJoining " + gl.getLobbyId() + "..."));
        player.teleport(Bukkit.getWorld(gl.getLobbyId() + "-hub").getSpawnLocation());

        return true;
    }
}
