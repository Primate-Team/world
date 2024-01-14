package ru.samsonium.primate.world.utils;

import ru.samsonium.primate.world.PrimateWorld;

import java.io.File;
import java.sql.*;

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
     * Execute query with result
     * @param sql SQL query
     * @param params List of params in statement
     * @return ResultSet of query
     * @throws Exception if unsupported type detected
     * @throws SQLException if SQL has errors
     */
    protected ResultSet queryWithResult(String sql, Object... params) throws Exception {
        PreparedStatement stmt = cn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof String) {
                stmt.setString(i + 1, (String) params[i]);
            } else if (params[i] instanceof Integer) {
                stmt.setInt(i + 1, (Integer) params[i]);
            } else {
                throw new Exception("Unsupported type");
            }
        }

        ResultSet rs = stmt.executeQuery();
        stmt.close();

        return rs;
    }

    /**
     * Execute query
     * @param sql SQL query
     * @throws SQLException if SQL has errors
     */
    protected void query(String sql) throws SQLException {
        Statement stmt = cn.createStatement();
        stmt.execute(sql);
        stmt.close();
    }

    /**
     * Get db class instance
     * @return Database instance
     */
    public static Database get() {
        return instance;
    }
}
