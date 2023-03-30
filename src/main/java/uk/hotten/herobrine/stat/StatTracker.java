package uk.hotten.herobrine.stat;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

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

    public void start() { Bukkit.getServer().getPluginManager().registerEvents(this, sm.gm.getPlugin()); }

    public void reset() {
        HandlerList.unregisterAll(this);
        stat.clear();
    }

    public void increment(UUID uuid, int by) {
        if (!stat.containsKey(uuid)) {
            stat.put(uuid, by);
            return;
        }

        int old = stat.get(uuid);
        int updated = old + by;

        stat.put(uuid, updated);
    }

}
