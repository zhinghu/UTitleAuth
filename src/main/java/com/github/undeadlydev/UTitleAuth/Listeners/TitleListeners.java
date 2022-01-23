package com.github.undeadlydev.UTitleAuth.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.undeadlydev.UTitleAuth.Main;
import com.github.undeadlydev.UTitleAuth.Utils.ChatUtils;
import com.github.undeadlydev.UTitleAuth.Utils.TitleAPI;

import fr.xephi.authme.api.v3.AuthMeApi;

public class TitleListeners implements Listener {

	public Main plugin;
	
	public TitleListeners(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void NoRegister(PlayerJoinEvent event) {
		String player = event.getPlayer().getName();
		Player p = event.getPlayer();
		if (!AuthMeApi.getInstance().isRegistered(player)) {
			//NO REGISTER
			SendTitleNoRegister(p);
			Main.SecurePlayerRegister.add(p.getUniqueId());
		} else {
			//NO LOGIN
			SendTitleNoLogin(p);
	        Main.SecurePlayerLogin.add(p.getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void OnDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Main.SecurePlayerRegister.contains(p.getUniqueId())) {
			Main.SecurePlayerRegister.remove(p.getUniqueId());
		}
		if (Main.SecurePlayerLogin.contains(p.getUniqueId())) {
			Main.SecurePlayerLogin.remove(p.getUniqueId());
		}
	}
	
	public static void SendTitleNoRegister(Player player) {
		String Title = ChatUtils.replace(Main.GetCfg().getString("TITLES.NO-REGISTER.TITLE"), player);
		String subTitle = ChatUtils.replace(Main.GetCfg().getString("TITLES.NO-REGISTER.SUBTITLE"), player);
		
		if (((Main) Main.getInt()).getServerVersionNumber() > 10) {
		      player.sendTitle(Title, subTitle, 0, 999999999, 999999999);
		} else {
			TitleAPI title = new TitleAPI();
			title.setTitle(Title);
	        title.setSubTitle(subTitle);
	        
	        title.setFadeIn(0);
	        title.setStay(999999999);
	        title.setFadeOut(999999999);
	    
	        title.sendTimes(player);
	        title.sendTitle(player);
	        title.sendSubTitle(player);	
		}

	}
	
	public static void SendTitleNoLogin(Player player) {
		String Title = ChatUtils.replace(Main.GetCfg().getString("TITLES.NO-LOGIN.TITLE"), player);
		String subTitle = ChatUtils.replace(Main.GetCfg().getString("TITLES.NO-LOGIN.SUBTITLE"), player);
		
		if (((Main) Main.getInt()).getServerVersionNumber() > 10) {
		      player.sendTitle(Title, subTitle, 0, 999999999, 999999999);
		} else {
			TitleAPI title = new TitleAPI();
			title.setTitle(Title);
	        title.setSubTitle(subTitle);
	        
	        title.setFadeIn(0);
	        title.setStay(999999999);
	        title.setFadeOut(999999999);
	    
	        title.sendTimes(player);
	        title.sendTitle(player);
	        title.sendSubTitle(player);
		}

	}
	
	public static void SendTitleOnRegister(Player player) {
		String Title = ChatUtils.replace(Main.GetCfg().getString("TITLES.ON-REGISTER.TITLE"), player);
		String subTitle = ChatUtils.replace(Main.GetCfg().getString("TITLES.ON-REGISTER.SUBTITLE"), player);
		
		int Fadein = Main.GetCfg().getInt("TITLES.ON-REGISTER.TIME.FADEIN");
        int Stay = Main.GetCfg().getInt("TITLES.ON-REGISTER.TIME.STAY");
        int FadeOut = Main.GetCfg().getInt("TITLES.ON-REGISTER.TIME.FADEOUT");
		if (((Main) Main.getInt()).getServerVersionNumber() > 10) {
		      player.sendTitle(Title, subTitle, Fadein, Stay, FadeOut);
		} else {
			TitleAPI title = new TitleAPI();
			title.setTitle(Title);
	        title.setSubTitle(subTitle);
	        
	        title.setFadeIn(Fadein);
	        title.setStay(Stay);
	        title.setFadeOut(FadeOut);
	    
	        title.sendTimes(player);
	        title.sendTitle(player);
	        title.sendSubTitle(player);	
		}
	}
	
	public static void SendTitleOnLogin(Player player) {
		String Title = ChatUtils.replace(Main.GetCfg().getString("TITLES.ON-LOGIN.TITLE"), player);
		String subTitle = ChatUtils.replace(Main.GetCfg().getString("TITLES.ON-LOGIN.SUBTITLE"), player);
		
		int Fadein = Main.GetCfg().getInt("TITLES.ON-LOGIN.TIME.FADEIN");
        int Stay = Main.GetCfg().getInt("TITLES.ON-LOGIN.TIME.STAY");
        int FadeOut = Main.GetCfg().getInt("TITLES.ON-LOGIN.TIME.FADEOUT");
		if (((Main) Main.getInt()).getServerVersionNumber() > 10) {
		      player.sendTitle(Title, subTitle, Fadein, Stay, FadeOut);
		} else {
			TitleAPI title = new TitleAPI();
			title.setTitle(Title);
	        title.setSubTitle(subTitle);
	        
	        title.setFadeIn(Fadein);
	        title.setStay(Stay);
	        title.setFadeOut(FadeOut);
	    
	        title.sendTimes(player);
	        title.sendTitle(player);
	        title.sendSubTitle(player);	
		}
	}
}
