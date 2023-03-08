package com.undeadlydev.UTitleAuth.nms;

import com.undeadlydev.UTitleAuth.superclass.NMSReflection;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

public class NMSReflectionOld extends NMSReflection {

    private static String version;
    private static Class<?> packet;
    private Object enumTimes, enumTitle, enumSubtitle;
    private Constructor<?> packetPlayOutTitle, packetPlayOutTimes, packetPlayOutChat;
    private Method a, position;
    private boolean isNewAction;

    public NMSReflectionOld() {
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            packet = getNMSClass("Packet");
            enumTimes = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
            enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            packetPlayOutTimes = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            packetPlayOutTitle = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
            a = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
            position = getNMSClass("Entity").getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class);
            isNewAction = version.equals("v1_12_R1") || version.equals("v1_13_R2") || version.equals("v1_14_R1") || version.equals("v1_15_R1") || version.equals("v1_16_R1") || version.equals("v1_16_R2") || version.equals("v1_16_R3") || version.equals("v1_17_R1");
            if (!isNewAction) {
                packetPlayOutChat = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
                a = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Player player, Object object) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            connection.getClass().getMethod("sendPacket", packet).invoke(connection, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendActionBar(String msg, Player... players) {
        sendActionBar(msg, Arrays.asList(players));
    }

    public void sendActionBar(String msg, Collection<Player> players) {
        if (isNewAction) {
            BaseComponent[] text = new ComponentBuilder(msg).create();
            for (Player p : players) {
                if (p == null || !p.isOnline())
                    continue;
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
            }
        } else {
            try {
                Object icbc = a.invoke(null, "{\"text\": \"" + msg + "\"}");
                Object packet = packetPlayOutChat.newInstance(icbc, (byte) 2);
                for (Player p : players) {
                    if (p == null || !p.isOnline())
                        continue;
                    sendPacket(p, packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player... players) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, Arrays.asList(players));
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Collection<Player> players) {
        try {
            Object titleC = a.invoke(null, "{\"text\": \"" + title + "\"}");
            Object subtitleC = a.invoke(null, "{\"text\": \"" + subtitle + "\"}");
            Object timesPacket = packetPlayOutTimes.newInstance(enumTimes, null, fadeIn, stay, fadeOut);
            Object titlePacket = packetPlayOutTitle.newInstance(enumTitle, titleC);
            Object subtitlePacket = packetPlayOutTitle.newInstance(enumSubtitle, subtitleC);
            for (Player p : players) {
                if (p == null || !p.isOnline()) continue;
                sendPacket(p, timesPacket);
                sendPacket(p, titlePacket);
                sendPacket(p, subtitlePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}