package ru.samsonium.primate.world.back;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.samsonium.primate.world.PrimateWorld;
import ru.samsonium.primate.world.utils.messages.Messages;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class BackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Messages.CMD_PLAYER_ONLY.getMsg());
            return true;
        }

        Player p = (Player) commandSender;
        FileConfiguration config = PrimateWorld.get().getConfig();

        String path = "deaths." + p.getName();
        if (!config.contains(path)) {
            p.sendMessage(text("У вас нет сохранённой точки смерти", RED));
            return true;
        }

        String worldName = config.getString(path + ".world");
        assert worldName != null;

        World world = PrimateWorld.get().getServer().getWorld(worldName);
        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        float yaw = (float) config.getDouble(path + ".yaw");
        float pitch = (float) config.getDouble(path + ".pitch");

        Location death = new Location(world, x, y, z, yaw, pitch);

        p.sendMessage(text("Перемещение на место смерти...", GOLD));
        p.teleport(death);

        return true;
    }
}
