package com.undeadlydev.UTitleAuth.nms;

import com.undeadlydev.UTitleAuth.superclass.NMSReflection;
import com.undeadlydev.UTitleAuth.enums.Versions;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;

public class NMSReflectionNew extends NMSReflection {

    public NMSReflectionNew() {
    }

    public void sendPacket(Player player, Object packet) {
        try {
            if(Versions.getVersion().esMayorIgual(Versions.v1_17)) {
                Object handle = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = handle.getClass().getField("b").get(handle);
                playerConnection.getClass().getMethod("a", getClass("net.minecraft.network.protocol.Packet")).invoke(playerConnection, packet );
            } else {
                Object handle = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
                playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet") ).invoke(playerConnection, packet );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            if (p == null || !p.isOnline()) continue;
            if(Versions.getVersion().esMayorIgual(Versions.v1_16)) {
                p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }else {
                send(title, subtitle, fadeIn , stay ,fadeOut, p);
            }
        }
    }
    private void send(String title, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut, Player player) {
        try{
            Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
            Constructor<?> Constructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
            Object timePacket = Constructor.newInstance(new Object[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null), chat, fadeIn, stay, fadeOut });
            Constructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent") });
            Object titlePacket = Constructor.newInstance(new Object[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chat });
            chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
            Object subtitlePacket = Constructor.newInstance(new Object[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chat });
            sendPacket(player, timePacket);
            sendPacket(player, titlePacket);
            sendPacket(player, subtitlePacket);
        }
        catch (Exception var11){
            var11.printStackTrace();
        }
    }

}