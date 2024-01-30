package com.undeadlydev.UTitleAuth.managers;

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
                if (!plugin.cancelAc().containsKey(player.getName())) {
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                plugin.getVc().getReflection().sendActionBar(plugin.getLang().get(player, "actionbar.noregister").replace("<time>", String.valueOf(time)), player);
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
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                plugin.getVc().getReflection().sendActionBar(plugin.getLang().get(player, "actionbar.nologin").replace("<time>", String.valueOf(time)), player);
                time--;
            }

        }).runTaskTimer(this.plugin, 0L, 20L);
        plugin.cancelAc().put(player.getName(), bukkitTask);
    }

    public void SendAcOnPremium(Player player) {
        String message = plugin.getLang().get(player, "actionbar.autologin");
        new BukkitRunnable() {
            int time = plugin.getConfig().getInt("config.actionbar.autologin.time.stay");
            @Override
            public void run() {
                if (plugin.getLoginSecure().contains(player.getUniqueId())) {
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                plugin.getVc().getReflection().sendActionBar(message, player);
                time--;
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }

    public void SendAcOnRegister(Player player) {
        String message = plugin.getLang().get(player, "actionbar.register");
        new BukkitRunnable() {
            int time = plugin.getConfig().getInt("config.actionbar.register.time.stay");
            @Override
            public void run() {
                if (AuthMeApi.getInstance().isRegistered(player.getName()) && AuthMeApi.getInstance().isAuthenticated(player.getPlayer()) || plugin.getRegisterSecure().contains(player.getUniqueId())) {
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                plugin.getVc().getReflection().sendActionBar(message, player);
                time--;
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }

    public void SendAcOnLogin(Player player) {
        String message = plugin.getLang().get(player, "actionbar.login");
        new BukkitRunnable() {
            int time = plugin.getConfig().getInt("config.actionbar.login.time.stay");
            @Override
            public void run() {
                if (plugin.getLoginSecure().contains(player.getUniqueId())) {
                    cancel();
                    return;
                }
                if (time <= 0) {
                    cancel();
                    return;
                }
                plugin.getVc().getReflection().sendActionBar(message, player);
                time--;
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }
}
