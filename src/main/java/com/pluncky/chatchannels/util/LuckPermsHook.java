package com.pluncky.chatchannels.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsHook {
    private LuckPerms api;

    public boolean setup() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") == null) {
            return false;
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            this.api = provider.getProvider();
        }

        return this.api != null;
    }

    public TextComponent getPlayerPrefix(Player player) {
        if (this.api == null) {
            return getDefaultPrefix();
        }

        final String playerName = player.getName();

        final User user = api.getUserManager().getUser(playerName);
        if (user == null) {
            return getDefaultPrefix();
        }

        final String prefix = user.getCachedData().getMetaData().getPrefix();

        if (prefix == null || prefix.isEmpty()) {
            return getDefaultPrefix();
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(prefix);
    }

    private TextComponent getDefaultPrefix() {
        return Component.text("[Membro] ", NamedTextColor.GRAY);
    }
}
