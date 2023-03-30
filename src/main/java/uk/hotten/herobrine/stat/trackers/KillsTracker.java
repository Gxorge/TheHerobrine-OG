package uk.hotten.herobrine.stat.trackers;

import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.stat.StatManager;
import uk.hotten.herobrine.stat.StatTracker;
import uk.hotten.herobrine.utils.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillsTracker extends StatTracker {

    public KillsTracker(StatManager sm) {
        super(sm, "Kills", "kills", "How many kills you got!");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void death(PlayerDeathEvent event) {
        Player player = event.getEntity();

        GameManager gm = GameManager.get();
        if (gm.getGameState() != GameState.LIVE)
            return;

        if (player.getKiller() == null)
            return;

        Player killer = player.getKiller();

        if (gm.getSurvivors().contains(player) || gm.getHerobrine() == player) {
            if (gm.getSurvivors().contains(killer) || gm.getHerobrine() == killer) {
                increment(killer.getUniqueId(), 1);
            }
        }
    }
}
