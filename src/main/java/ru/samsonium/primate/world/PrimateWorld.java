package ru.samsonium.primate.world;

import org.bukkit.plugin.java.JavaPlugin;
import ru.samsonium.primate.world.home.HomeCommand;
import ru.samsonium.primate.world.home.HomeDB;
import ru.samsonium.primate.world.home.SetHomeCommand;
import ru.samsonium.primate.world.spawn.SetSpawnCommand;
import ru.samsonium.primate.world.spawn.SpawnCommand;

import java.util.Objects;
import java.util.logging.Level;

public final class PrimateWorld extends JavaPlugin {

    @Override
    public void onEnable() {
        try {

            // Initialize databases
            HomeDB.init();

            // Register commands
            Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand());
            Objects.requireNonNull(getCommand("setspawn")).setExecutor(new SetSpawnCommand());
            Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
            Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand());
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Get plugin base class instance
     * @return PrimateWorld instance
     */
    public static PrimateWorld get() {
        return getPlugin(PrimateWorld.class);
    }
}
