package com.undeadlydev.UTitleAuth;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Sets;
import com.undeadlydev.UTitleAuth.Commands.UTitleAuthCommand;
import com.undeadlydev.UTitleAuth.Listeners.PlayerListeners;
import com.undeadlydev.UTitleAuth.Utils.ChatUtils;
import com.undeadlydev.UTitleAuth.Utils.ConfigUtils;
import com.undeadlydev.UTitleAuth.Utils.ConsoleUtils;
import com.undeadlydev.UTitleAuth.Utils.Metrics;

public class TitleAuth  extends JavaPlugin {

	private static TitleAuth instance;
	public static ConfigUtils cfg;
	
	public final static Set<UUID> SecurePlayerRegister = Sets.newHashSet();
	public final static Set<UUID> SecurePlayerLogin = Sets.newHashSet();
	
    public static JavaPlugin getInt() {
        return (JavaPlugin)instance;
    }
    
    public static ConfigUtils GetCfg() {
        return cfg;
    }
    
    public static FileConfiguration getOtherConfig() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        FileConfiguration config = p.getConfig();
        return config;
    }
    
    public void onEnable() {
        ConsoleUtils.getLoggs("&7-----------------------------------", true);
        super.onEnable();
        instance = this;
        EnableMetrics();
        LoadHooks();
        cfg = new ConfigUtils("Config");
        
        new UTitleAuthCommand(this);
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents((Listener)new PlayerListeners(this), (Plugin)this);
        
        ConsoleUtils.getLoggs(" ", true);
        ConsoleUtils.getLoggs("&7-----------------------------------", true);
        ConsoleUtils.getLoggs(" ", true);
        ConsoleUtils.getLoggs("&fServer: &c" + getServer().getName() + " " + getServer().getVersion() , true);
        ConsoleUtils.getLoggs("&fSuccessfully Plugin &aEnabled! &cv" + getDescription().getVersion(), true);
        ConsoleUtils.getLoggs("&fCreator: &eBrunoAvixdub", true);
        ConsoleUtils.getLoggs("&fThanks for use my plugin :D", true);
        ConsoleUtils.getLoggs(" ", true);
        ConsoleUtils.getLoggs("&7-----------------------------------", true); 
    }
    
    public void LoadHooks() {
        if (Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {
    		ConsoleUtils.getLoggs("&fPlugin &aAuthMe &aHooked Successfully!", true);
    	} else {
    		ConsoleUtils.getError("&fPlugin &cAuthMe &cHooked not found!", true);
	    	Bukkit.getPluginManager().disablePlugin((Plugin)this);
    	}
        if (Bukkit.getPluginManager().isPluginEnabled("FastLogin")) {
        	ChatUtils.FastLogin(true);
    		ConsoleUtils.getLoggs("&fPlugin &aFastLogin &bAutoLogin Premium &aHooked Successfully!", true);
    	} else {
    		ChatUtils.FastLogin(false);
    	}
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            ConsoleUtils.getLoggs("&fPlugin &ePlaceholderAPI &aHooked Successfully!", true);
            ChatUtils.placeholderAPI(true);
        } else {
        	ConsoleUtils.getError("&fPlugin &ePlaceholderAPI &cIs hooked not found!", true);
        	ChatUtils.placeholderAPI(false);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
        	ConsoleUtils.getLoggs("&fPlugin &aCMILib &aHooked Successfully!", true);
        }
    }
    
    private void EnableMetrics() {
        int pluginId = 14756;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
    }
    
    public void onDisable() {
    	ConsoleUtils.getLoggs("&7-----------------------------------", true);
    	ConsoleUtils.getLoggs("", true);
    	ConsoleUtils.getLoggs("&fSuccessfully Plugin &cDisable!", true);
    	ConsoleUtils.getLoggs("&fCreator: &eBrunoAvixdub", true);
    	ConsoleUtils.getLoggs("&fThanks for use my plugin :D", true);
    	ConsoleUtils.getLoggs("", true);
    	ConsoleUtils.getLoggs("&7-----------------------------------", true);
    }
}
