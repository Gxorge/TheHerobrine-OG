package moe.gabriella.herobrine.game.runnables;

import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.Message;
import moe.gabriella.herobrine.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class HerobrineSetup extends BukkitRunnable {

    Player player = GameManager.getInstance().getHerobrine();

    @Override
    public void run() {
        try {
            player.sendMessage(Message.format(ChatColor.GREEN + "You are " + ChatColor.RED + ChatColor.BOLD + "THE HEROBRINE! " + ChatColor.MAGIC + "###" + ChatColor.RESET));
            player.sendMessage(Message.format(ChatColor.GRAY + "Destroy all survivors to take over the WORLD!"));

            PlayerUtil.sendTitle(player, ChatColor.AQUA + "Welcome to the Herobrine", ChatColor.YELLOW + "You are " + ChatColor.RED + "THE HEROBRINE", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, ChatColor.AQUA + "Be careful of the Survivors!", ChatColor.YELLOW + "They want to take you down", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, ChatColor.AQUA + "Their aim is to capture shards", ChatColor.YELLOW + "and make the you weaker", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, ChatColor.AQUA + "Shards spawn randomly", ChatColor.YELLOW + "Use your compass to find them", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, ChatColor.AQUA + "Stop them from capturing to win", ChatColor.YELLOW + "Use your special items to help", 10, 60, 10);
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Message.format(ChatColor.RED + "Error displaying your titles!"));
        }
    }
}
