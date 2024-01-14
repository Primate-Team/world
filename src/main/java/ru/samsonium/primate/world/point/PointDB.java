package ru.samsonium.primate.world.point;

import org.bukkit.Location;
import org.bukkit.World;
import ru.samsonium.primate.world.PrimateWorld;
import ru.samsonium.primate.world.utils.Database;
import ru.samsonium.primate.world.utils.DBResultStatus;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

public class PointDB extends Database {
    private static PointDB instance;

    protected PointDB() throws SQLException {
        super("points");

        query("""
            CREATE TABLE IF NOT EXISTS points (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                uuid TEXT NOT NULL,
                name TEXT NOT NULL,
                world TEXT NOT NULL,
                x REAL NOT NULL,
                y REAL NOT NULL,
                z REAL NOT NULL,
                yaw REAL NOT NULL,
                pitch REAL NOT NULL
            );
        """);
    }

    public static void init() throws SQLException {
        if (instance != null) return;
        instance = new PointDB();
    }

    public static PointDB get() {
        return instance;
    }

    /**
     * Get point names list by player id
     * @param uuid Player id
     * @return List of names
     */
    @Nullable public ArrayList<String> getListByUUID(String uuid) {
        try (ResultSet rs = queryWithResult("SELECT name FROM points WHERE uuid=?", uuid)) {
            ArrayList<String> result = new ArrayList<>();
            while (rs.next())
                result.add(rs.getString("name"));

            return result;
        } catch (Exception e) {
            PrimateWorld.get().getLogger().log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    /**
     * Get point info using player id and point name
     * @param uuid Player id
     * @param name Point name
     * @return Location instance or null
     */
    @Nullable public Location getByName(String uuid, String name) {
        try (ResultSet rs = queryWithResult("SELECT * FROM points WHERE uuid=? AND name=?", uuid, name)) {
            if (!rs.next()) return null;

            World world = PrimateWorld.get().getServer().getWorld(rs.getString("world"));
            double x = rs.getDouble("x");
            double y = rs.getDouble("y");
            double z = rs.getDouble("z");
            float yaw = rs.getFloat("yaw");
            float pitch = rs.getFloat("pitch");

            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            PrimateWorld.get().getLogger().log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    /**
     * Create new point for specified user
     * @param uuid Player id
     * @param name Point name
     * @param world World name
     * @param x X
     * @param y Y
     * @param z Z
     * @param yaw Yaw
     * @param pitch Pitch
     * @return Operation status
     */
    public DBResultStatus createPoint(String uuid, String name, String world, double x, double y, double z, float yaw, float pitch) {
        try (ResultSet rs = queryWithResult("SELECT COUNT(x) as row FROM points WHERE uuid=? AND name=?", uuid, name)) {
            if (!rs.next()) return DBResultStatus.UNKNOWN;
            int count = rs.getInt("row");
            if (count > 0) return DBResultStatus.EXISTS;

            updateQuery("INSERT INTO points(uuid,name,world,x,y,z,yaw,pitch) VALUES (?,?,?,?,?,?,?,?)",
                    uuid, name, world,
                    x, y, z,
                    yaw, pitch);

            return DBResultStatus.OK;
        } catch (Exception e) {
            PrimateWorld.get().getLogger().log(Level.SEVERE, e.getMessage());
            return DBResultStatus.UNKNOWN;
        }
    }

    /**
     * Delete point by name
     * @param uuid Player id
     * @param name Point name
     * @return Operation status
     */
    public DBResultStatus deletePoint(String uuid, String name) {
        try (ResultSet rs = queryWithResult("SELECT COUNT(x) as row FROM points WHERE uuid=? AND name=?", uuid, name)) {
            if (!rs.next()) return DBResultStatus.UNKNOWN;
            int count = rs.getInt("row");
            if (count == 0) return DBResultStatus.NOT_FOUND;

            updateQuery("DELETE FROM points WHERE uuid=? AND name=?", uuid, name);

            return DBResultStatus.OK;
        } catch (Exception e) {
            PrimateWorld.get().getLogger().log(Level.SEVERE, e.getMessage());
            return DBResultStatus.UNKNOWN;
        }
    }
}
