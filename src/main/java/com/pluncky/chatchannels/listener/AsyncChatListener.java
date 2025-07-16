package com.pluncky.chatchannels.listener;

import com.pluncky.chatchannels.ChatChannelsPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class AsyncChatListener implements Listener {
    private final ChatChannelsPlugin plugin;

    @EventHandler(priority = EventPriority.HIGH)
    private void onAsyncChat(AsyncChatEvent event) {
        event.setCancelled(true);

        final Player player = event.getPlayer();
        final String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        this.plugin.getServer().getScheduler().runTask(plugin, () -> {
            Bukkit.dispatchCommand(player, "local " + message);
        });
    }
}
