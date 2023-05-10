package uk.hotten.herobrine.stat;

import lombok.Getter;
import uk.hotten.herobrine.data.SqlManager;
import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.lobby.GameLobby;
import uk.hotten.herobrine.stat.trackers.CaptureTracker;
import uk.hotten.herobrine.stat.trackers.DeathTracker;
import uk.hotten.herobrine.stat.trackers.KillsTracker;
import uk.hotten.herobrine.stat.trackers.PointsTracker;
import uk.hotten.herobrine.utils.Console;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatManager {

    private boolean nullBool;

    @Getter private JavaPlugin plugin;
    @Getter private GameLobby gameLobby;
    private GameManager gm;

    @Getter private StatTracker pointsTracker;

    @Getter private HashMap<UUID, Integer> points;
    @Getter private HashMap<UUID, Integer> captures;
    @Getter private HashMap<UUID, Integer> kills;
    @Getter private HashMap<UUID, Integer> deaths;
    @Getter HashMap<UUID, GameRank> gameRanks;

    private String highestPlayerUUID;
    private int showDeathBringerAt;

    public StatManager(JavaPlugin plugin, GameLobby gameLobby) {
        Console.info(gameLobby, "Loading Stat Manager...");
        this.plugin = plugin;
        this.gameLobby = gameLobby;
        this.gm = gameLobby.getGameManager();

        gm.setStatTrackers(new StatTracker[] {
                new PointsTracker(this),
                new CaptureTracker(this, gameLobby),
                new KillsTracker(this, gameLobby),
                new DeathTracker(this, gameLobby)
        });

        for (StatTracker tracker : gm.getStatTrackers()) {
            if (tracker.getInternalName().equals("points")) {
                pointsTracker = tracker;
                break;
            }
        }

        points = new HashMap<>();
        captures = new HashMap<>();
        kills = new HashMap<>();
        deaths = new HashMap<>();
        gameRanks = new HashMap<>();

        showDeathBringerAt = plugin.getConfig().getInt("showDeathBringerAt");
        highestPlayerUUID = getHighestPlayer();
        if (highestPlayerUUID == null)
            Console.error(gameLobby, "Failed to get UUID of highest player.");
        else
            Console.debug(gameLobby, "UUID of highest player is " + highestPlayerUUID);

        Console.info(gameLobby, "Stat Manager is ready!");
    }

    public void startTracking() {
        for (StatTracker tracker : gm.getStatTrackers()) {
            tracker.start();
        }
    }

    public void stopTracking() {
        for (StatTracker tracker : gm.getStatTrackers()) {
            tracker.stop();
        }
    }

    public void push() {
        Console.info(gameLobby, "Pushing stats...");

        for (StatTracker tracker : gm.getStatTrackers()) {
            for (Map.Entry<UUID, Integer> entry : tracker.stat.entrySet()) {
                UUID uuid = entry.getKey();
                int stat = entry.getValue();

                int curr = getCurrentStat(uuid, tracker);
                if (curr == -1) {
                    Console.error(gameLobby, "Error pushing stat, previous was -1 for " + uuid + "!");
                    continue;
                }

                setStat(uuid, tracker.getInternalName(), curr, stat);
            }

            tracker.reset();
        }

        Console.info(gameLobby, "Stats pushed!");
    }

    private String getHighestPlayer() {
        try {
            Connection connection = SqlManager.get().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT UUID FROM hb_stat ORDER BY points DESC LIMIT 1;");
            ResultSet rs = statement.executeQuery();

            String result;
            if (rs.next()) {
                result = rs.getString("uuid");
            } else {
                result = null;
            }

            connection.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setStat(UUID uuid, String name, int prev, int amount) {
        try {
            Connection connection = SqlManager.get().createConnection();

            PreparedStatement statement = connection.prepareStatement("UPDATE hb_stat SET " + name + "=? WHERE `uuid`=?");
            int next = prev + amount;

            statement.setInt(1, next);
            statement.setString(2, uuid.toString());

            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCurrentStat(UUID uuid, StatTracker stat) {
        return getCurrentStat(uuid, stat.getInternalName());
    }

    private int getCurrentStat(UUID uuid, String stat) {
        try {
            Connection connection = SqlManager.get().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT " + stat + " FROM hb_stat WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();

            int result;
            if (rs.next()) {
                result = rs.getInt(stat);
            } else {
                result = -1;
            }

            connection.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean exists(UUID uuid) {
        try {
            Connection connection = SqlManager.get().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM hb_stat WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();

            boolean result = rs.next();
            connection.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return nullBool;
        }
    }

    private void create(UUID uuid) {
        try {
            Connection connection = SqlManager.get().createConnection();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO hb_stat (uuid) VALUE (?)");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void check(UUID uuid) {
        if (!exists(uuid))
            create(uuid);

        points.put(uuid, getCurrentStat(uuid, pointsTracker));
        captures.put(uuid, getCurrentStat(uuid, "captures"));
        kills.put(uuid, getCurrentStat(uuid, "kills"));
        deaths.put(uuid, getCurrentStat(uuid, "deaths"));

        if (uuid.toString().equals(highestPlayerUUID) && points.get(uuid) >= showDeathBringerAt)
            gameRanks.put(uuid, GameRank.DEATHBRINGER);
        else
            gameRanks.put(uuid, GameRank.findRank(points.get(uuid)));
    }

    public GameRank getGameRank(UUID uuid) {
        return gameRanks.get(uuid);
    }

}
