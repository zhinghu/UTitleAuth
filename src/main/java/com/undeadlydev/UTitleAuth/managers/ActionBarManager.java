package com.undeadlydev.UTitleAuth.managers;

import com.cryptomorin.xseries.messages.ActionBar;
import com.undeadlydev.UTitleAuth.TitleAuth;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ActionBarManager {

    TitleAuth plugin = TitleAuth.get();

    public void SendAcNoRegister(Player player) {
        BukkitTask bukkitTask = (new BukkitRunnable() {
            int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
            public void run() {
                if (!plugin.getRegisterSecure().contains(player.getUniqueId())) {
                    ActionBar.clearActionBar(player);
                    cancel();
                    return;
                }
                if (!plugin.cancelAc().containsKey(player.getName())) {
                    ActionBar.clearActionBar(player);
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                ActionBar.sendActionBar(player, plugin.getLang().get(player, "actionbar.noregister").replace("<time>", String.valueOf(time)));
                time--;
            }
        }).runTaskTimer(plugin, 0L, 20L);
        plugin.cancelAc().put(player.getName(), bukkitTask);
    }

    public void SendAcNoLogin(Player player) {
        BukkitTask bukkitTask = (new BukkitRunnable() {
            int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
            @Override
            public void run() {
                if (!plugin.cancelAc().containsKey(player.getName())) {
                    ActionBar.clearActionBar(player);
                    cancel();
                    return;
                }
                if (!plugin.cancelAc().containsKey(player.getName())) {
                    ActionBar.clearActionBar(player);
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                ActionBar.sendActionBar(player, plugin.getLang().get(player, "actionbar.nologin").replace("<time>", String.valueOf(time)));
                time--;
            }

        }).runTaskTimer(this.plugin, 0L, 20L);
        plugin.cancelAc().put(player.getName(), bukkitTask);
    }

    public void SendAcOnPremium(Player player) {
        String message = plugin.getLang().get(player, "actionbar.autologin");
        int time = plugin.getConfig().getInt("config.actionbar.autologin.time.stay");
        ActionBar.sendActionBar(plugin , player, message, time);
    }

    public void SendAcOnRegister(Player player) {
        String message = plugin.getLang().get(player, "actionbar.register");
        int time = plugin.getConfig().getInt("config.actionbar.register.time.stay");
        ActionBar.sendActionBar(plugin , player, message, time);
    }

    public void SendAcOnLogin(Player player) {
        String message = plugin.getLang().get(player, "actionbar.login");
        int time = plugin.getConfig().getInt("config.actionbar.login.time.stay");
        ActionBar.sendActionBar(plugin , player, message, time);
    }
}
