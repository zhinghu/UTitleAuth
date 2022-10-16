package com.github.undeadlydev.UTitleAuth.Utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class ChatUtils {

	private static final Pattern REPLACE_ALL_PATTERN = Pattern.compile("(&)?&([0-9a-fk-orA-FK-OR])");
	
	private static final Pattern REPLACE_ALL_RGB_PATTERN = Pattern.compile("(&)?&#([0-9a-fA-F]{6})");
	
	private static boolean placeholderAPI = false;
	
	public static void placeholderAPI(boolean api) {
		placeholderAPI = api;
	}
	
	public static boolean FastLogin = false;
	
	public static void FastLogin(boolean bol) {
		FastLogin = bol;
	}
	
    public static String colorCodes(String nonColoredText) {
        if (nonColoredText == null)
            return nonColoredText; 
        if (nonColoredText.isEmpty())
            return nonColoredText; 
        if (nonColoredText.length() <= 0)
            return nonColoredText; 
        if (nonColoredText == "")
            return nonColoredText; 
        if (nonColoredText == " ")
            return nonColoredText; 
        String coloredText = ChatColor.translateAlternateColorCodes('&', nonColoredText);
        return coloredText;
    }
    
    public static String replaceUcode(String string) {
	String newString = string;
    while (newString.contains("<ucode")) {
        String code = newString.split("<ucode")[1].split(">")[0];
		newString = newString.replaceAll("<ucode" + code + ">", 
		String.valueOf((char)Integer.parseInt(code, 16)));
    } 
    if (newString.contains("<a>"))
        newString = newString.replaceAll("<a>", "á"); 
	if (newString.contains("<e>"))
		newString = newString.replaceAll("<e>", "é"); 
	if (newString.contains("<i>"))
		newString = newString.replaceAll("<i>", "í"); 
	if (newString.contains("<o>"))
		newString = newString.replaceAll("<o>", "ó"); 
	if (newString.contains("<u>"))
        newString = newString.replaceAll("<u>", "ú");
    return newString;
    }
    
    public static String papicolor(String string, Player p) {
	    if (string == null)
		    return ""; 
		String newString = string;
		if (newString.contains("<player>"))
		      newString = newString.replaceAll("<player>", p.getName()); 
		
		if (placeholderAPI)
		    try {
		        newString = PlaceholderAPI.setPlaceholders(p, newString);
		    } catch (Exception exception) {}
		    newString = colorCodes(newString);
		    return newString;
    }
    
    public static String replaceXColor(String input, Player p) {
        if (input == null)
            return null; 
        return replaceColor(papicolor(input, p), EnumSet.allOf(ChatColor.class), true);
    }
    
    static String replaceColor(String input, Set<ChatColor> supported, boolean rgb) {
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
        if (!VersionUtils.isNewVersion())
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
    
	public static List<String> replaceList(List<String> list, OfflinePlayer p) {
	    List<String> localList = new ArrayList<>();
		localList.addAll(list);
		for (int i = 0; i < localList.size(); i++)
		    localList.set(i, papicolor(localList.get(i), (Player) p)); 
		    return localList;
    }
	
	public static List<String> replaceList(List<String> paramList) {
        ArrayList<String> arrayList = new ArrayList<>();
		arrayList.addAll(paramList);
		for (byte b = 0; b < arrayList.size(); b++) {
		    String str = colorCodes(arrayList.get(b));
		    arrayList.set(b, str);
		} 
		return arrayList;
	}
}
