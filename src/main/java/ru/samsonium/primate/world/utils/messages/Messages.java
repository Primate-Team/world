package ru.samsonium.primate.world.utils.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public enum Messages {
    CMD_PLAYER_ONLY(text("Данная команда доступна только игрокам", RED));

    private TextComponent msg;
    Messages(TextComponent msg) {
        this.msg = msg;
    }

    public TextComponent getMsg() {
        return msg;
    }
}
