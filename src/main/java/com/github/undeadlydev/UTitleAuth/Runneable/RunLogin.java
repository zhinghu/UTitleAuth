package com.github.undeadlydev.UTitleAuth.Runneable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.undeadlydev.UTitleAuth.Main;
import com.github.undeadlydev.UTitleAuth.Listeners.TitleListeners;
import fr.xephi.authme.api.v3.AuthMeApi;

public class RunLogin extends BukkitRunnable {

    public RunLogin(Main plugin) {
    }
    
    public void run() {
    	for (Player p : Bukkit.getOnlinePlayers()) {
    		Player pl = p.getPlayer();
    		String player = p.getPlayer().getName();
			if (AuthMeApi.getInstance().isRegistered(player)) {
				if (Main.SecurePlayerRegister.contains(pl.getUniqueId())) {	
        		    TitleListeners.SendTitleOnRegister(pl);
        			Main.SecurePlayerRegister.remove(pl.getUniqueId());
				}
				if (Main.SecurePlayerLogin.contains(pl.getUniqueId())) {
    				if (AuthMeApi.getInstance().isAuthenticated(p)) {
        				TitleListeners.SendTitleOnLogin(pl);
        				Main.SecurePlayerLogin.remove(pl.getUniqueId());	
    				}
				}
			}
    	}
    }
}
