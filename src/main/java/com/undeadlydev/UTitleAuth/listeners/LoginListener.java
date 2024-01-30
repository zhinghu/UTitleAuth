package com.undeadlydev.UTitleAuth.listeners;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.utils.CenterMessage;
import com.undeadlydev.UTitleAuth.utils.Utils;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginListener implements Listener {

    private TitleAuth plugin;

    public LoginListener(TitleAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnLoginPlayer(LoginEvent event) {
        Player p = event.getPlayer();
        plugin.getLoginSecure().remove(p.getUniqueId());
        if (AuthMeApi.getInstance().isAuthenticated(p)) {
            if (Utils.FastLogin && JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
                plugin.getTm().SendTitlePremium(p);
                if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
                    plugin.cancelac.remove(p.getName());
                    plugin.getAc().SendAcOnPremium(p);
                }
                if (plugin.getConfig().getBoolean("config.message.welcome.autologin.enabled")) {
                    SendWPremium(p);
                }
            } else {
                plugin.getTm().SendTitleOnLogin(p);
                if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
                    plugin.cancelac.remove(p.getName());
                    plugin.getAc().SendAcOnLogin(p);
                }
                if (plugin.getConfig().getBoolean("config.message.welcome.login.enabled")) {
                    SendWOnLogin(p);
                }
            }
        }
    }

    private void SendWOnLogin(Player player) {
        String mesage = plugin.getLang().get(player, "message.welcome.login");
        if (plugin.getConfig().getBoolean("config.message.welcome.login.center")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
        }
    }

    private void SendWPremium(Player player) {
        String mesage = plugin.getLang().get(player, "message.welcome.autologin");
        if (plugin.getConfig().getBoolean("config.message.welcome.autologin.center")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
        }
    }
}
