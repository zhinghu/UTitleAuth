package com.undeadlydev.UTitleAuth.listeners;

import com.google.common.collect.Sets;
import com.undeadlydev.UTitleAuth.utls.Utils;
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
import com.undeadlydev.UTitleAuth.utls.ChatUtils;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import fr.xephi.authme.events.RegisterEvent;
import fr.xephi.authme.events.UnregisterByAdminEvent;
import fr.xephi.authme.events.UnregisterByPlayerEvent;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Set;
import java.util.UUID;

public class PlayerListeners implements Listener {
	private TitleAuth plugin;

	private Set<UUID> SecurePlayerRegister = Sets.newHashSet();
	private Set<UUID> SecurePlayerLogin = Sets.newHashSet();

	public PlayerListeners(TitleAuth plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (SecurePlayerRegister.contains(p.getUniqueId())) {
			SecurePlayerRegister.remove(p.getUniqueId());
		}
		if (SecurePlayerLogin.contains(p.getUniqueId())) {
			SecurePlayerLogin.remove(p.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		if (SecurePlayerRegister.contains(p.getUniqueId())) {
			SecurePlayerRegister.remove(p.getUniqueId());
		}
		if (SecurePlayerLogin.contains(p.getUniqueId())) {
			SecurePlayerLogin.remove(p.getUniqueId());
		}
	}

	@EventHandler
    public void UnRegisterByPlayer(UnregisterByPlayerEvent event) {
    	Player p = event.getPlayer();
    	SendTitleNoRegister(p);
		SecurePlayerRegister.add(p.getUniqueId());
		if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
		    SendAcNoRegister(p); 
		}
    }
	
	@EventHandler
    public void UnRegisterByAdmin(UnregisterByAdminEvent event) {
    	Player p = event.getPlayer();
    	SendTitleNoRegister(p);
		SecurePlayerRegister.add(p.getUniqueId());
		if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
		    SendAcNoRegister(p); 
		}
    }
	
