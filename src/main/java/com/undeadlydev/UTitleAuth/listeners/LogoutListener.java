package com.undeadlydev.UTitleAuth.listeners;

import com.undeadlydev.UTitleAuth.TitleAuth;
import fr.xephi.authme.events.LogoutEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LogoutListener implements Listener {

    private TitleAuth plugin;

    public LogoutListener(TitleAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnLogoutPlayer(LogoutEvent event) {
        Player p = event.getPlayer();
        plugin.addLoginSecure(p);
        plugin.getTm().SendTitleNoLogin(p);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAc().SendAcNoLogin(p);
        }
    }
}
