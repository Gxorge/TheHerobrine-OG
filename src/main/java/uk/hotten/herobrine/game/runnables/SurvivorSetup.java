package uk.hotten.herobrine.game.runnables;

import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class SurvivorSetup extends BukkitRunnable {

    Player player;

    public SurvivorSetup(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            player.sendMessage(Message.format(ChatColor.GREEN + "You are a " + ChatColor.BOLD + "Survivor" + ChatColor.GREEN + "."));
            player.sendMessage(Message.format(ChatColor.GRAY + "Collect shards and return them to the alter to weaken Herobrine!"));

            PlayerUtil.sendTitle(player, ChatColor.AQUA + "Welcome to the Herobrine!", ChatColor.YELLOW + "You are a " + ChatColor.GREEN + "SURVIVOR", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, ChatColor.AQUA + "Be careful of " + ChatColor.RED + "The Herobrine", ChatColor.YELLOW + "For now he is just a cloud of smoke", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, ChatColor.AQUA + "Your aim is to capture shards", ChatColor.YELLOW + "and make the " + ChatColor.RED + "Herobrine " + ChatColor.YELLOW + "weaker", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, ChatColor.AQUA + "Shards spawn randomly", ChatColor.YELLOW + "Use your compass to find them", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, ChatColor.AQUA + "To make the " + ChatColor.RED + "Herobrine " + ChatColor.AQUA + "weaker", ChatColor.YELLOW + "you need to capture shards", 10, 60, 10);
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Message.format(ChatColor.RED + "Error displaying your titles!"));
        }
    }
}
