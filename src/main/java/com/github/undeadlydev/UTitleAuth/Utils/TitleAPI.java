package com.github.undeadlydev.UTitleAuth.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TitleAPI {
  private static Class<?> titlePacket;
  
  private static Class<?> enumTitleAction;
  
  private static Class<?> chatSerializer;
  
  private static Class<?> craftPlayer;
  
  private static Class<?> packet;
  
  private static Method getHandle;
  
  private static boolean invoked;
  
  private String title;
  
  private String subTitle;
  
  private int fadeIn;
  
  private int stay;
  
  private int fadeOut;
  
  private static int versionId = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].replace(".", "#").split("#")[1]);
  
  public TitleAPI() {
    if (!invoked) {
      try {
        packet = Reflection.getNMSClass("Packet");
        craftPlayer = Reflection.getCraftClass("entity.CraftPlayer");
        getHandle = craftPlayer.getDeclaredMethod("getHandle", new Class[0]);
        titlePacket = Reflection.getNMSClass("PacketPlayOutTitle");
        enumTitleAction = Reflection.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
        chatSerializer = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");
      } catch (Exception e) {
        e.printStackTrace();
      } 
      invoked = true;
    } 
  }
  
  public TitleAPI setTitle(String title) {
    this.title = title;
    return this;
  }
  
  public TitleAPI setSubTitle(String subTitle) {
    this.subTitle = subTitle;
    return this;
  }
  
  public TitleAPI setFadeIn(int fadeIn) {
    this.fadeIn = fadeIn;
    return this;
  }
  
  public TitleAPI setStay(int stay) {
    this.stay = stay;
    return this;
  }
  
  public TitleAPI setFadeOut(int fadeOut) {
    this.fadeOut = fadeOut;
    return this;
  }
  
  public TitleAPI sendClear(Player... players) {
    try {
      for (Player player : players) {
        @SuppressWarnings("deprecation")
		Object titleObject = titlePacket.newInstance();
        Field field = titleObject.getClass().getDeclaredField("a");
        field.setAccessible(true);
        field.set(titleObject, Reflection.getField(enumTitleAction.getDeclaredField("CLEAR")).get((Object)null));
        Object handle = getHandle.invoke(player, new Object[0]);
        Object connection = Reflection.getValue(handle, "playerConnection");
        Method send = Reflection.getMethod(connection, "sendPacket", new Class[] { packet });
        send.invoke(connection, new Object[] { titleObject });
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return this;
  }
  
  public TitleAPI sendReset(Player... players) {
    try {
      for (Player player : players) {
        @SuppressWarnings("deprecation")
		Object titleObject = titlePacket.newInstance();
        Field field = titleObject.getClass().getDeclaredField("a");
        field.setAccessible(true);
        field.set(titleObject, Reflection.getField(enumTitleAction.getDeclaredField("RESET")).get((Object)null));
        Object handle = getHandle.invoke(player, new Object[0]);
        Object connection = Reflection.getValue(handle, "playerConnection");
        Method send = Reflection.getMethod(connection, "sendPacket", new Class[] { packet });
        send.invoke(connection, new Object[] { titleObject });
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return this;
  }
  
  public TitleAPI sendTitle(Player... players) {
    try {
      for (Player player : players) {
    	title = ChatColor.translateAlternateColorCodes('&', title);
        @SuppressWarnings("deprecation")
		Object titleObject = titlePacket.newInstance();
        Field field = titleObject.getClass().getDeclaredField("a");
        field.setAccessible(true);
        field.set(titleObject, Reflection.getField(enumTitleAction.getDeclaredField("TITLE")).get((Object)null));
        field = titleObject.getClass().getDeclaredField("b");
        field.setAccessible(true);
        if (versionId >= 9) {
          field.set(titleObject, chatSerializer.getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\": \"" + this.title + '"' + "}" }));
        } else {
          field.set(titleObject, chatSerializer.getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + this.title + "'}" }));
        } 
        Object handle = getHandle.invoke(player, new Object[0]);
        Object connection = Reflection.getValue(handle, "playerConnection");
        Method send = Reflection.getMethod(connection, "sendPacket", new Class[] { packet });
        send.invoke(connection, new Object[] { titleObject });
      } 
    } catch (Exception e) {
      try {
        for (Player player : players) {
          @SuppressWarnings("deprecation")
		Object titleObject = titlePacket.newInstance();
          Field field = titleObject.getClass().getDeclaredField("a");
          field.setAccessible(true);
          field.set(titleObject, Reflection.getField(enumTitleAction.getDeclaredField("TITLE")).get((Object)null));
          field = titleObject.getClass().getDeclaredField("b");
          field.setAccessible(true);
          if (versionId >= 9) {
            field.set(titleObject, chatSerializer.getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + this.title + "'}" }));
          } else {
            field.set(titleObject, chatSerializer.getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\": \"" + this.title + '"' + "}" }));
          } 
          Object handle = getHandle.invoke(player, new Object[0]);
          Object connection = Reflection.getValue(handle, "playerConnection");
          Method send = Reflection.getMethod(connection, "sendPacket", new Class[] { packet });
          send.invoke(connection, new Object[] { titleObject });
        } 
      } catch (Exception ex) {
        e.printStackTrace();
      } 
    } 
    return this;
  }
  
  public TitleAPI sendSubTitle(Player... players) {
    try {
      for (Player player : players) {
    	subTitle = ChatColor.translateAlternateColorCodes('&', subTitle);
        @SuppressWarnings("deprecation")
		Object titleObject = titlePacket.newInstance();
        Field field = titleObject.getClass().getDeclaredField("a");
        field.setAccessible(true);
        field.set(titleObject, Reflection.getField(enumTitleAction.getDeclaredField("SUBTITLE")).get((Object)null));
        field = titleObject.getClass().getDeclaredField("b");
        field.setAccessible(true);
        if (versionId >= 9) {
          field.set(titleObject, chatSerializer.getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\": \"" + this.subTitle + '"' + "}" }));
        } else {
          field.set(titleObject, chatSerializer.getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + this.subTitle + "'}" }));
        } 
        Object handle = getHandle.invoke(player, new Object[0]);
        Object connection = Reflection.getValue(handle, "playerConnection");
        Method send = Reflection.getMethod(connection, "sendPacket", new Class[] { packet });
        send.invoke(connection, new Object[] { titleObject });
      } 
    } catch (Exception e) {
      try {
        for (Player player : players) {
          @SuppressWarnings("deprecation")
		Object titleObject = titlePacket.newInstance();
          Field field = titleObject.getClass().getDeclaredField("a");
          field.setAccessible(true);
          field.set(titleObject, Reflection.getField(enumTitleAction.getDeclaredField("SUBTITLE")).get((Object)null));
          field = titleObject.getClass().getDeclaredField("b");
          field.setAccessible(true);
          if (versionId >= 9) {
            field.set(titleObject, chatSerializer.getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + this.subTitle + "'}" }));
          } else {
            field.set(titleObject, chatSerializer.getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\": \"" + this.subTitle + '"' + "}" }));
          } 
          Object handle = getHandle.invoke(player, new Object[0]);
          Object connection = Reflection.getValue(handle, "playerConnection");
          Method send = Reflection.getMethod(connection, "sendPacket", new Class[] { packet });
          send.invoke(connection, new Object[] { titleObject });
        } 
      } catch (Exception ex) {
        e.printStackTrace();
      } 
    } 
    return this;
  }
  
  public TitleAPI sendTimes(Player... players) {
    try {
      for (Player player : players) {
        @SuppressWarnings("deprecation")
		Object titleObject = titlePacket.newInstance();
        Field field = titleObject.getClass().getDeclaredField("a");
        field.setAccessible(true);
        field.set(titleObject, Reflection.getField(enumTitleAction.getDeclaredField("TIMES")).get((Object)null));
        field = titleObject.getClass().getDeclaredField("c");
        field.setAccessible(true);
        field.set(titleObject, Integer.valueOf(this.fadeIn));
        field = titleObject.getClass().getDeclaredField("d");
        field.setAccessible(true);
        field.set(titleObject, Integer.valueOf(this.stay));
        field = titleObject.getClass().getDeclaredField("e");
        field.setAccessible(true);
        field.set(titleObject, Integer.valueOf(this.fadeOut));
        Object handle = getHandle.invoke(player, new Object[0]);
        Object connection = Reflection.getValue(handle, "playerConnection");
        Method send = Reflection.getMethod(connection, "sendPacket", new Class[] { packet });
        send.invoke(connection, new Object[] { titleObject });
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return this;
  }
}