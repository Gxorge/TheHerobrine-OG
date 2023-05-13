package uk.hotten.herobrine.data;

import uk.hotten.herobrine.utils.Console;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class SqlManager {

    private JavaPlugin plugin;
    private static SqlManager instance;

    private String host, username, password;
    private int port;

    public SqlManager(JavaPlugin plugin) {
        Console.info("Loading SQL Manager...");
        this.plugin = plugin;
        instance = this;

        host = plugin.getConfig().getString("sqlHost");
        username = plugin.getConfig().getString("sqlUsername");
        password = plugin.getConfig().getString("sqlPassword");
        port = plugin.getConfig().getInt("sqlPort");

        checkStatTable();
        Console.info("SQL Manager is ready!");
    }

    public static SqlManager get() { return instance; }

    public Connection createConnection() throws SQLException, ClassNotFoundException {
        synchronized (this) {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port + "/" + "theherobrine?userSSL=false", username, password);
        }
    }

    private void checkStatTable() {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "hb_stat", new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `hb_stat` (" +
                    " `uuid` varchar(36) NOT NULL," +
                    " `points` int NOT NULL DEFAULT 0," +
                    " `captures` int NOT NULL DEFAULT 0," +
                    " `kills` int NOT NULL DEFAULT 0," +
                    " `deaths` int NOT NULL DEFAULT 0," +
                    " UNIQUE KEY `uuid` (`uuid`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created stat table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check stat table.");
            e.printStackTrace();
        }
    }

}
