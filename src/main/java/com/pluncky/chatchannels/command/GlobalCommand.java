package com.pluncky.chatchannels.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.pluncky.chatchannels.ChatChannelsPlugin;
import com.pluncky.chatchannels.util.ChatFormatter;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class GlobalCommand {
    private final ChatChannelsPlugin plugin;

    public LiteralCommandNode<CommandSourceStack> buildCommand() {
        return Commands.literal("global")
                .then(Commands.argument("mensagem", StringArgumentType.greedyString())
                        .executes(this::execute))
                .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();

        final String message = context.getArgument("mensagem", String.class);

        if (message.isEmpty()) {
            return 0;
        }

        final TextComponent formattedMessage = ChatFormatter.formatGlobalMessage(this.plugin.getLuckPermsHook(), sender, message);

        final boolean highlightPermission = sender.hasPermission("chatchannels.messages.highlight");

        final Server server = Bukkit.getServer();

        if (highlightPermission) server.broadcast(Component.text(" "));
        server.broadcast(formattedMessage);
        if (highlightPermission) server.broadcast(Component.text(" "));

        return 1;
    }
}
