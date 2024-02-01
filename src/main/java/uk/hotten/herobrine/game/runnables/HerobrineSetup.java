package uk.hotten.herobrine.game.runnables;

import uk.hotten.herobrine.utils.Message;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class HerobrineSetup extends BukkitRunnable {

    private Player player;

    public HerobrineSetup(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            Message.send(player, Message.format("&aYou are &c&lTHE HEROBRINE! &k###&r"));
            Message.send(player, Message.format("&7Destroy all survivors to take over the WORLD!"));

            PlayerUtil.sendTitle(player, "&bWelcome to the Herobrine", "&eYou are &cTHE HEROBRINE", 10, 80, 10);
            TimeUnit.SECONDS.sleep(4);
            PlayerUtil.sendTitle(player, "&bBe careful of the Survivors!", "&eThey want to take you down", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player,"&bTheir aim is to capture shards", "&eand make the you weaker", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, "&bShards spawn randomly", "&eUse your compass to find them", 10, 60, 10);
            TimeUnit.SECONDS.sleep(3);
            PlayerUtil.sendTitle(player, "&bStop them from capturing to win", "&eUse your special items to help", 10, 60, 10);
        } catch (Exception e) {
            e.printStackTrace();
            Message.send(player, Message.format("&cError displaying your titles!"));
        }
    }
}
