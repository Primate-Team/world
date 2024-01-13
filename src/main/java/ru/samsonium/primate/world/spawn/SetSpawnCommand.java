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

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Messages.CMD_PLAYER_ONLY.getMsg());
            return true;
        }

        Player p = (Player) commandSender;
        if (!p.isOp()) {
            commandSender.sendMessage(text("Данная команда только для операторов", RED));
            return true;
        }

        FileConfiguration config = PrimateWorld.get().getConfig();
        Location spawnLocation = p.getLocation();
        config.set("spawn.world", spawnLocation.getWorld().getName());
        config.set("spawn.x", spawnLocation.getX());
        config.set("spawn.y", spawnLocation.getY());
        config.set("spawn.z", spawnLocation.getZ());
        config.set("spawn.yaw", spawnLocation.getYaw());
        config.set("spawn.pitch", spawnLocation.getPitch());

        commandSender.sendMessage(text("Точка спавна успешно установлена!", GOLD));

        return true;
    }
}
