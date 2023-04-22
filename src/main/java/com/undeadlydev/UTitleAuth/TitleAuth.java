package com.undeadlydev.UTitleAuth;

import com.undeadlydev.UTitleAuth.config.Settings;
import com.undeadlydev.UTitleAuth.controllers.VersionController;
import com.undeadlydev.UTitleAuth.managers.AddonManager;
import com.undeadlydev.UTitleAuth.superclass.SpigotUpdater;
import com.undeadlydev.UTitleAuth.utils.ChatUtils;
import com.undeadlydev.UTitleAuth.utils.Utils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.undeadlydev.UTitleAuth.cmds.utitleauthCMD;
import com.undeadlydev.UTitleAuth.listeners.GeneralListeners;
import org.bukkit.scheduler.BukkitRunnable;

public class TitleAuth  extends JavaPlugin {

	private static TitleAuth instance;

    private AddonManager adm;
    private VersionController vc;
    private Settings lang;

    public static TitleAuth get() {
        return instance;
    }

    public AddonManager getAdm() {
        return adm;
    }

    public VersionController getVc() {
        return vc;
    }

    public Settings getLang() {
        return lang;
    }

    public static FileConfiguration getOtherConfig() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        FileConfiguration config = p.getConfig();
        return config;
    }
    
    public void onEnable() {
        instance = this;
        PluginManager pm = getServer().getPluginManager();
        sendLogMessage("&7-----------------------------------");
        loadconfig();
        vc = new VersionController(this);
        lang = new Settings("lang", true, true);
        adm = new AddonManager();
        new utitleauthCMD(this);
        adm.reload();
        LoadHooks();
        pm.registerEvents(new GeneralListeners(this), this);
        loadMetrics();
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
        sendLogMessage(" ");
        sendLogMessage("&fServer: &c" + getServer().getName() + " " + getServer().getBukkitVersion());
        sendLogMessage("&fSuccessfully Plugin &aEnabled! &cv" + getDescription().getVersion());
        sendLogMessage("&fCreator: &eUnDeadlyDev");
        sendLogMessage("&fThanks for use my plugin :D");
        sendLogMessage(" ");
        sendLogMessage("&7-----------------------------------");
        CheckUpdate();
    }

    private  void  loadconfig(){
        saveDefaultConfig();
        reloadConfig();
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
        if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
            Utils.CMILib(true);
            sendLogMessage("&fPlugin &aCMILib &aHooked Successfully!");
        } else {
            Utils.CMILib(false);
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
        Bukkit.getConsoleSender().sendMessage(ChatUtils.parseLegacy("&7[&e&lUTitleAuth&7] &8| " + msg));
    }

    public void loadMetrics() {
        int pluginId = 14756;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie("chart_id", () -> "My value"));
    }

    private void CheckUpdate() {
        if(getConfig().getBoolean("update-check")) {
            new BukkitRunnable() {
                public void run() {
                    SpigotUpdater updater = new SpigotUpdater(instance, 88058);
                    try {
                        if (updater.checkForUpdates()) {
                            sendLogMessage("An update for UTitleAuth (v" + updater.getLatestVersion() + ") is available at:");
                            sendLogMessage(updater.getResourceURL());
                        }
                    } catch (Exception e) {
                        sendLogMessage("Failed to check for a update on spigot.");
                    }
                }

            }.runTask(this);
        }
    }
}
