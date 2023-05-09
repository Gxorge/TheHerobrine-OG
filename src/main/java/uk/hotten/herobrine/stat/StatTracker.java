package uk.hotten.herobrine.stat;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.Console;

import java.util.HashMap;
import java.util.UUID;

public class StatTracker implements Listener {

    public StatManager sm;

    @Getter private String displayName;
    @Getter private String internalName;
    @Getter private String desc;

    public HashMap<UUID, Integer> stat;

    public StatTracker(StatManager sm, String displayName, String internalName, String desc) {
        this.sm = sm;
        this.displayName = displayName;
        this.internalName = internalName;
        this.desc = desc;

        stat = new HashMap<>();
    }

    public void start() { Console.debug(displayName + " tracking STARTED."); Bukkit.getServer().getPluginManager().registerEvents(this, sm.getPlugin()); }
    public void stop() { Console.debug(displayName + " tracking STOPPED."); HandlerList.unregisterAll(this); }
    public void reset() { Console.debug(displayName + " tracking RESET."); stat.clear(); }

    public void increment(UUID uuid, int by) {
        if (!stat.containsKey(uuid)) {
            stat.put(uuid, by);
            Console.debug("Stat " + displayName + " for " + uuid.toString() + " has been incremented by " + by);
            return;
        }

        int old = stat.get(uuid);
        int updated = old + by;

        stat.put(uuid, updated);
        Console.debug("Stat " + displayName + " for " + uuid.toString() + " has been incremented by " + by);
    }

}
