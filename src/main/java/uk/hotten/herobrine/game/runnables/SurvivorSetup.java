package uk.hotten.herobrine.game.runnables;

import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.utils.PlayerUtil;
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
            Message.send(player, Message.format("&aYou are a &lSurvivor&r&a."));
            Message.send(player, Message.format("&7Collect shards and return them to the alter to weaken Herobrine!"));

            PlayerUtil.sendTitle(player, "&bWelcome to the Herobrine!", "&eYou are a &aSURVIVOR", 500, 4000, 500);
            TimeUnit.SECONDS.sleep(4);
            PlayerUtil.sendTitle(player, "&bBe careful of &cThe Herobrine", "&eFor now he is just a cloud of smoke", 500, 4000, 500);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, "&bYour aim is to capture shards", "&eand make the &cHerobrine &eweaker", 500, 4000, 500);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, "&bShards spawn randomly", "&eUse your compass to find them", 500, 4000, 500);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, "&bTo make the &cHerobrine &bweaker", "&eyou need to capture shards", 500, 4000, 500);
        } catch (Exception e) {
            e.printStackTrace();
            Message.send(player, Message.format("&cError displaying your titles!"));
        }
    }
}
