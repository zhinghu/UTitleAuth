package com.undeadlydev.UTitleAuth.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.Utils.ActionBarAPI;
import com.undeadlydev.UTitleAuth.Utils.ChatUtils;
import com.undeadlydev.UTitleAuth.Utils.TitleAPI;
import com.undeadlydev.UTitleAuth.Utils.VersionUtils;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import fr.xephi.authme.events.RegisterEvent;
import fr.xephi.authme.events.UnregisterByAdminEvent;
import fr.xephi.authme.events.UnregisterByPlayerEvent;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerListeners implements Listener {
	private TitleAuth plugin;
    
	public PlayerListeners(TitleAuth plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
    public void UnRegisterByPlayer(UnregisterByPlayerEvent event) {
    	Player p = event.getPlayer();
    	SendTitleNoRegister(p);
		TitleAuth.SecurePlayerRegister.add(p.getUniqueId());
		if (TitleAuth.GetCfg().getBoolean("ACTIONBAR.Enable")) {
		    SendAcNoRegister(p); 
		}
    }
	
	@EventHandler
    public void UnRegisterByAdmin(UnregisterByAdminEvent event) {
    	Player p = event.getPlayer();
    	SendTitleNoRegister(p);
		TitleAuth.SecurePlayerRegister.add(p.getUniqueId());
		if (TitleAuth.GetCfg().getBoolean("ACTIONBAR.Enable")) {
		    SendAcNoRegister(p); 
		}
    }
	
	@EventHandler
    public void OnRegisterPlayer(RegisterEvent event) {
		Player p = event.getPlayer();
		TitleAuth.SecurePlayerRegister.remove(p.getUniqueId());
		SendTitleOnRegister(p);
		if (TitleAuth.GetCfg().getBoolean("ACTIONBAR.Enable")) {
		    SendAcOnRegister(p);
		}
	}
	
	@EventHandler
    public void OnLoginPlayer(LoginEvent event) {
		Player p = event.getPlayer();
		TitleAuth.SecurePlayerLogin.remove(p.getUniqueId());
		if (ChatUtils.FastLogin && JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
			SendTitlePremium(p);
			if (TitleAuth.GetCfg().getBoolean("ACTIONBAR.Enable")) {
				SendAcOnPremium(p);
			}
		} else {
			SendTitleOnLogin(p);
			if (TitleAuth.GetCfg().getBoolean("ACTIONBAR.Enable")) {
				SendAcOnLogin(p);
			}	
		}
	}
	
	@EventHandler
    public void OnLogoutPlayer(LogoutEvent event) {
		Player p = event.getPlayer();
		TitleAuth.SecurePlayerLogin.add(p.getUniqueId());
		SendTitleNoLogin(p);
		if (TitleAuth.GetCfg().getBoolean("ACTIONBAR.Enable")) {
			SendAcNoLogin(p);
		}
	}
    
	@EventHandler(priority = EventPriority.HIGHEST)
    public void OnDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (TitleAuth.SecurePlayerRegister.contains(p.getUniqueId())) {
			TitleAuth.SecurePlayerRegister.remove(p.getUniqueId());
		}
		if (TitleAuth.SecurePlayerLogin.contains(p.getUniqueId())) {
			TitleAuth.SecurePlayerLogin.remove(p.getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void OnKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		if (TitleAuth.SecurePlayerRegister.contains(p.getUniqueId())) {
			TitleAuth.SecurePlayerRegister.remove(p.getUniqueId());
		}
		if (TitleAuth.SecurePlayerLogin.contains(p.getUniqueId())) {
			TitleAuth.SecurePlayerLogin.remove(p.getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void AuthLoginEvent(PlayerJoinEvent event) {
	    String player = event.getPlayer().getName().toLowerCase();
		Player p = event.getPlayer();
		if (!AuthMeApi.getInstance().isRegistered(player)) {
			//NO REGISTER
			SendTitleNoRegister(p);
			TitleAuth.SecurePlayerRegister.add(p.getUniqueId());
			if (TitleAuth.GetCfg().getBoolean("ACTIONBAR.Enable")) {
			    SendAcNoRegister(p); 
			}
			(new BukkitRunnable() {
    			public void run() {
    				if (p.isOnline() && TitleAuth.SecurePlayerRegister.contains(p.getUniqueId()) && !AuthMeApi.getInstance().isRegistered(player)) {
    					return;
                    } else {
                    	cancel();
                    }
                }
            }).runTaskTimer((Plugin)plugin, 0L, 5L); 
		} else {
			//NO LOGIN
			SendTitleNoLogin(p);
	        TitleAuth.SecurePlayerLogin.add(p.getUniqueId());
	        if (TitleAuth.GetCfg().getBoolean("ACTIONBAR.Enable")) {
	            SendAcNoLogin(p);
	        }
	        (new BukkitRunnable() {
    			public void run() {
    				if (p.isOnline() && TitleAuth.SecurePlayerLogin.contains(p.getUniqueId()) && !AuthMeApi.getInstance().isAuthenticated(p)) {
    					return;
                    } else {
                    	cancel();
                    }
                }
            }).runTaskTimer((Plugin)plugin, 0L, 5L); 
		}
	}
    
    private void SendAcNoRegister(Player player) {
		if (VersionUtils.isNewVersion()) {
			new BukkitRunnable() {
				int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
				@Override
				public void run() {
					if (!TitleAuth.SecurePlayerRegister.contains(player.getUniqueId())) {
			        	cancel();
			            return;
					}
					if (time <= 0) {
						cancel();
					}
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("ACTIONBAR.NO_REGISTER.MESSAGE").replace("<time>", String.valueOf(time)), player)));
					time--;
				}
			}.runTaskTimer(this.plugin, 0L, 20L);
			
		} else {
			new BukkitRunnable() {
				int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
				@Override
				public void run() {
					if (!TitleAuth.SecurePlayerRegister.contains(player.getUniqueId())) {
			        	cancel();
			        	return;
					}
					if (time <= 0) {
						cancel();
					}
					ActionBarAPI.sendActionBar(player, ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("ACTIONBAR.NO_REGISTER.MESSAGE").replace("<time>", String.valueOf(time)), player), 14, (Plugin)plugin);
					time--;
				}
			}.runTaskTimer(this.plugin, 0L, 20L);
		}	
	}
	
	private void SendAcNoLogin(Player player) {
		if (VersionUtils.isNewVersion()) {
			new BukkitRunnable() {
				int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
				@Override
				public void run() {
					if (!TitleAuth.SecurePlayerLogin.contains(player.getUniqueId())) {
			        	cancel();
			        	return;
					}
					if (time <= 0) {
						cancel();
					}
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("ACTIONBAR.NO_LOGIN.MESSAGE").replace("<time>", String.valueOf(time)), player)));
					time--;
				}
			}.runTaskTimer(this.plugin, 0L, 20L);			
		} else {
			new BukkitRunnable() {
				int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
				@Override
				public void run() {
					if (!TitleAuth.SecurePlayerLogin.contains(player.getUniqueId())) {
			        	cancel();
			        	return;
					}
					if (time <= 0) {
						cancel();
					}
					ActionBarAPI.sendActionBar(player, ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("ACTIONBAR.NO_LOGIN.MESSAGE").replace("<time>", String.valueOf(time)), player), 14, (Plugin)plugin);
					time--;
				}

			}.runTaskTimer(this.plugin, 0L, 20L);
		}	
	}
	
    private void SendTitlePremium(Player player) {
		String Title = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.AUTO-LOGIN-PREMIUM.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.AUTO-LOGIN-PREMIUM.SUBTITLE"), player);
		
		int Fadein = TitleAuth.GetCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.FADEIN");
        int Stay = TitleAuth.GetCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.STAY");
        int FadeOut = TitleAuth.GetCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.FADEOUT");
        if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, Fadein, Stay, FadeOut);
		} else {
	        if (VersionUtils.isNewVersion()) {
			      player.sendTitle(Title, subTitle, Fadein, Stay, FadeOut);
			} else {
			    Title = ChatUtils.replaceXColor(Title, player);
			    subTitle = ChatUtils.replaceXColor(subTitle, player);
			    TitleAPI.sendTitles(player, Integer.valueOf(Fadein), Integer.valueOf(Stay), Integer.valueOf(FadeOut), Title, subTitle);
			}
		}
	}
	
	private void SendTitleNoRegister(Player player) {
		String Title = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.NO-REGISTER.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.NO-REGISTER.SUBTITLE"), player);
		if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, 0, 999999999, 20);
		} else {
			if (VersionUtils.isNewVersion()) {
				player.sendTitle(Title, subTitle, 0, 999999999, 999999999);
			} else {
		        int fadeIn = (0);
			    int stay = (999999999);
			    int fadeOut = (20);
			    Title = ChatUtils.replaceXColor(Title, player);
			    subTitle = ChatUtils.replaceXColor(subTitle, player);
			    TitleAPI.sendTitles(player, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), Title, subTitle);
		        
		        
			}
		}
	}
	
	private void SendTitleNoLogin(Player player) {
		String Title = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.NO-LOGIN.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.NO-LOGIN.SUBTITLE"), player);
		if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, 0, 999999999, 20);
		} else {
			if (VersionUtils.isNewVersion()) {
			      player.sendTitle(Title, subTitle, 0, 999999999, 999999999);
			} else {
				int fadeIn = (0);
			    int stay = (999999999);
			    int fadeOut = (20);
			    Title = ChatUtils.replaceXColor(Title, player);
			    subTitle = ChatUtils.replaceXColor(subTitle, player);
			    TitleAPI.sendTitles(player, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), Title, subTitle);   
			}
		}
	}
	
	private void SendTitleOnRegister(Player player) {
		String Title = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.ON-REGISTER.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.ON-REGISTER.SUBTITLE"), player);
		
		int Fadein = TitleAuth.GetCfg().getInt("TITLES.ON-REGISTER.TIME.FADEIN");
        int Stay = TitleAuth.GetCfg().getInt("TITLES.ON-REGISTER.TIME.STAY");
        int FadeOut = TitleAuth.GetCfg().getInt("TITLES.ON-REGISTER.TIME.FADEOUT");
        if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, Fadein, Stay, FadeOut);
		} else {
	        if (VersionUtils.isNewVersion()) {
			      player.sendTitle(Title, subTitle, Fadein, Stay, FadeOut);
			} else {
			    Title = ChatUtils.replaceXColor(Title, player);
			    subTitle = ChatUtils.replaceXColor(subTitle, player);
			    TitleAPI.sendTitles(player, Integer.valueOf(Fadein), Integer.valueOf(Stay), Integer.valueOf(FadeOut), Title, subTitle);
			}
		}
	}
	
	private void SendTitleOnLogin(Player player) {
		String Title = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.ON-LOGIN.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("TITLES.ON-LOGIN.SUBTITLE"), player);
		
		int Fadein = TitleAuth.GetCfg().getInt("TITLES.ON-LOGIN.TIME.FADEIN");
        int Stay = TitleAuth.GetCfg().getInt("TITLES.ON-LOGIN.TIME.STAY");
        int FadeOut = TitleAuth.GetCfg().getInt("TITLES.ON-LOGIN.TIME.FADEOUT");
        if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, Fadein, Stay, FadeOut);
		} else {
	        if (VersionUtils.isNewVersion()) {
			      player.sendTitle(Title, subTitle, Fadein, Stay, FadeOut);
			} else {
			    Title = ChatUtils.replaceXColor(Title, player);
			    subTitle = ChatUtils.replaceXColor(subTitle, player);
			    TitleAPI.sendTitles(player, Integer.valueOf(Fadein), Integer.valueOf(Stay), Integer.valueOf(FadeOut), Title, subTitle);
			}
		}
	}
    
	private void SendAcOnPremium(Player player) {
		
		String actionbarr = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("ACTIONBAR.AUTO_LOGIN_PREMIUM.MESSAGE"), player);
		if (VersionUtils.isNewVersion()) {
		   player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionbarr));			
		} else {
            ActionBarAPI.sendActionBar(player, actionbarr, Integer.valueOf(TitleAuth.GetCfg().getInt("ACTIONBAR.AUTO_LOGIN_PREMIUM.STAY")), (Plugin)plugin);
		}	
	}
    
	private void SendAcOnRegister(Player player) {
		
		String actionbarr = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("ACTIONBAR.ON_REGISTER.MESSAGE"), player);
		if (VersionUtils.isNewVersion()) {
		   player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionbarr));			
		} else {
            ActionBarAPI.sendActionBar(player, actionbarr, Integer.valueOf(TitleAuth.GetCfg().getInt("ACTIONBAR.ON_REGISTER.STAY")), (Plugin)plugin);
		}	
	}
	
	private void SendAcOnLogin(Player player) {
		
		String actionbarr = ChatUtils.replaceXColor(TitleAuth.GetCfg().getString("ACTIONBAR.ON_LOGIN.MESSAGE"), player);
		if (VersionUtils.isNewVersion()) {
		   player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionbarr));			
		} else {
			ActionBarAPI.sendActionBar(player, actionbarr, Integer.valueOf(TitleAuth.GetCfg().getInt("ACTIONBAR.ON_LOGIN.STAY")), (Plugin)plugin);
		}	
	}
}
