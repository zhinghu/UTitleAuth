package com.undeadlydev.UTitleAuth.Utils;

import org.bukkit.Bukkit;

public class ConsoleUtils {
    public static void getLoggs(String paramString, boolean paramBoolean) {
	    if (paramBoolean) {
	        Bukkit.getConsoleSender().sendMessage(ChatUtils.colorCodes("&7[&eUTitleAuth&7] &r" + paramString));
		} else if (!paramBoolean) {
		    Bukkit.getConsoleSender().sendMessage(ChatUtils.colorCodes(paramString));
		}
	}

    public static void getError(String paramString, boolean paramBoolean) {
	    if (paramBoolean) {
	        Bukkit.getConsoleSender().sendMessage(ChatUtils.colorCodes("&c[ERROR] &r" + paramString));
		} else if (!paramBoolean) {
		    Bukkit.getConsoleSender().sendMessage(ChatUtils.colorCodes(paramString));
		}
	}
}
