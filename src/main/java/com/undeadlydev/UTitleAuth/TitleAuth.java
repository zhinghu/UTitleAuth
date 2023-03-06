package com.undeadlydev.UTitleAuth;

import com.undeadlydev.UTitleAuth.config.Settings;
import com.undeadlydev.UTitleAuth.controllers.VersionController;
import com.undeadlydev.UTitleAuth.managers.AddonManager;
import com.undeadlydev.UTitleAuth.utls.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.undeadlydev.UTitleAuth.cmds.utitleauthCMD;
import com.undeadlydev.UTitleAuth.listeners.PlayerListeners;
import com.undeadlydev.UTitleAuth.utls.ChatUtils;
import com.undeadlydev.UTitleAuth.data.Metrics;

public class TitleAuth  extends JavaPlugin {

	private static TitleAuth instance;

    private AddonManager adm;
    private VersionController vc;
    private Settings cfg;

    public static TitleAuth get() {
        return instance;
    }

    public AddonManager getAdm() {
        return adm;
    }

    public VersionController getVc() {
        return vc;
    }

    public Settings getCfg() {
        return cfg;
    }
    public static FileConfiguration getOtherConfig() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        FileConfiguration config = p.getConfig();
        return config;
    }
    
    public void onEnable() {
        instance = this;
        vc = new VersionController(this);
        cfg = new Settings("Config", true, false);
        adm = new AddonManager();
        new utitleauthCMD(this);
        adm.reload();
        LoadHooks();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListeners(this), this);
        EnableMetrics();
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
        sendLogMessage(" ");
        sendLogMessage("&fServer: &c" + getServer().getName() + " ");
        sendLogMessage("&fSuccessfully Plugin &aEnabled! &cv" + getDescription().getVersion());
        sendLogMessage("&fCreator: &eUnDeadlyDev");
        sendLogMessage("&fThanks for use my plugin :D");
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
    }
    
    public void LoadHooks() {
        if (Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {
            sendLogMessage("&fPlugin &aAuthMe &aHooked Successfully!");
    	} else {
            sendLogMessage("&fPlugin &cAuthMe &cHooked not found!");
	    	Bukkit.getPluginManager().disablePlugin(this);
    	}
        if (Bukkit.getPluginManager().isPluginEnabled("FastLogin")) {
        	Utils.FastLogin(true);
            sendLogMessage("&fPlugin &aFastLogin &bAutoLogin Premium &aHooked Successfully!");
    	} else {
    		Utils.FastLogin(false);
    	}
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            sendLogMessage("&fPlugin &ePlaceholderAPI &aHooked Successfully!");
        } else {
            sendLogMessage("&fPlugin &ePlaceholderAPI &cIs hooked not found!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
            sendLogMessage("&fPlugin &aCMILib &aHooked Successfully!");
        }
    }
    
    public void onDisable() {
        sendLogMessage("&7-----------------------------------");
        sendLogMessage("");
        sendLogMessage("&fSuccessfully Plugin &cDisable!");
        sendLogMessage("&fCreator: &eUnDeadlyDev");
        sendLogMessage("&fThanks for use my plugin :D");
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
    }

    public void sendLogMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&e&lUTitleAuth&7] &8| " + msg));
    }

    private void EnableMetrics() {
        int pluginId = 14756;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
    }
}
