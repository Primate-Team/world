package ru.samsonium.primate.world.home;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.samsonium.primate.world.utils.messages.Messages;

import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Messages.CMD_PLAYER_ONLY.getMsg());
            return true;
        }

        Player p = (Player) commandSender;
        Location home = p.getLocation();
        boolean result = HomeDB.get().setHome(p.getUniqueId().toString(),
                home.getWorld().getName(),
                home.getX(),
                home.getY(),
                home.getZ(),
                home.getYaw(),
                home.getPitch());

        if (result) p.sendMessage(text("Точка дома успешно установлена!", GREEN));
        else p.sendMessage(text("Не удалось установить точку дома :(", RED));

        return true;
    }
}
