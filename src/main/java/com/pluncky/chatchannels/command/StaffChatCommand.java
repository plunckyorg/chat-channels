package com.pluncky.chatchannels.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.pluncky.chatchannels.util.ChatFormatter;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class StaffChatCommand {
    private static final String STAFFCHAT_PERMISSION_NODE = "chatchannels.staffchat";

    public LiteralCommandNode<CommandSourceStack> buildCommand() {
        return Commands.literal("staffchat")
                .requires(source -> source.getSender().hasPermission(STAFFCHAT_PERMISSION_NODE))
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

        final TextComponent formattedMessage = ChatFormatter.formatStaffChatMessage(sender, message);

        final boolean highlightPermission = sender.hasPermission("chatchannels.messages.highlight");

        final Server server = Bukkit.getServer();

        if (highlightPermission) server.broadcast(Component.text(" "), STAFFCHAT_PERMISSION_NODE);
        server.broadcast(formattedMessage, STAFFCHAT_PERMISSION_NODE);
        if (highlightPermission) server.broadcast(Component.text(" "), STAFFCHAT_PERMISSION_NODE);

        return 1;
    }
}
