package moe.gabriella.herobrine.redis;

import lombok.Getter;
import moe.gabriella.herobrine.utils.Console;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {

    private JavaPlugin plugin;
    @Getter private static RedisManager instance;

    private JedisPool readPool;
    private JedisPool writePool;
    private String password;
    private boolean pwRequired;

    public RedisManager(JavaPlugin plugin) {
        Console.info("Loading Redis Manager...");
        this.plugin = plugin;
        instance = this;

        String host = plugin.getConfig().getString("redisHost");
        int port = plugin.getConfig().getInt("redisPort");
        password = plugin.getConfig().getString("redisPassword");
        pwRequired = !(password == null || password.equals("") || password.equals(" "));

        readPool = new JedisPool(new JedisPoolConfig(), host, port);
        writePool = new JedisPool(new JedisPoolConfig(), host, port);

        if (testConnection())
            Console.info("Redis Manager is ready!");
    }

    private boolean testConnection() {
        Console.debug("Testing redis connection...");
        try {
            try (Jedis jedis = readPool.getResource()) {
                if (pwRequired) jedis.auth(password);

                boolean e = jedis.exists("testing");

                Console.debug("Test successful!");
                return true;
            }
        } catch (Exception e) {
            Console.error("Error testing connection! Please see below:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean exists(String key) {
        try (Jedis jedis = readPool.getResource()) {
            if (pwRequired) jedis.auth(password);

            return jedis.exists(key);
        }
    }

    public String getKey(String key) {
        try (Jedis jedis = readPool.getResource()) {
            if (pwRequired) jedis.auth(password);

            return jedis.get(key);
        }
    }

    public void setKey(String key, String value) {
        try (Jedis jedis = writePool.getResource()) {
            if (pwRequired) jedis.auth(password);

            jedis.set(key, value);
        }
    }

    public void deleteKey(String key) {
        try (Jedis jedis = writePool.getResource()) {
            if (pwRequired) jedis.auth(password);

            jedis.del(key);
        }
    }

}
