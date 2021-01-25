package moe.gabriella.herobrine.data;

import moe.gabriella.herobrine.utils.Console;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
        Console.info("SQL Manager is ready!");
    }

    public static SqlManager get() { return instance; }

    public Connection createConnection() throws SQLException, ClassNotFoundException {
        synchronized (this) {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + "theherobrine?userSSL=false", username, password);
        }
    }

}
