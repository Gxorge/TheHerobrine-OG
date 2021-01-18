package moe.gabriella.herobrine.kit;

import lombok.Getter;
import me.gabriella.gabsgui.GUIItem;
import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Kit implements Listener {

    public GameManager gm;

    @Getter private String internalName; // don't change this later, it will break things
    @Getter private String displayName;
    @Getter private String permission;
    @Getter private String desc;
    @Getter private GUIItem displayItem;
    public HashMap<Player, ArrayList<KitAbility>> abilities;

    public Kit(GameManager gm, String internalName, String displayName, String permission, String desc, GUIItem displayItem) {
        this.gm = gm;

        this.internalName = internalName;
        this.displayName = displayName;
        this.permission = permission;
        this.desc = desc;
        this.displayItem = displayItem;
        this.abilities = new HashMap<>();
    }

    public void apply(Player player) {
        PlayerUtil.clearInventory(player);
        PlayerUtil.clearEffects(player);

        setupPlayer(player);
        setupAbilities(player);
    }

    public boolean ownsKit(Player player) {
        if (permission == null)
            return true;

        return player.hasPermission(permission);
    }

    public void addAbilityToPlayer(Player player, KitAbility ability) {
        if (!abilities.containsKey(player))
            abilities.put(player, new ArrayList<>());

        abilities.get(player).add(ability);
        Bukkit.getServer().getPluginManager().registerEvents(ability, gm.getPlugin());
        ability.apply(player);
    }

    public void voidAbilities() {
        for (Map.Entry<Player, ArrayList<KitAbility>> entry : abilities.entrySet()) {
            for (KitAbility a : entry.getValue())
                HandlerList.unregisterAll(a);
        }
        abilities = new HashMap<>();
    }

    public abstract void setupAbilities(Player player);
    public abstract void setupPlayer(Player player);
}
