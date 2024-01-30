package com.undeadlydev.UTitleAuth.listeners;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.utils.CenterMessage;
import fr.xephi.authme.events.RegisterEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegisterListener implements Listener {

    private TitleAuth plugin;

    public RegisterListener(TitleAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnRegisterPlayer(RegisterEvent event) {
        Player p = event.getPlayer();
        plugin.getRegisterSecure().remove(p.getUniqueId());
        if (TitleAuth.getOtherConfig().getBoolean("settings.registration.forceLoginAfterRegister")) {
            SendNoLogin(p);
            return;
        }
        plugin.getTm().SendTitleOnRegister(p);
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.cancelac.remove(p.getName());
            plugin.getAc().SendAcOnRegister(p);
        }
        if (plugin.getConfig().getBoolean("config.message.welcome.register.enabled")) {
            SendWOnRegister(p);
        }
    }

    private void SendNoLogin(Player p) {
        plugin.getTm().SendTitleNoLogin(p);
        plugin.addLoginSecure(p.getUniqueId());
        if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
            plugin.getAc().SendAcNoLogin(p);
        }
    }

    private void SendWOnRegister(Player player) {
        String mesage = plugin.getLang().get(player, "message.welcome.register");
        if (plugin.getConfig().getBoolean("config.message.welcome.register.center")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
        }
    }
}
