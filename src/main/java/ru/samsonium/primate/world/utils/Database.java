package ru.samsonium.primate.world.utils;

import ru.samsonium.primate.world.PrimateWorld;

import java.io.File;
import java.sql.*;

public class Database {
    protected Connection cn;

    protected Database(String name) throws SQLException {
        File pluginDir = PrimateWorld.get().getDataFolder();
        if (!pluginDir.exists())
            pluginDir.mkdirs();
        String path = pluginDir.getAbsolutePath() + "/" + name + ".db";

        cn = DriverManager.getConnection("jdbc:sqlite:" + path);
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

        return stmt.executeQuery();
    }

    /**
     * Execute SQL with table modify query
     * @param sql SQL query
     * @param params List of params
     * @throws Exception if unsupported type
     * @throws SQLException if SQL has errors
     */
    protected void updateQuery(String sql, Object... params) throws Exception {
        PreparedStatement stmt = cn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof String) {
                stmt.setString(i + 1, (String) params[i]);
            } else if (params[i] instanceof Integer) {
                stmt.setInt(i + 1, (Integer) params[i]);
            } else if (params[i] instanceof Float) {
                stmt.setFloat(i + 1, (Float) params[i]);
            } else if (params[i] instanceof Double) {
                stmt.setDouble(i + 1, (Double) params[i]);
            } else {
                throw new Exception("Unsupported type");
            }
        }

        stmt.executeUpdate();
        stmt.close();
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
}
