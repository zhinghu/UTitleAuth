package com.undeadlydev.UTitleAuth.listeners;

import com.undeadlydev.UTitleAuth.TitleAuth;
import fr.xephi.authme.events.UnregisterByPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UnregisterListener implements Listener {

    private TitleAuth plugin;

    public UnregisterListener(TitleAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void UnRegisterByPlayer(UnregisterByPlayerEvent event) {
        Player p = event.getPlayer();
        plugin.getTm().SendTitleNoRegister(p);
        plugin.addRegisterSecure(p);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAc().SendAcNoRegister(p);
        }
    }
}
