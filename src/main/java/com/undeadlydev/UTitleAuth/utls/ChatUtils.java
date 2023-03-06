package com.undeadlydev.UTitleAuth.utls;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.undeadlydev.UTitleAuth.TitleAuth;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

public class ChatUtils {

	private static final Pattern REPLACE_ALL_PATTERN = Pattern.compile("(&)?&([0-9a-fk-orA-FK-OR])");
	
	private static final Pattern REPLACE_ALL_RGB_PATTERN = Pattern.compile("(&)?&#([0-9a-fA-F]{6})");
	

    public static String colorCodes(String paramString) {
        String coloredText = paramString;
        if(VersionUtils.getVersion().esMayorIgual(VersionUtils.v1_16)) {
            coloredText = parseHex(coloredText, EnumSet.allOf(ChatColor.class), true);
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

    public static String parseHex(String input, Set<ChatColor> supported, boolean rgb) {
        StringBuffer legacyBuilder = new StringBuffer();
        Matcher legacyMatcher = REPLACE_ALL_PATTERN.matcher(input);
        while (legacyMatcher.find()) {
            boolean isEscaped = (legacyMatcher.group(1) != null);
            if (!isEscaped) {
                char code = legacyMatcher.group(2).toLowerCase(Locale.ROOT).charAt(0);
                for (ChatColor color : supported) {
                    if (color.getChar() == code)
                        legacyMatcher.appendReplacement(legacyBuilder, "§$2");
                }
            }
            legacyMatcher.appendReplacement(legacyBuilder, "&$2");
        }
        legacyMatcher.appendTail(legacyBuilder);
        if (rgb) {
            StringBuffer rgbBuilder = new StringBuffer();
            Matcher rgbMatcher = REPLACE_ALL_RGB_PATTERN.matcher(legacyBuilder.toString());
            while (rgbMatcher.find()) {
                boolean isEscaped = (rgbMatcher.group(1) != null);
                if (!isEscaped)
                    try {
                        String hexCode = rgbMatcher.group(2);
                        rgbMatcher.appendReplacement(rgbBuilder, parseHexColor(hexCode));
                        continue;
                    } catch (NumberFormatException numberFormatException) {}
                rgbMatcher.appendReplacement(rgbBuilder, "&#$2");
            }
            rgbMatcher.appendTail(rgbBuilder);
            return rgbBuilder.toString();
        }
        return legacyBuilder.toString();
    }

    public static String parseHexColor(String hexColor) throws NumberFormatException {
        if (!VersionUtils.getVersion().esMayorIgual(VersionUtils.v1_16))
            throw new NumberFormatException("Cannot use RGB colors in versions < 1.16");
        if (hexColor.startsWith("#"))
            hexColor = hexColor.substring(1);
        if (hexColor.length() != 6)
            throw new NumberFormatException("Invalid hex length");
        Color.fromRGB(Integer.decode("#" + hexColor).intValue());
        StringBuilder assembledColorCode = new StringBuilder();
        assembledColorCode.append("§x");
        for (char curChar : hexColor.toCharArray())
            assembledColorCode.append('§').append(curChar);
        return assembledColorCode.toString();
    }
}
