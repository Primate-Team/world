package ru.samsonium.primate.world.spawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import ru.samsonium.primate.world.PrimateWorld;
import ru.samsonium.primate.world.utils.messages.Messages;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Messages.CMD_PLAYER_ONLY.getMsg());
            return true;
        }

        FileConfiguration config = PrimateWorld.get().getConfig();
        if (!config.contains("spawn")) {
            commandSender.sendMessage(text("Точка спавна не установлена!", YELLOW));
            return true;
        }

        String world = config.getString("spawn.world");
        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        if (world == null) {
            commandSender.sendMessage(text("Произошла ошибка при получении координат спавна", RED));
            return true;
        }

        World spawnWorld = PrimateWorld.get().getServer().getWorld(world);
        Location spawnLocation = new Location(spawnWorld, x, y, z, yaw, pitch);

        commandSender.sendMessage(text("Перемещение на спавн...", GOLD));
        ((Player) commandSender).teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.COMMAND);

        return true;
    }
}
