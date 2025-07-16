package com.pluncky.chatchannels;

import com.pluncky.chatchannels.cache.LastTellCache;
import com.pluncky.chatchannels.command.*;
import com.pluncky.chatchannels.listener.AsyncChatListener;
import com.pluncky.chatchannels.util.LuckPermsHook;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class ChatChannelsPlugin extends JavaPlugin {
    private final LastTellCache lastTellCache = new LastTellCache();
    private LuckPermsHook luckPermsHook;

    @Override
    public void onEnable() {
        this.init();
        getLogger().info("Chat channels plugin loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Chat channels plugin unloaded!");
    }

    private void init() {
        saveDefaultConfig();
        this.hook();
        this.registerListeners(
                new AsyncChatListener(this)
        );
        this.registerCommands();
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();

        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            commands.register(
                    new LocalCommand(this).buildCommand(),
                    "Envia uma mensagem para os jogadores próximos",
                    List.of("l")
            );

            commands.register(
                    new GlobalCommand(this).buildCommand(),
                    "Envia uma mensagem para todos os jogadores do servidor",
                    List.of("g")
            );

            commands.register(
                    new StaffChatCommand().buildCommand(),
                    "Envia uma mensagem para todos os jogadores com permissão de staff",
                    List.of("sc")
            );

            commands.register(
                    new TellCommand(this).buildCommand(),
                    "Envia uma mensagem privada para um jogador",
                    List.of("msg", "w", "whisper")
            );

            commands.register(
                    new ReplyCommand(this).buildCommand(),
                    "Responde a última mensagem privada recebida",
                    List.of("r", "responder")
            );
        });
    }

    private void hook() {
        this.luckPermsHook = new LuckPermsHook();
        if (!this.luckPermsHook.setup()) {
            getLogger().warning("LuckPerms not found or failed to hook. Prefixes will not be available.");
        }
    }
}
