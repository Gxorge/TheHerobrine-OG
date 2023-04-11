package uk.hotten.herobrine;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import org.bukkit.Sound;
import uk.hotten.herobrine.commands.*;
import uk.hotten.herobrine.data.SqlManager;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.data.RedisManager;
import uk.hotten.herobrine.stat.StatManager;
import uk.hotten.herobrine.utils.Console;
import uk.hotten.herobrine.world.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HerobrinePluginOG extends JavaPlugin {

    @Override
    public void onEnable() {
        Console.info("Setting up The Herobrine!");

        this.saveDefaultConfig();

        Console.showDebug = getConfig().getBoolean("showDebugMessages");

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        SqlManager sqlManager = new SqlManager(this);
        RedisManager redisManager = new RedisManager(this);

        WorldManager worldManager = new WorldManager(this);
        GameManager gameManager = new GameManager(this, worldManager, redisManager, protocolManager);
        StatManager statManager = new StatManager(this, gameManager);

        getCommand("setherobrine").setExecutor(new SetHerobrineCommand());
        getCommand("forcestart").setExecutor(new ForceStartCommand());
        getCommand("dropshard").setExecutor(new DropShardCommand());
        getCommand("pausetimer").setExecutor(new PauseTimerCommand());
        getCommand("vote").setExecutor(new VoteCommand());

        // Stops the eye of ender break SFX from the Notch's Wisdom and Totem of Healing abilities
        protocolManager.addPacketListener(
                new PacketAdapter(this, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (!gameManager.getSurvivors().contains(event.getPlayer()) && gameManager.getHerobrine() != event.getPlayer())
                            return;

                        Sound effectId = event.getPacket().getSoundEffects().read(0);
                        if (effectId == Sound.ENTITY_ENDER_EYE_DEATH) {
                            event.setCancelled(true);
                        }
                    }
                }
        );

        ScoreboardLib.setPluginInstance(this);

        Console.info("The Herobrine! is ready.");
    }

    @Override
    public void onDisable() {
        WorldManager.getInstance().clean();
    }
}
