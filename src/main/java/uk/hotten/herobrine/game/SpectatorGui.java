package uk.hotten.herobrine.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.gxui.GUIBase;
import uk.hotten.gxui.GUIButton;
import uk.hotten.gxui.GUIItem;

import java.util.ArrayList;

public class SpectatorGui extends GUIBase {

    private GameManager gm;

    public SpectatorGui(JavaPlugin plugin, Player player, GameManager gm) {
        super(plugin, player, "&8Spectator Menu", 18, false);
        this.gm = gm;
    }

    @Override
    public void setupItems() {
        int current = 0;
        ArrayList<Player> players = new ArrayList<>(gm.getSurvivors());
        players.add(0, gm.getHerobrine());
        for (Player p : players) {
            GUIItem head = new GUIItem(Material.PLAYER_HEAD, 1, gm.getTeamColours().get(p) + p.getName(), p.getName());
            head.button(new GUIButton() {
                @Override
                public boolean leftClick() {
                    getPlayer().closeInventory();
                    getPlayer().teleport(p);
                    return true;
                }

                @Override public boolean leftClickShift() { return false; }
                @Override public boolean rightClick() { return false; }
                @Override public boolean rightClickShift() { return false; }
            });

            addItem(current, head);
            current++;
        }
    }
}
