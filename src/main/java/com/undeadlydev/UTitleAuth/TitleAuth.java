package com.undeadlydev.UTitleAuth;

import com.google.common.collect.Sets;
import com.undeadlydev.UTitleAuth.managers.FileManager;
import com.undeadlydev.UTitleAuth.controllers.VersionController;
import com.undeadlydev.UTitleAuth.listeners.*;
import com.undeadlydev.UTitleAuth.managers.ActionBarManager;
import com.undeadlydev.UTitleAuth.managers.AddonManager;
import com.undeadlydev.UTitleAuth.managers.TitlesManager;
import com.undeadlydev.UTitleAuth.superclass.SpigotUpdater;
import com.undeadlydev.UTitleAuth.utils.ChatUtils;
import com.undeadlydev.UTitleAuth.utils.HexUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.undeadlydev.UTitleAuth.cmds.utitleauthCMD;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TitleAuth  extends JavaPlugin {

	private static TitleAuth instance;

    private AddonManager adm;
    private TitlesManager tm;
    private ActionBarManager ac;
    private VersionController vc;
    private FileManager lang;

    private Set<UUID> SecurePlayerRegister = Sets.newHashSet();
    private Set<UUID> SecurePlayerLogin = Sets.newHashSet();
    private Map<String, BukkitTask> cancelac = new HashMap<>();

    public static TitleAuth get() {
        return instance;
    }

    public AddonManager getAdm() {
        return adm;
    }

    public VersionController getVc() {
        return vc;
    }

    public FileManager getLang() {
        return lang;
    }

    public Set<UUID> getRegisterSecure() {
        return SecurePlayerRegister;
    }

    public void addRegisterSecure(Player uuid){
        this.SecurePlayerRegister.add(uuid.getUniqueId());
    }

    public Set<UUID> getLoginSecure() {
        return SecurePlayerLogin;
    }

    public void addLoginSecure(Player uuid){
        this.SecurePlayerLogin.add(uuid.getUniqueId());
    }

    public TitlesManager getTm() {
        return tm;
    }

    public ActionBarManager getAc() {
        return ac;
    }

    public Map<String, BukkitTask> cancelAc() {
        return cancelac;
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
        getConfig().options().copyDefaults(true);
        saveConfig();
        vc = new VersionController(this);
        lang = new FileManager("lang", true);
        adm = new AddonManager();
        tm = new TitlesManager();
        ac = new ActionBarManager();
        new utitleauthCMD(this);
        adm.reload();
        loadHooks();
        pm.registerEvents(new GeneralListeners(this), this);
        pm.registerEvents(new LoginListener(this), this);
        pm.registerEvents(new LogoutListener(this), this);
        pm.registerEvents(new PlayerJoinEvent(this), this);
        pm.registerEvents(new RegisterListener(this), this);
        pm.registerEvents(new UnregisterbyAdminListener(this), this);
        pm.registerEvents(new UnregisterListener(this), this);
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

    public void loadHooks() {
        if (Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {
            sendLogMessage("&fPlugin &aAuthMe &aHooked Successfully!");
    	} else {
            sendLogMessage("&fPlugin &cAuthMe &cHooked not found!");
	    	Bukkit.getPluginManager().disablePlugin(this);
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
