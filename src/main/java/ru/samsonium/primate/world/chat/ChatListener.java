package ru.samsonium.primate.world.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class ChatListener implements Listener {

    @EventHandler
    public void onMessage(AsyncChatEvent e) {
        e.message(text()
                .append(text("Примат ", GOLD))
                .append(text(e.getPlayer().getName(), WHITE))
                .append(text(" → ", GRAY))
                .append(e.originalMessage())
                .build());
    }
}
