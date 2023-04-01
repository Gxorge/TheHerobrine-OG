package uk.hotten.herobrine.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.game.runnables.StartingRunnable;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.Message;

public class ForceStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        GameManager gm = GameManager.get();

        if (gm.getGameState() != GameState.WAITING) {
            sender.sendMessage(Message.format(ChatColor.RED + "You cannot run this command right now."));
            return true;
        }

        int startTime;
        if (args == null || args.length == 0) {
            startTime = 10;
        } else {
            try {
                startTime = Integer.parseInt(args[0]);
            } catch (Exception e) {
                sender.sendMessage(Message.format(ChatColor.RED + "Correct Usage: /forcestart [time]"));
                return true;
            }
        }

        if (startTime < 10)
            startTime = 10;

        sender.sendMessage(Message.format(ChatColor.GREEN + "The game will start in " + startTime + " seconds unless the player count goes below 2."));
        gm.startTimer = startTime+1;
        gm.setGameState(GameState.STARTING);
        new StartingRunnable(true).runTaskTimerAsynchronously(gm.getPlugin(), 0, 20);
        return true;
    }
}