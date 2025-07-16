package com.pluncky.chatchannels.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatFormatter {
    private static final NamedTextColor CONSOLE_TEXT_COLOR = NamedTextColor.BLACK;
    private static final NamedTextColor GLOBAL_TEXT_COLOR = NamedTextColor.GRAY;
    private static final NamedTextColor LOCAL_TEXT_COLOR = NamedTextColor.YELLOW;
    private static final NamedTextColor STAFF_CHAT_TEXT_COLOR = NamedTextColor.BLUE;
    private static final NamedTextColor TELL_CHAT_TEXT_COLOR = NamedTextColor.LIGHT_PURPLE;

    public static TextComponent formatGlobalMessage(LuckPermsHook luckPermsHook, CommandSender sender, String message) {
        final TextComponent prefix = sender instanceof Player player ?
                luckPermsHook.getPlayerPrefix(player)
                : Component.text("[Server ]", CONSOLE_TEXT_COLOR);

        final String playerName = sender.getName();

        TextComponent formattedMessage = Component.text("[G] ", GLOBAL_TEXT_COLOR)
                .append(prefix)
                .append(Component.text(playerName + ": ", prefix.color()))
                .append(Component.text(message, GLOBAL_TEXT_COLOR));

        return formattedMessage;
    }

    public static TextComponent formatLocalMessage(LuckPermsHook luckPermsHook, Player sender, String message) {
        final TextComponent prefix = luckPermsHook.getPlayerPrefix(sender);
        final String playerName = sender.getName();

        final TextColor prefixColor = prefix.color();

        TextComponent formattedMessage = Component.text("[L] ", LOCAL_TEXT_COLOR)
                .append(prefix)
                .append(Component.text(playerName + ": ", prefixColor))
                .append(Component.text(message, LOCAL_TEXT_COLOR));

        return formattedMessage;
    }

    public static TextComponent formatTellMessage(CommandSender sender, String message, boolean sending) {
        final String playerName = sender.getName();

        final TextComponent formattedMessage = Component.text("[DM] ", TELL_CHAT_TEXT_COLOR)
                .append(Component.text(sending ? "Você" : playerName, TELL_CHAT_TEXT_COLOR))
                .append(Component.text(" ➤ ", NamedTextColor.DARK_GRAY))
                .append(Component.text(sending ? playerName : "Você", TELL_CHAT_TEXT_COLOR))
                .append(Component.text(": ", TELL_CHAT_TEXT_COLOR))
                .append(Component.text(message, TELL_CHAT_TEXT_COLOR));

        return formattedMessage;
    }

    public static TextComponent formatStaffChatMessage(CommandSender sender, String message) {
        final String playerName = sender.getName();

        final TextComponent formattedMessage = Component.text("[SC] ", STAFF_CHAT_TEXT_COLOR)
                .append(Component.text(playerName + ": ", STAFF_CHAT_TEXT_COLOR))
                .append(Component.text(message, STAFF_CHAT_TEXT_COLOR));

        return formattedMessage;
    }
}
