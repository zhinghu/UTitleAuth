package com.undeadlydev.UTitleAuth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.undeadlydev.UTitleAuth.TitleAuth;


public class GeneralListeners implements Listener {

	private TitleAuth plugin;

	public GeneralListeners(TitleAuth plugin) {
		this.plugin = plugin;
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		remove(p);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		remove(p);
	}

	private void remove(Player p) {
		if (plugin.getRegisterSecure().contains(p.getUniqueId())) {
			plugin.getRegisterSecure().remove(p.getUniqueId());
		}
		if (plugin.getLoginSecure().contains(p.getUniqueId())) {
			plugin.getLoginSecure().remove(p.getUniqueId());
		}
		if (plugin.cancelac.containsKey(p.getName())){
			plugin.cancelac.remove(p.getName());
		}
	}
}
