package uk.hotten.herobrine.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.lobby.LobbyManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.utils.ShardState;

public class DropShardCommand implements CommandExecutor {

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


        if (gm.getGameState() != GameState.LIVE || gm.getShardState() != ShardState.CARRYING) {
            Message.send(sender, Message.format("&cYou cannot run this command right now."));
            return true;
        }

        gm.getShardCarrier().getInventory().remove(Material.NETHER_STAR);
        gm.getShardHandler().drop(gm.getShardCarrier().getLocation().add(10, 0, 0));
        Message.send(sender, Message.format("The shard has been forcefully dropped."));
        return true;
    }
}
