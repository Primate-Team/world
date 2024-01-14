package ru.samsonium.primate.world.back;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.samsonium.primate.world.PrimateWorld;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.deathMessage(text().append(text(e.getPlayer().getName(), WHITE), text(" умер", GOLD)).build());

        Player p = e.getPlayer();
        Location death = p.getLocation();
        FileConfiguration config = PrimateWorld.get().getConfig();

        String path = "deaths." + p.getName();
        config.set(path + ".world", death.getWorld().getName());
        config.set(path + ".x", death.getX());
        config.set(path + ".y", death.getY());
        config.set(path + ".z", death.getZ());
        config.set(path + ".yaw", death.getYaw());
        config.set(path + ".pitch", death.getPitch());

        p.sendMessage(text().append(text("Точка смерти сохранена. Используйте ", GOLD),
                text("/back", WHITE),
                text(", чтобы вернуться", GOLD)));
    }
}
