package com.undeadlydev.UTitleAuth.managers;


import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.addons.placeholders.PlaceholderAPIAddon;
import com.undeadlydev.UTitleAuth.interfaces.PlaceholderAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AddonManager {

    private PlaceholderAddon placeholder;

    public boolean check(String pluginName) {
        TitleAuth plugin = TitleAuth.get();
        if (plugin.getConfig().isSet("addons." + pluginName) && plugin.getConfig().getBoolean("addons." + pluginName)) {
            if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
                plugin.sendLogMessage("Hooked into §a" + pluginName + "§e!");
                return true;
            } else {
                plugin.getConfig().set("addons." + pluginName, false);
                plugin.saveConfig();
                return false;
            }
        }
        return false;
    }

    public void reload() {
        TitleAuth plugin = TitleAuth.get();
        if (check("PlaceholderAPI")) {
            placeholder = new PlaceholderAPIAddon();
        }
    }

    public String parsePlaceholders(Player p, String value) {
        if (placeholder != null) {
            value = placeholder.parsePlaceholders(p, value);
        }
        return value;
    }
}