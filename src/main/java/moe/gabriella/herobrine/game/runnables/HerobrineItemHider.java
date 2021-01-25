package moe.gabriella.herobrine.game.runnables;

import com.mojang.datafixers.util.Pair;
import moe.gabriella.herobrine.game.GameManager;
import moe.gabriella.herobrine.utils.GameState;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class HerobrineItemHider extends BukkitRunnable {

    GameManager gm = GameManager.get();
    Player player = gm.getHerobrine();

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

        final List<Pair<EnumItemSlot, ItemStack>> equipList = new ArrayList<>();
        equipList.add(new com.mojang.datafixers.util.Pair<>(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.AIR))));
        equipList.add(new com.mojang.datafixers.util.Pair<>(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.AIR))));
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(player.getEntityId(), equipList);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p == player) continue;
            PlayerConnection pc = ((CraftPlayer) p).getHandle().playerConnection;
            pc.sendPacket(packet);
        }
    }
}
