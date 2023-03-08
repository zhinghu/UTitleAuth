package com.undeadlydev.UTitleAuth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.undeadlydev.UTitleAuth.TitleAuth;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatUtils {

    private static Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]){6}>");

    public static String colorCodes(String paramString) {
        String coloredText = paramString;
        if(VersionUtils.getVersion().esMayorIgual(VersionUtils.v1_16)) {
            coloredText = color(coloredText);
        }
        coloredText = ChatColor.translateAlternateColorCodes('&', coloredText);
        return coloredText;
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

    private static String color(String coloredText) {
        Matcher matcher = HEX_PATTERN.matcher(coloredText);
        while (matcher.find()) {
            ChatColor hexColor = ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
            String before = coloredText.substring(0, matcher.start());
            String after = coloredText.substring(matcher.end());
            coloredText = before + hexColor + after;
            matcher = HEX_PATTERN.matcher(coloredText);

        }
        return coloredText;
    }
}
