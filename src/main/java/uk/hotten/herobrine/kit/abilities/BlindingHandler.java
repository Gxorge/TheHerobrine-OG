package uk.hotten.herobrine.kit.abilities;

import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class BlindingHandler extends BukkitRunnable {

    Item nugget;

    public BlindingHandler(Item nugget) {
        this.nugget = nugget;
    }

    @Override
    public void run() {
        try { TimeUnit.SECONDS.sleep(2); } catch (Exception e) { e.printStackTrace(); }
        Bukkit.getServer().getScheduler().runTask(GameManager.get().getPlugin(), () -> {
            Location loc  = nugget.getLocation();
            nugget.remove();
            loc.getWorld().createExplosion(loc, 0f, false, false);
            for (Player p : GameManager.get().getSurvivors()) {
                if (PlayerUtil.getDistance(p, loc) <= 5.5) {
                    PlayerUtil.addEffect(p, PotionEffectType.BLINDNESS, 100, 1, false, false);
                }
            }
        });
    }
}
