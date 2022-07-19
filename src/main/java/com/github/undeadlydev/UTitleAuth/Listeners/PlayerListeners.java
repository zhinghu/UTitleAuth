package com.github.undeadlydev.UTitleAuth.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import com.github.undeadlydev.UTitleAuth.Main;
import com.github.undeadlydev.UTitleAuth.Utils.ActionBarAPI;
import com.github.undeadlydev.UTitleAuth.Utils.ChatUtils;
import com.github.undeadlydev.UTitleAuth.Utils.TitleAPI;
import com.github.undeadlydev.UTitleAuth.Utils.VersionUtils;

import fr.xephi.authme.api.v3.AuthMeApi;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerListeners implements Listener {
	private Main plugin;
    private final Integer timeleft;
    
	public PlayerListeners(Main plugin) {
		this.plugin = plugin;
		this.timeleft = Main.getOtherConfig().getInt("settings.restrictions.timeout");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
    public void AuthLoginEvent(PlayerJoinEvent event) {
	    String player = event.getPlayer().getName().toLowerCase();
		Player p = event.getPlayer();
		if (!AuthMeApi.getInstance().isRegistered(player)) {
			//NO REGISTER
			SendTitleNoRegister(p);
			Main.SecurePlayerRegister.add(p.getUniqueId());
			if (Main.GetCfg().getBoolean("ACTIONBAR.Enable")) {
			    this.SendAcNoRegister(p);
			     
			}
			(new BukkitRunnable() {
    		    int time = Main.getOtherConfig().getInt("settings.restrictions.timeout");
    			public void run() {
    				if (p.isOnline() && Main.SecurePlayerRegister.contains(p.getUniqueId()) && !AuthMeApi.getInstance().isRegistered(player)) {
    					this.time--;
                    } else {
                    	Main.SecurePlayerRegister.remove(p.getUniqueId());
            			SendTitleOnRegister(p);
            			if (Main.GetCfg().getBoolean("ACTIONBAR.Enable")) {
            			    SendAcOnRegister(p);
            			}
                    	cancel();
                    }
    				if (this.time <= 0) {
                        cancel();
    				}
                }
            }).runTaskTimer((Plugin)plugin, 0L, 20L); 
		} else {
			//NO LOGIN
			SendTitleNoLogin(p);
	        Main.SecurePlayerLogin.add(p.getUniqueId());
	        if (Main.GetCfg().getBoolean("ACTIONBAR.Enable")) {
	            SendAcNoLogin(p);
	        }
	        (new BukkitRunnable() {
    		    int time = Main.getOtherConfig().getInt("settings.restrictions.timeout");
    			public void run() {
    				if (p.isOnline() && Main.SecurePlayerLogin.contains(p.getUniqueId()) && !AuthMeApi.getInstance().isAuthenticated(p)) {
    					this.time--;
                    } else {
                    	Main.SecurePlayerLogin.remove(p.getUniqueId());
        				if (ChatUtils.FastLogin && JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
        					SendTitlePremium(p);
        					if (Main.GetCfg().getBoolean("ACTIONBAR.Enable")) {
        						SendAcOnPremium(p);
        					}
        				} else {
            				SendTitleOnLogin(p);
            				if (Main.GetCfg().getBoolean("ACTIONBAR.Enable")) {
            					SendAcOnLogin(p);
            				}	
        				}
                    	cancel();
                    }
    				if (this.time <= 0) {
                        cancel();
    				}
                }
            }).runTaskTimer((Plugin)plugin, 0L, 20L); 
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
		
		if (VersionUtils.isNewVersion()) {
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
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtils.replaceXColor(Main.GetCfg().getString("ACTIONBAR.NO_REGISTER.MESSAGE").replace("<time>", String.valueOf(time[0])), player)));
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
					ActionBarAPI.sendActionBar(player, ChatUtils.replaceXColor(Main.GetCfg().getString("ACTIONBAR.NO_REGISTER.MESSAGE").replace("<time>", String.valueOf(time[0])), player), 14, (Plugin)plugin);
					time[0]--;
				}
			}.runTaskTimer(this.plugin, 0L, 20L);
		}	
	}
	
	private void SendAcNoLogin(Player player) {
		final int[] time = {this.timeleft};
		
		if (VersionUtils.isNewVersion()) {
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
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtils.replaceXColor(Main.GetCfg().getString("ACTIONBAR.NO_LOGIN.MESSAGE").replace("<time>", String.valueOf(time[0])), player)));
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
					ActionBarAPI.sendActionBar(player, ChatUtils.replaceXColor(Main.GetCfg().getString("ACTIONBAR.NO_LOGIN.MESSAGE").replace("<time>", String.valueOf(time[0])), player), 14, (Plugin)plugin);
					time[0]--;
				}

			}.runTaskTimer(this.plugin, 0L, 20L);
		}	
	}
	
    public void SendRegisterRequiered(Player p) {
		SendTitleNoRegister(p);
		Main.SecurePlayerRegister.add(p.getUniqueId());
		if (Main.GetCfg().getBoolean("ACTIONBAR.Enable")) {
		    SendAcNoRegister(p);
		}
    }
	
    private void SendTitlePremium(Player player) {
		String Title = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.AUTO-LOGIN-PREMIUM.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.AUTO-LOGIN-PREMIUM.SUBTITLE"), player);
		
		int Fadein = Main.GetCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.FADEIN");
        int Stay = Main.GetCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.STAY");
        int FadeOut = Main.GetCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.FADEOUT");
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
		String Title = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.NO-REGISTER.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.NO-REGISTER.SUBTITLE"), player);
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
		String Title = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.NO-LOGIN.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.NO-LOGIN.SUBTITLE"), player);
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
		String Title = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.ON-REGISTER.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.ON-REGISTER.SUBTITLE"), player);
		
		int Fadein = Main.GetCfg().getInt("TITLES.ON-REGISTER.TIME.FADEIN");
        int Stay = Main.GetCfg().getInt("TITLES.ON-REGISTER.TIME.STAY");
        int FadeOut = Main.GetCfg().getInt("TITLES.ON-REGISTER.TIME.FADEOUT");
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
		String Title = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.ON-LOGIN.TITLE"), player);
		String subTitle = ChatUtils.replaceXColor(Main.GetCfg().getString("TITLES.ON-LOGIN.SUBTITLE"), player);
		
		int Fadein = Main.GetCfg().getInt("TITLES.ON-LOGIN.TIME.FADEIN");
        int Stay = Main.GetCfg().getInt("TITLES.ON-LOGIN.TIME.STAY");
        int FadeOut = Main.GetCfg().getInt("TITLES.ON-LOGIN.TIME.FADEOUT");
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
	
	@SuppressWarnings("deprecation")
	private void SendAcOnPremium(Player player) {
		
		String actionbarr = ChatUtils.replaceXColor(Main.GetCfg().getString("ACTIONBAR.AUTO_LOGIN_PREMIUM.MESSAGE"), player);
		if (VersionUtils.isNewVersion()) {
		   player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionbarr));			
		} else {
            ActionBarAPI.sendActionBar(player, actionbarr, Integer.valueOf(Main.GetCfg().getInt("ACTIONBAR.AUTO_LOGIN_PREMIUM.STAY")), (Plugin)plugin);
		}	
	}
    
	@SuppressWarnings("deprecation")
	private void SendAcOnRegister(Player player) {
		
		String actionbarr = ChatUtils.replaceXColor(Main.GetCfg().getString("ACTIONBAR.ON_REGISTER.MESSAGE"), player);
		if (VersionUtils.isNewVersion()) {
		   player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionbarr));			
		} else {
            ActionBarAPI.sendActionBar(player, actionbarr, Integer.valueOf(Main.GetCfg().getInt("ACTIONBAR.ON_REGISTER.STAY")), (Plugin)plugin);
		}	
	}
	
	@SuppressWarnings("deprecation")
	private void SendAcOnLogin(Player player) {
		
		String actionbarr = ChatUtils.replaceXColor(Main.GetCfg().getString("ACTIONBAR.ON_LOGIN.MESSAGE"), player);
		if (VersionUtils.isNewVersion()) {
		   player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionbarr));			
		} else {
			ActionBarAPI.sendActionBar(player, actionbarr, Integer.valueOf(Main.GetCfg().getInt("ACTIONBAR.ON_LOGIN.STAY")), (Plugin)plugin);
		}	
	}
}
