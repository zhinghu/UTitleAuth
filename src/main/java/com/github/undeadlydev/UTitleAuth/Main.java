package com.github.undeadlydev.UTitleAuth;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.undeadlydev.UTitleAuth.Listeners.TitleListeners;
import com.github.undeadlydev.UTitleAuth.Runneable.RunUtils;
import com.github.undeadlydev.UTitleAuth.Utils.ConfigUtils;
import com.github.undeadlydev.UTitleAuth.Utils.ConsoleUtils;
import com.google.common.collect.Sets;

public class Main  extends JavaPlugin{

	private static Main instance;
	
	public final static Set<UUID> SecurePlayerRegister = Sets.newHashSet();
	
	public final static Set<UUID> SecurePlayerLogin = Sets.newHashSet();
	
    public static JavaPlugin getInt() {
        return (JavaPlugin)instance;
    }
    public RunUtils titlerunutils;
    
    public static ConfigUtils cfg;
    
    public static ConfigUtils GetCfg() {
        return cfg;
    }
    
    public static int SERVER_VERSION;
    
    public void onEnable() {
    	SERVER_VERSION = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].replace(".", "#").split("#")[1]);
        if (SERVER_VERSION > 15)
        ConsoleUtils.getLoggs("&7-----------------------------------", true);
        if (Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {
    		ConsoleUtils.getLoggs("&fPlugin &aAuthMe &aHooked Successfully!", true);
    	} else {
    		ConsoleUtils.getError("&fPlugin &cAuthMe &cHooked not found!", true);
	    	Bukkit.getPluginManager().disablePlugin((Plugin)this);
    	}
    	
    	ConsoleUtils.getLoggs(" ", true);
        super.onEnable();
        instance = this;
        
        cfg = new ConfigUtils("Config");
        
        this.titlerunutils = new RunUtils(this);
        
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents((Listener)new TitleListeners(this), (Plugin)this);
        
        ConsoleUtils.getLoggs(" ", true);
        ConsoleUtils.getLoggs("&7-----------------------------------", true);
        ConsoleUtils.getLoggs(" ", true);
        ConsoleUtils.getLoggs("&fServer: &c" + getServer().getVersion() + " " , true);
        ConsoleUtils.getLoggs("&fSuccessfully Plugin &aEnabled! &cv" + getDescription().getVersion(), true);
        ConsoleUtils.getLoggs("&fCreator: &eBrunoAvixdub", true);
        ConsoleUtils.getLoggs("&fThanks for use my plugin :D", true);
        ConsoleUtils.getLoggs(" ", true);
        ConsoleUtils.getLoggs("&7-----------------------------------", true); 
    }
      
    public int getServerVersionNumber() {
        return SERVER_VERSION;
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
