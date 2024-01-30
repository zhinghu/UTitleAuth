package com.undeadlydev.UTitleAuth.listeners;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.superclass.SpigotUpdater;
import com.undeadlydev.UTitleAuth.utils.HexUtils;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinEvent implements Listener {

    private TitleAuth plugin;

    public PlayerJoinEvent(TitleAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AuthLoginEvent(org.bukkit.event.player.PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String pl = p.getName();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if (!AuthMeApi.getInstance().isRegistered(pl)) {
                    plugin.getTm().SendTitleNoRegister(p);
                    plugin.addRegisterSecure(p);
                    if (plugin.getConfig().getBoolean("config.actionbar.enabled"))
                        plugin.getAc().SendAcNoRegister(p);
                } else if(!AuthMeApi.getInstance().isAuthenticated(p)) {
                    plugin.getTm().SendTitleNoLogin(p);
                    plugin.addLoginSecure(p);
                    if (plugin.getConfig().getBoolean("config.actionbar.enabled"))
                        plugin.getAc().SendAcNoLogin(p);
                }
            }
        },  20L);
    }

    @EventHandler
    public void PlayerJoinUpdateCheck(org.bukkit.event.player.PlayerJoinEvent e) {
        if (plugin.getConfig().getBoolean("update-check")) {
            final Player p = e.getPlayer();
            if (p.isOp() || p.hasPermission("utitleauth.updatecheck")) {
                new BukkitRunnable() {
                    public void run() {
                        SpigotUpdater updater = new SpigotUpdater(plugin, 88058);
                        try {
                            if (updater.checkForUpdates()) {
                                p.sendMessage(HexUtils.colorify("&bAn update for &fUTitleAuth &e(&fUTitleAuth &fv" + updater.getLatestVersion() + "&e)"));
                                p.sendMessage(HexUtils.colorify("&bis available at &e" + updater.getResourceURL()));
                            }
                        } catch (Exception e) {}
                    }
                }.runTaskAsynchronously(plugin);
            }
        }
    }
}
