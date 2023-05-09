package uk.hotten.herobrine.game.runnables;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.Console;
import uk.hotten.herobrine.utils.GameState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class HerobrineItemHider extends BukkitRunnable {

    private GameManager gm;
    private Player player;
    private ProtocolManager protocolManager;

    public HerobrineItemHider(GameManager gm) {
        this.gm = gm;
        this.player = gm.getHerobrine();
        this.protocolManager = gm.getProtocolManager();
    }

    @Override
    public void run() {
        if (gm.getGameState() != GameState.LIVE) {
            cancel();
            return;
        }

        if (gm.getShardCount() == 3) {
            cancel();
            return;
        }
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> hideList = new ArrayList<>();
        hideList.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, new ItemStack(Material.AIR)));
        hideList.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, new ItemStack(Material.AIR)));

        PacketContainer hideItem = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        hideItem.getIntegers().write(0, player.getEntityId());
        hideItem.getSlotStackPairLists().write(0, hideList);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p == player) continue;
            try {
                protocolManager.sendServerPacket(p, hideItem);
            } catch (Exception e) {
                Console.error("Failed to hide Herobrine's items from " + p.getName());
                e.printStackTrace();
            }
        }
    }
}
