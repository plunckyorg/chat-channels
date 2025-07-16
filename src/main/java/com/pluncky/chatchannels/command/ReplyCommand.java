package com.pluncky.chatchannels.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.pluncky.bukkitutils.utils.messages.ErrorMessages;
import com.pluncky.chatchannels.ChatChannelsPlugin;
import com.pluncky.chatchannels.util.ChatFormatter;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ReplyCommand {
    private final ChatChannelsPlugin plugin;

    public LiteralCommandNode<CommandSourceStack> buildCommand() {
        return Commands.literal("reply")
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

        if (!this.plugin.getLastTellCache().hasLastTell(sender.getName())) {
            sender.sendMessage("§cVocê não tem ninguém para responder.");
            return 0;
        }

        final String targetName = this.plugin.getLastTellCache().getLastTell(sender.getName());

        final Player receiver = Bukkit.getPlayer(targetName);

        if (receiver == null) {
            sender.sendMessage(ErrorMessages.PLAYER_NOT_ONLINE.getMessage());
            return 0;
        }

        sender.sendMessage(ChatFormatter.formatTellMessage(sender, message, true));
        receiver.sendMessage(ChatFormatter.formatTellMessage(sender, message, false));

        return 1;
    }
}
