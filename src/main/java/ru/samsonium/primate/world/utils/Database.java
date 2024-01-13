package ru.samsonium.primate.world.utils;

import ru.samsonium.primate.world.PrimateWorld;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instance;
    protected Connection cn;

    protected Database(String path) throws SQLException {
        cn = DriverManager.getConnection("jdbc:sqlite:" + path);
    }

    /**
     * Initialize database
     */
    protected void init(String dbName) throws SQLException {
        File pluginDir = PrimateWorld.get().getDataFolder();
        if (!pluginDir.exists())
            pluginDir.mkdirs();
        String path = pluginDir.getAbsolutePath() + "/" + dbName + "db";

        if (instance != null) return;
        instance = new Database(path);
    }

    /**
     * Get db class instance
     * @return Database instance
     */
    public static Database get() {
        return instance;
    }
}
