package ru.samsonium.primate.world.point;

import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.samsonium.primate.world.PrimateWorld;
import ru.samsonium.primate.world.utils.DBResultStatus;
import ru.samsonium.primate.world.utils.messages.Messages;

import java.util.ArrayList;
import java.util.logging.Level;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class PointCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Messages.CMD_PLAYER_ONLY.getMsg());
            return true;
        }

        if (args.length < 1) {
            commandSender.sendMessage(getHelp());
            return true;
        }

        Player p = (Player) commandSender;
        try {
            switch (args[0]) {
                case "list":
                    ArrayList<String> points = PointDB.get().getListByUUID(p.getUniqueId().toString());
                    if (points == null || points.isEmpty()) {
                        p.sendMessage(text("У вас пока нет точек", YELLOW));
                        return true;
                    }

                    TextComponent.Builder msg = text();
                    for (int i = 0; i < points.size(); i++) {
                        if (i > 0) msg.append(text(", ", WHITE));
                        msg.append(text(points.get(i), GOLD));
                    }

                    p.sendMessage(text().append(text("Список ваших точек:\n", WHITE), msg.build()));
                    break;

                case "set":
                    pointSet(p, args[1]);
                    break;

                case "del":
                    pointDel(p, args[1]);
                    break;

                case "tp":
                    pointTp(p, args[1]);
                    break;

                default:
                    p.sendMessage(text("Неизвестная подкомманда", RED));
            }
        } catch (Exception e) {
            PrimateWorld.get().getLogger().log(Level.SEVERE, e.getMessage());
            p.sendMessage(text("Произошла ошибка сервера при выполнении команды"));
            return true;
        }

        return true;
    }

    /**
     * Execute set point logic
     * @param p Player
     * @param name Point name
     */
    private void pointSet(Player p, String name) {
        Location point = p.getLocation();
        DBResultStatus result = PointDB.get().createPoint(p.getUniqueId().toString(),
                name,
                point.getWorld().getName(),
                point.getX(),
                point.getY(),
                point.getZ(),
                point.getYaw(),
                point.getPitch());

        switch (result) {
            case OK -> p.sendMessage(text("Точка успешно установлена!", GREEN));
            case EXISTS -> p.sendMessage(text("Точка с таким же названием уже существует", RED));
            case UNKNOWN -> p.sendMessage(text("Произошла непредвиденная ошибка, не удалось создать точку", RED));
        }
    }

    /**
     * Execute delete point logic
     * @param p Player
     * @param name Point name
     */
    private void pointDel(Player p, String name) {
        DBResultStatus result = PointDB.get().deletePoint(p.getUniqueId().toString(), name);

        switch (result) {
            case OK -> p.sendMessage(text("Точка успешно удалена!", GREEN));
            case NOT_FOUND -> p.sendMessage(text("Точка с таким названием не найдена", RED));
            case UNKNOWN -> p.sendMessage(text("Произошла непредвиденная ошибка, не удалось удалить точку", RED));
        }
    }

    /**
     * Execute point teleport logic
     * @param p Player
     * @param name Point name
     */
    private void pointTp(Player p, String name) {
        Location point = PointDB.get().getByName(p.getUniqueId().toString(), name);
        if (point == null) {
            p.sendMessage(text("Не удалось найти точку с таким названием", RED));
            return;
        }

        p.sendMessage(text("Перемещение на точку...", GOLD));
        p.teleport(point);
    }

    /**
     * Help page generator
     * @return TextComponent of help page
     */
    private TextComponent getHelp() {
        return text().append(text("---------- [", WHITE),
                text(" Точки ", GOLD),
                text("] ----------\n", WHITE),
                text(" /point list", YELLOW),
                text(" - список точек\n", GRAY),
                text(" /point set <", YELLOW),
                text("название", WHITE),
                text(">", YELLOW),
                text(" - установить точку\n", GRAY),
                text(" /point del <", YELLOW),
                text("название", WHITE),
                text(">", YELLOW),
                text(" - удалить точку\n", GRAY),
                text(" /point tp <", YELLOW),
                text("название", WHITE),
                text(">", YELLOW),
                text(" - переместиться на точку\n", GRAY),
                text("--------------------------------")
        ).build();
    }
}
