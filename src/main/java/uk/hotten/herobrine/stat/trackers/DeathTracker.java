package uk.hotten.herobrine.stat.trackers;

import org.bukkit.event.player.PlayerQuitEvent;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.stat.StatManager;
import uk.hotten.herobrine.stat.StatTracker;
import uk.hotten.herobrine.utils.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathTracker extends StatTracker {

    public DeathTracker(StatManager sm) {
        super(sm, "Deaths", "deaths", "How many times you died!");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void death(PlayerDeathEvent event) {
        Player player = event.getEntity();

        GameManager gm = GameManager.get();
        if (gm.getGameState() != GameState.LIVE)
            return;

        if (gm.getSurvivors().contains(player) || gm.getHerobrine() == player)
            increment(player.getUniqueId(), 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        GameManager gm = GameManager.get();
        if (gm.getGameState() != GameState.LIVE)
            return;

        if (gm.getSurvivors().contains(player) || gm.getHerobrine() == player)
            increment(player.getUniqueId(), 1);
    }
}
