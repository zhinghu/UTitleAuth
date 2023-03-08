package com.undeadlydev.UTitleAuth.superclass;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class NMSReflection {

    private final static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static Class<?> getNMSClass(String name) {
        try {
            return getClass("net.minecraft.server." + version + "." + name);
        } catch (Exception var3) {
            return null;
        }
    }

    public static Class<?> getNMSClassArray(String name)
    {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return getClass("[Lnet.minecraft.server." + version + "." + name+";");

    }

    public static Class<?> getOBClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (Exception var3) {
            return null;
        }
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void sendPacket(Player player, Object object);

    public abstract void sendActionBar(String msg, Player... players);

    public abstract void sendActionBar(String msg, Collection<Player> players);

    public abstract void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player... players);

    public abstract void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Collection<Player> players);
}