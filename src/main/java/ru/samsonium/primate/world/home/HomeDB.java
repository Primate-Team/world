package ru.samsonium.primate.world.home;

import org.bukkit.Location;
import org.bukkit.World;
import ru.samsonium.primate.world.PrimateWorld;
import ru.samsonium.primate.world.utils.Database;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class HomeDB extends Database {
    private static HomeDB instance;

    protected HomeDB(String name) throws SQLException {
        super(name);

        query("""
            CREATE TABLE IF NOT EXISTS homes (
                uuid TEXT PRIMARY KEY,
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
        instance = new HomeDB("homes");
    }

    public static HomeDB get() {
        return instance;
    }

    /**
     * Get players by id
     * @param uuid Player id
     * @return Location or null
     */
    @Nullable public Location getByUUID(String uuid) {
        try (ResultSet rs = queryWithResult("SELECT * FROM homes WHERE uuid=?", uuid)) {
            if (rs == null || !rs.next()) return null;

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
     * Set home point
     * @param uuid Player id
     * @param world World name
     * @param x X
     * @param y Y
     * @param z Z
     * @param yaw Yaw
     * @param pitch Pitch
     */
    public boolean setHome(String uuid, String world, double x, double y, double z, float yaw, float pitch) {
        try (ResultSet exists = queryWithResult("SELECT COUNT(x) AS rows FROM homes WHERE uuid=?", uuid)) {
            boolean playerExists = false;
            if (exists.next())
                playerExists = exists.getInt("rows") > 0;

            if (playerExists) {
                updateQuery("""
                    UPDATE homes SET
                        world=?,
                        x=?, y=?, z=?,
                        yaw=?, pitch=?
                    WHERE uuid=?;
                """, world, x, y, z, yaw, pitch, uuid);
            } else {
                updateQuery("""
                    INSERT INTO homes(uuid,world,x,y,z,yaw,pitch)
                    VALUES (?,?,?,?,?,?,?);
                """, uuid, world, x, y, z, yaw, pitch);
            }

            return true;
        } catch (Exception e) {
            PrimateWorld.get().getLogger().log(Level.SEVERE, e.getMessage());
            return false;
        }
    }
}
