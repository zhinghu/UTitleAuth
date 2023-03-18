package com.undeadlydev.UTitleAuth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.undeadlydev.UTitleAuth.TitleAuth;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatUtils {

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("[<](#[0-9A-Fa-f]{6}+)[>]");

    public static String colorCodes(String paramString) {
        if (paramString == null)
            return null;
        if (VersionUtils.getVersion().esMayorIgual(VersionUtils.v1_16) && HEX_COLOR_PATTERN.matcher(paramString).find()) {
            String str1 = null;
            String str2 = null;
            for (Matcher matcher = HEX_COLOR_PATTERN.matcher(paramString); matcher.find(); paramString = paramString.replace(str1, str2)) {
                str1 = matcher.group();
                StringBuilder stringBuilder = new StringBuilder("§x");
                for (char c : str1.substring(2, matcher.group().length() - 1).toUpperCase().toCharArray())
                    stringBuilder.append('§').append(c);
                str2 = stringBuilder.toString();
            }
        }
        return parseLegacy(paramString);
    }

    public static String replace(String string, Player p) {
        TitleAuth plugin = TitleAuth.get();
        if (string == null)
            return "";
        String newString = string;
        if (newString.contains("{player}"))
            newString = newString.replace("{player}", p.getName());
        if (newString.contains("{online}"))
            newString = newString.replace("{online}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));

        newString = plugin.getAdm().parsePlaceholders(p, newString);
        newString = colorCodes(newString);
        return newString;
    }

    public static String parseLegacy(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
