package com.pluncky.chatchannels.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.pluncky.chatchannels.ChatChannelsPlugin;
import com.pluncky.chatchannels.util.ChatFormatter;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class LocalCommand {
    private final ChatChannelsPlugin plugin;

    public LiteralCommandNode<CommandSourceStack> buildCommand() {
        return Commands.literal("local")
                .then(Commands.argument("mensagem", StringArgumentType.greedyString())
                        .executes(this::execute))
                .build();
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();
        if (!(sender instanceof Player player)) {
            return 0;
        }

        final String message = context.getArgument("mensagem", String.class);

        if (message.isEmpty()) {
            return 0;
        }

        final TextComponent formattedMessage = ChatFormatter.formatLocalMessage(this.plugin.getLuckPermsHook(), player, message);

        final boolean highlightPermission = player.hasPermission("chatchannels.messages.highlight");

        final Set<Player> nearbyPlayers = this.getNearbyPlayers(player);

        for (Player player_ : nearbyPlayers) {
            if (highlightPermission) player_.sendMessage(" ");
            player_.sendMessage(formattedMessage);
            if (highlightPermission) player_.sendMessage(" ");
        }

        return 1;
    }

    private Set<Player> getNearbyPlayers(Player player) {
        final int radius = this.plugin.getConfig().getInt("local-radius", 32);

        Stream<Player> nearbyPlayers = player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius)
                .stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity);

        return nearbyPlayers.collect(Collectors.toSet());
    }
}
