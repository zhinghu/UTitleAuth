package com.undeadlydev.UTitleAuth.nms;

import com.undeadlydev.UTitleAuth.enums.Versions;
import com.undeadlydev.UTitleAuth.superclass.NMSReflection;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;

public class NMSReflectionNewes extends NMSReflection {

    public NMSReflectionNewes() {}

    public void sendPacket(Player player, Object packet) {}

    public void sendActionBar(String msg, Player... players) {
        sendActionBar(msg, Arrays.asList(players));
    }

    public void sendActionBar(String msg, Collection<Player> players) {
        for (Player p : players) {
            if (p == null || !p.isOnline())
                continue;
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        }
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player... players) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, Arrays.asList(players));
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Collection<Player> players) {
        for (Player p : players) {
            if (p == null || !p.isOnline())
                continue;
            p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }
}