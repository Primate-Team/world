package ru.samsonium.primate.world.home;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.samsonium.primate.world.utils.messages.Messages;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Messages.CMD_PLAYER_ONLY.getMsg());
            return true;
        }

        Player p = (Player) commandSender;
        Location home = HomeDB.get().getByUUID(p.getUniqueId().toString());
        if (home == null) {
            p.sendMessage(text("Вы ещё не устанавливали точку дома", YELLOW));
            return true;
        }

        p.sendMessage(text("Перемещение домой...", GOLD));
        p.teleport(home);

        return true;
    }
}
