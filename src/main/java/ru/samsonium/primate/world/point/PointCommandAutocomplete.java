package ru.samsonium.primate.world.point;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.samsonium.primate.world.PrimateWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class PointCommandAutocomplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0 || !(commandSender instanceof Player)) return null;

        final List<String> result = new ArrayList<>();
        if (args.length < 2) {
            result.add("list");
            result.add("set");
            result.add("del");
            result.add("tp");
        } else if (args.length == 2 && args[0].equals("tp")) try {
            ArrayList<String> points = PointDB.get().getListByUUID(((Player) commandSender).getUniqueId().toString());
            return Objects.requireNonNull(points).subList(0, Math.min(10, points.size()));
        } catch (Exception e) {
            PrimateWorld.get().getLogger().log(Level.SEVERE, e.getMessage());
            return null;
        }

        return result;
    }
}
