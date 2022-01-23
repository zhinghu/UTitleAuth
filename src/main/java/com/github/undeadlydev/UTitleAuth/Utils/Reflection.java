package com.github.undeadlydev.UTitleAuth.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflection {
  public static Class<?> getNMSClass(String name) {
    return getClass("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().substring(23) + "." + name);
  }
  
  public static Class<?> getCraftClass(String name) {
    return getClass("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().substring(23) + "." + name);
  }
  
  public static Class<?> getClass(String name) {
    try {
      return Class.forName(name);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static void sendPacket(Player player, Object packet) {
	    try {
	      Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
	      Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
	      playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	  }
	  
	  public static void sendPacketPos_1_17(Player player, Object packet) {
	    try {
	      Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
	      Object playerConnection = handle.getClass().getField("b").get(handle);
	      Object networkManager = playerConnection.getClass().getField("a").get(playerConnection);
	      networkManager.getClass().getMethod("sendPacket", new Class[] { getClass("net.minecraft.network.protocol.Packet") }).invoke(networkManager, new Object[] { packet });
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	  }
	  
  @SuppressWarnings("deprecation")
public static Object getValue(Object o, String fieldName) {
    try {
      Field field = o.getClass().getDeclaredField(fieldName);
      if (!field.isAccessible())
        field.setAccessible(true); 
      return field.get(o);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  @SuppressWarnings("deprecation")
public static Method getMethod(Object o, String methodName, Class<?>... params) {
    try {
      Method method = o.getClass().getMethod(methodName, params);
      if (!method.isAccessible())
        method.setAccessible(true); 
      return method;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static Field getField(Field field) {
    field.setAccessible(true);
    return field;
  }
}