	@EventHandler
    public void OnRegisterPlayer(RegisterEvent event) {
		Player p = event.getPlayer();
		SecurePlayerRegister.remove(p.getUniqueId());
		SendTitleOnRegister(p);
		if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
		    SendAcOnRegister(p);
		}
	}
	
	@EventHandler
    public void OnLoginPlayer(LoginEvent event) {
		Player p = event.getPlayer();
		SecurePlayerLogin.remove(p.getUniqueId());
		if (Utils.FastLogin && JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
			SendTitlePremium(p);
			if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
				SendAcOnPremium(p);
			}
		} else {
			SendTitleOnLogin(p);
			if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
				SendAcOnLogin(p);
			}	
		}
	}
	
	@EventHandler
    public void OnLogoutPlayer(LogoutEvent event) {
		Player p = event.getPlayer();
		SecurePlayerLogin.add(p.getUniqueId());
		SendTitleNoLogin(p);
		if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
			SendAcNoLogin(p);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void AuthLoginEvent(PlayerJoinEvent event) {
	    String player = event.getPlayer().getName().toLowerCase();
		Player p = event.getPlayer();
		if (!AuthMeApi.getInstance().isRegistered(player)) {
			//NO REGISTER
			SendTitleNoRegister(p);
			SecurePlayerRegister.add(p.getUniqueId());
			if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
				SendAcNoRegister(p);
			}
		} else {
			//NO LOGIN
			SendTitleNoLogin(p);
	        SecurePlayerLogin.add(p.getUniqueId());
	        if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
	            SendAcNoLogin(p);
	        }
		}
	}
    
    private void SendAcNoRegister(Player player) {
		new BukkitRunnable() {
			int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
			@Override
			public void run() {
				if (!SecurePlayerRegister.contains(player.getUniqueId())) {
					cancel();
					return;
				}
				if (time <= 0) {
					cancel();
				}
				plugin.getVc().getReflection().sendActionBar(ChatUtils.replace(plugin.getCfg().get(player, "ACTIONBAR.NO_REGISTER.MESSAGE").replace("<time>", String.valueOf(time)), player), player);
				time--;
			}
		}.runTaskTimer(this.plugin, 0L, 20L);
	}
	
	private void SendAcNoLogin(Player player) {
		new BukkitRunnable() {
			int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
			@Override
			public void run() {
				if (!SecurePlayerLogin.contains(player.getUniqueId())) {
					cancel();
					return;
				}
				if (time <= 0) {
					cancel();
				}
				plugin.getVc().getReflection().sendActionBar(ChatUtils.replace(plugin.getCfg().get(player, "ACTIONBAR.NO_LOGIN.MESSAGE").replace("<time>", String.valueOf(time)), player), player);
				time--;
			}

		}.runTaskTimer(this.plugin, 0L, 20L);
	}
	
    private void SendTitlePremium(Player player) {
		String Title = plugin.getCfg().get(player, "TITLES.AUTO-LOGIN-PREMIUM.TITLE");
		String subTitle = plugin.getCfg().get(player, "TITLES.AUTO-LOGIN-PREMIUM.SUBTITLE");
		
		int fadeIn = plugin.getCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.FADEIN");
        int stay = plugin.getCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.STAY");
        int fadeOut = plugin.getCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.FADEOUT");
        if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
		} else {
			Title = ChatUtils.replace(Title, player);
			subTitle = ChatUtils.replace(subTitle, player);
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);

		}
	}
	
	private void SendTitleNoRegister(Player player) {
		String Title = plugin.getCfg().get(player ,"TITLES.NO-REGISTER.TITLE");
		String subTitle = plugin.getCfg().get(player, "TITLES.NO-REGISTER.SUBTITLE");
		if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, 0, 999999999, 20);
		} else {
			int fadeIn = (0);
			int stay = (999999999);
			int fadeOut = (20);
			Title = ChatUtils.replace(Title, player);
			subTitle = ChatUtils.replace(subTitle, player);
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);
		}
	}
	
	private void SendTitleNoLogin(Player player) {
		String Title = plugin.getCfg().get(player, "TITLES.NO-LOGIN.TITLE");
		String subTitle = plugin.getCfg().get("TITLES.NO-LOGIN.SUBTITLE");
		if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, 0, 999999999, 20);
		} else {
			int fadeIn = (0);
			int stay = (999999999);
			int fadeOut = (20);
			Title = ChatUtils.replace(Title, player);
			subTitle = ChatUtils.replace(subTitle, player);
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);
		}
	}
	
	private void SendTitleOnRegister(Player player) {
		String Title = plugin.getCfg().get(player, "TITLES.ON-REGISTER.TITLE");
		String subTitle = plugin.getCfg().get(player, "TITLES.ON-REGISTER.SUBTITLE");
		
		int fadeIn = plugin.getCfg().getInt("TITLES.ON-REGISTER.TIME.FADEIN");
        int stay = plugin.getCfg().getInt("TITLES.ON-REGISTER.TIME.STAY");
        int fadeOut = plugin.getCfg().getInt("TITLES.ON-REGISTER.TIME.FADEOUT");
        if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
		} else {
			Title = ChatUtils.replace(Title, player);
			subTitle = ChatUtils.replace(subTitle, player);
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);
		}
	}
	
	private void SendTitleOnLogin(Player player) {
		String Title = plugin.getCfg().get(player, "TITLES.ON-LOGIN.TITLE");
		String subTitle = plugin.getCfg().get(player, "TITLES.ON-LOGIN.SUBTITLE");
		
		int fadeIn = plugin.getCfg().getInt("TITLES.ON-LOGIN.TIME.FADEIN");
        int stay = plugin.getCfg().getInt("TITLES.ON-LOGIN.TIME.STAY");
        int fadeOut = plugin.getCfg().getInt("TITLES.ON-LOGIN.TIME.FADEOUT");
        if (Bukkit.getPluginManager().isPluginEnabled("CMILib")) {
			CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
		} else {
			Title = ChatUtils.replace(Title, player);
			subTitle = ChatUtils.replace(subTitle, player);
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);

		}
	}
    
	private void SendAcOnPremium(Player player) {
		String message = ChatUtils.replace(plugin.getCfg().get(player, "ACTIONBAR.AUTO_LOGIN_PREMIUM.MESSAGE"), player);
		new BukkitRunnable() {
			int time = plugin.getCfg().getInt("ACTIONBAR.AUTO_LOGIN_PREMIUM.STAY");
			@Override
			public void run() {
				if (SecurePlayerLogin.contains(player.getUniqueId())) {
					cancel();
				}
				if (time <= 0) {
					cancel();
				}
				plugin.getVc().getReflection().sendActionBar(message, player);
				time--;
			}
		}.runTaskTimer(this.plugin, 0L, 20L);
	}
    
	private void SendAcOnRegister(Player player) {
		String message = ChatUtils.replace(plugin.getCfg().get(player, "ACTIONBAR.ON_REGISTER.MESSAGE"), player);
		new BukkitRunnable() {
			int time = plugin.getCfg().getInt("ACTIONBAR.ON_REGISTER.STAY");
			@Override
			public void run() {
				if (SecurePlayerRegister.contains(player.getUniqueId())) {
					cancel();
				}
				if (time <= 0) {
					cancel();
				}
				plugin.getVc().getReflection().sendActionBar(message, player);
				time--;
			}
		}.runTaskTimer(this.plugin, 0L, 20L);
	}
	
	private void SendAcOnLogin(Player player) {
		String message = ChatUtils.replace(plugin.getCfg().get(player, "ACTIONBAR.ON_LOGIN.MESSAGE"), player);
		new BukkitRunnable() {
			int time = plugin.getCfg().getInt("ACTIONBAR.ON_LOGIN.STAY");
			@Override
			public void run() {
				if (SecurePlayerLogin.contains(player.getUniqueId())) {
					cancel();
				}
				if (time <= 0) {
					cancel();
				}
				plugin.getVc().getReflection().sendActionBar(message, player);
				time--;
			}
		}.runTaskTimer(this.plugin, 0L, 20L);
	}
}
