package com.github.undeadlydev.UTitleAuth.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.undeadlydev.UTitleAuth.Main;
import com.github.undeadlydev.UTitleAuth.Utils.ActionBarAPI;
import com.github.undeadlydev.UTitleAuth.Utils.ChatUtils;
import com.github.undeadlydev.UTitleAuth.Utils.TitleAPI;
import com.github.undeadlydev.UTitleAuth.Utils.VersionUtils;

import fr.xephi.authme.api.v3.AuthMeApi;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class TitleListeners implements Listener {
	private Main plugin;
	 private final Integer timeleft;

	public TitleListeners(Main plugin) {
		this.plugin = plugin;
		this.timeleft = Main.getOtherConfig().getInt("settings.restrictions.timeout");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void NoRegister(PlayerJoinEvent event) {
		String player = event.getPlayer().getName();
		Player p = event.getPlayer();
		if (!AuthMeApi.getInstance().isRegistered(player)) {
			//NO REGISTER
			SendTitleNoRegister(p);
			Main.SecurePlayerRegister.add(p.getUniqueId());
			this.SendAcNoRegister(p);
		} else {
			//NO LOGIN
			SendTitleNoLogin(p);
	        Main.SecurePlayerLogin.add(p.getUniqueId());
	        SendAcNoLogin(p);
	        
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
	
	private void SendAcNoRegister(Player player) {
        final int[] time = {this.timeleft};
		
		if (VersionUtils.mc1_18 || VersionUtils.mc1_18_1) {
			new BukkitRunnable() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if (!Main.SecurePlayerRegister.contains(player.getUniqueId())) {
			        	this.cancel();
			        	return;
					}
					if (time[0] <= 0) {
						cancel();
	                    return;
					}
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtils.replace(Main.GetCfg().getString("ACTIONBAR.NO_REGISTER.MESSAGE").replace("<time>", String.valueOf(time[0])), player)));
					time[0]--;
				}
			}.runTaskTimer(this.plugin, 0L, 20L);
			
		} else {
			new BukkitRunnable() {

				@Override
				public void run() {
					if (!Main.SecurePlayerRegister.contains(player.getUniqueId())) {
			        	this.cancel();
			        	return;
					}
					if (time[0] <= 0) {
						cancel();
	                    return;
					}
					ActionBarAPI.sendActionBar(player, ChatUtils.replace(Main.GetCfg().getString("ACTIONBAR.NO_REGISTER.MESSAGE").replace("<time>", String.valueOf(time[0])), player), 14, (Plugin)plugin);
					time[0]--;
				}
			}.runTaskTimer(this.plugin, 0L, 20L);
		}	
	}
	
	private void SendAcNoLogin(Player player) {
		final int[] time = {this.timeleft};
		
		if (VersionUtils.mc1_18 || VersionUtils.mc1_18_1) {
			new BukkitRunnable() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if (!Main.SecurePlayerLogin.contains(player.getUniqueId())) {
			        	this.cancel();
			        	return;
					}
					if (time[0] <= 0) {
						cancel();
	                    return;
					}
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtils.replace(Main.GetCfg().getString("ACTIONBAR.NO_LOGIN.MESSAGE").replace("<time>", String.valueOf(time[0])), player)));
					time[0]--;
				}
			}.runTaskTimer(this.plugin, 0L, 20L);			
		} else {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (!Main.SecurePlayerLogin.contains(player.getUniqueId())) {
			        	this.cancel();
			        	return;
					}
					if (time[0] <= 0) {
						cancel();
	                    return;
					}
					ActionBarAPI.sendActionBar(player, ChatUtils.replace(Main.GetCfg().getString("ACTIONBAR.NO_LOGIN.MESSAGE").replace("<time>", String.valueOf(time[0])), player), 14, (Plugin)plugin);
					time[0]--;
				}

			}.runTaskTimer(this.plugin, 0L, 20L);
		}	
	}
	

	
	public static void SendTitleNoRegister(Player player) {

		String Title = ChatUtils.replace(Main.GetCfg().getString("TITLES.NO-REGISTER.TITLE"), player);
		String subTitle = ChatUtils.replace(Main.GetCfg().getString("TITLES.NO-REGISTER.SUBTITLE"), player);		
		
		if (VersionUtils.mc1_18 || VersionUtils.mc1_18_1) {
			player.sendTitle(Title, subTitle, 0, 999999999, 999999999);
			
		} else {
	        
	        int fadeIn = (0);
		    int stay = (999999999);
		    int fadeOut = (20);
		    Title = ChatUtils.replace(Title, player);
		    subTitle = ChatUtils.replace(subTitle, player);
		    TitleAPI.sendTitles(player, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), Title, subTitle);
	        
	        
		}

	}
	
	public static void SendTitleNoLogin(Player player) {
		String Title = ChatUtils.replace(Main.GetCfg().getString("TITLES.NO-LOGIN.TITLE"), player);
		String subTitle = ChatUtils.replace(Main.GetCfg().getString("TITLES.NO-LOGIN.SUBTITLE"), player);
		
		if (VersionUtils.mc1_18 || VersionUtils.mc1_18_1) {
		      player.sendTitle(Title, subTitle, 0, 999999999, 999999999);
		} else {
			int fadeIn = (0);
		    int stay = (999999999);
		    int fadeOut = (20);
		    Title = ChatUtils.replace(Title, player);
		    subTitle = ChatUtils.replace(subTitle, player);
		    TitleAPI.sendTitles(player, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), Title, subTitle);
	        
		}

	}
	
	public static void SendTitleOnRegister(Player player) {
		String Title = ChatUtils.replace(Main.GetCfg().getString("TITLES.ON-REGISTER.TITLE"), player);
		String subTitle = ChatUtils.replace(Main.GetCfg().getString("TITLES.ON-REGISTER.SUBTITLE"), player);
		
		int Fadein = Main.GetCfg().getInt("TITLES.ON-REGISTER.TIME.FADEIN");
        int Stay = Main.GetCfg().getInt("TITLES.ON-REGISTER.TIME.STAY");
        int FadeOut = Main.GetCfg().getInt("TITLES.ON-REGISTER.TIME.FADEOUT");
        if (VersionUtils.mc1_18 || VersionUtils.mc1_18_1) {
		      player.sendTitle(Title, subTitle, Fadein, Stay, FadeOut);
		} else {
		    Title = ChatUtils.replace(Title, player);
		    subTitle = ChatUtils.replace(subTitle, player);
		    TitleAPI.sendTitles(player, Integer.valueOf(Fadein), Integer.valueOf(Stay), Integer.valueOf(FadeOut), Title, subTitle);
		}
	}
	
	public static void SendTitleOnLogin(Player player) {
		String Title = ChatUtils.replace(Main.GetCfg().getString("TITLES.ON-LOGIN.TITLE"), player);
		String subTitle = ChatUtils.replace(Main.GetCfg().getString("TITLES.ON-LOGIN.SUBTITLE"), player);
		
		int Fadein = Main.GetCfg().getInt("TITLES.ON-LOGIN.TIME.FADEIN");
        int Stay = Main.GetCfg().getInt("TITLES.ON-LOGIN.TIME.STAY");
        int FadeOut = Main.GetCfg().getInt("TITLES.ON-LOGIN.TIME.FADEOUT");
        
        if (VersionUtils.mc1_18 || VersionUtils.mc1_18_1) {
		      player.sendTitle(Title, subTitle, Fadein, Stay, FadeOut);
		} else {
		    Title = ChatUtils.replace(Title, player);
		    subTitle = ChatUtils.replace(subTitle, player);
		    TitleAPI.sendTitles(player, Integer.valueOf(Fadein), Integer.valueOf(Stay), Integer.valueOf(FadeOut), Title, subTitle);
		}
	}
}
