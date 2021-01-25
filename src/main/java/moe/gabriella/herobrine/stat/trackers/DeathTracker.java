package moe.gabriella.herobrine.stat.trackers;

import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.stat.StatManager;
import moe.gabriella.herobrine.stat.StatTracker;
import moe.gabriella.herobrine.utils.GameState;
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
}
