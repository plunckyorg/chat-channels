package com.pluncky.chatchannels.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.pluncky.bukkitutils.command.model.OnlinePlayerArgument;
import com.pluncky.bukkitutils.utils.messages.ErrorMessages;
import com.pluncky.chatchannels.ChatChannelsPlugin;
import com.pluncky.chatchannels.util.ChatFormatter;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class TellCommand {
    private final ChatChannelsPlugin plugin;

    public LiteralCommandNode<CommandSourceStack> buildCommand() {
        return Commands.literal("tell")
                .then(Commands.argument("jogador", OnlinePlayerArgument.get())
                        .then(Commands.argument("mensagem", StringArgumentType.greedyString())
                                .executes(this::execute)
                        )
                ).build();
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();

        final Player target = context.getArgument("jogador", Player.class);

        if (target == null) {
            sender.sendMessage(Component.text(ErrorMessages.PLAYER_NOT_FOUND.getMessage(), NamedTextColor.RED));
            return 0;
        }

        final String message = context.getArgument("mensagem", String.class);

        if (message.isEmpty()) {
            sender.sendMessage(Component.text("§cUso: /tell <jogador> <mensagem>", NamedTextColor.RED));
            return 0;
        }

        sender.sendMessage(ChatFormatter.formatTellMessage(sender, message, true));
        target.sendMessage(ChatFormatter.formatTellMessage(sender, message, false));

        this.plugin.getLastTellCache().setLastTell(sender.getName(), target.getName());

        return 1;
    }
}
