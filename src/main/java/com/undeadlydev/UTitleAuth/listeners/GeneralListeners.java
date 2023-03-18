package com.undeadlydev.UTitleAuth.listeners;

import com.google.common.collect.Sets;
import com.undeadlydev.UTitleAuth.utils.CenterMessage;
import com.undeadlydev.UTitleAuth.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import com.undeadlydev.UTitleAuth.TitleAuth;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import fr.xephi.authme.events.RegisterEvent;
import fr.xephi.authme.events.UnregisterByAdminEvent;
import fr.xephi.authme.events.UnregisterByPlayerEvent;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GeneralListeners implements Listener {
	private TitleAuth plugin;

	private Set<UUID> SecurePlayerRegister = Sets.newHashSet();
	private Set<UUID> SecurePlayerLogin = Sets.newHashSet();

	private static Map<String, BukkitTask> cancelac = new HashMap<>();

	public GeneralListeners(TitleAuth plugin) {
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
		cancelac.remove(p.getName());
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
		cancelac.remove(p.getName());
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
		if (event.getPlayer() == null)
			return;
		if (!event.getPlayer().isOnline())
			return;
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
			cancelac.remove(p.getName());
		    SendAcOnRegister(p);
		}
        if (plugin.getCfg().getBoolean("MESSAGE.WELCOME-MESSAGE.ON_REGISTER.Enable")) {
            SendWOnRegister(p);
        }
	}
	
	@EventHandler
    public void OnLoginPlayer(LoginEvent event) {
		Player p = event.getPlayer();
		SecurePlayerLogin.remove(p.getUniqueId());
		if (Utils.FastLogin && JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
			SendTitlePremium(p);
			if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
				cancelac.remove(p.getName());
				SendAcOnPremium(p);
			}
            if (plugin.getCfg().getBoolean("MESSAGE.WELCOME-MESSAGE.AUTO_LOGIN_PREMIUM.Enable")) {
                SendWPremium(p);
            }
		} else {
			SendTitleOnLogin(p);
			if (plugin.getCfg().getBoolean("ACTIONBAR.Enable")) {
				cancelac.remove(p.getName());
				SendAcOnLogin(p);
			}
			if (plugin.getCfg().getBoolean("MESSAGE.WELCOME-MESSAGE.ON_LOGIN.Enable")) {
				SendWOnLogin(p);
			}
		}
	}

    private void SendWOnRegister(Player player) {
        String mesage = plugin.getCfg().get(player, "MESSAGE.WELCOME-MESSAGE.ON_REGISTER.Message");
        if (plugin.getCfg().getBoolean("MESSAGE.WELCOME-MESSAGE.ON_REGISTER.Enable")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
        }
    }

	private void SendWOnLogin(Player player) {
		String mesage = plugin.getCfg().get(player, "MESSAGE.WELCOME-MESSAGE.ON_LOGIN.Message");
		if (plugin.getCfg().getBoolean("MESSAGE.WELCOME-MESSAGE.ON_LOGIN.Enable")) {
			for (String s : mesage.split("\\n")) {
				player.sendMessage(CenterMessage.getCenteredMessage(s));
			}
		} else {
			player.sendMessage(mesage);
		}
	}

    private void SendWPremium(Player player) {
        String mesage = plugin.getCfg().get(player, "MESSAGE.WELCOME-MESSAGE.AUTO_LOGIN_PREMIUM.Message");
        if (plugin.getCfg().getBoolean("MESSAGE.WELCOME-MESSAGE.AUTO_LOGIN_PREMIUM.Enable")) {
            for (String s : mesage.split("\\n")) {
                player.sendMessage(CenterMessage.getCenteredMessage(s));
            }
        } else {
            player.sendMessage(mesage);
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
		BukkitTask bukkitTask = (new BukkitRunnable() {
			int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
			public void run() {
				if (!cancelac.containsKey(player.getName())) {
					cancel();
				}
				if (time <= 0) {
					cancel();
				}
				plugin.getVc().getReflection().sendActionBar(plugin.getCfg().get(player, "ACTIONBAR.NO_REGISTER.MESSAGE").replace("<time>", String.valueOf(time)), player);
				time--;
			}
		}).runTaskTimer(plugin, 0L, 20L);
		cancelac.put(player.getName(), bukkitTask);
	}
	
	private void SendAcNoLogin(Player player) {
		BukkitTask bukkitTask = (new BukkitRunnable() {
			int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
			@Override
			public void run() {
				if (!cancelac.containsKey(player.getName())) {
					cancel();
					return;
				}
				if (time <= 0) {
					cancel();
				}
				plugin.getVc().getReflection().sendActionBar(plugin.getCfg().get(player, "ACTIONBAR.NO_LOGIN.MESSAGE").replace("<time>", String.valueOf(time)), player);
				time--;
			}

		}).runTaskTimer(this.plugin, 0L, 20L);
		cancelac.put(player.getName(), bukkitTask);
	}
	
    private void SendTitlePremium(Player player) {
		String Title = plugin.getCfg().get(player, "TITLES.AUTO-LOGIN-PREMIUM.TITLE");
		String subTitle = plugin.getCfg().get(player, "TITLES.AUTO-LOGIN-PREMIUM.SUBTITLE");
		
		int fadeIn = plugin.getCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.FADEIN");
        int stay = plugin.getCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.STAY");
        int fadeOut = plugin.getCfg().getInt("TITLES.AUTO-LOGIN-PREMIUM.TIME.FADEOUT");
        if (Utils.CMILib) {
			CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
		} else {
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);

		}
	}
	
	private void SendTitleNoRegister(Player player) {
		String Title = plugin.getCfg().get(player ,"TITLES.NO-REGISTER.TITLE");
		String subTitle = plugin.getCfg().get(player, "TITLES.NO-REGISTER.SUBTITLE");
		if (Utils.CMILib) {
			CMITitleMessage.send(player, Title, subTitle, 0, 999999999, 20);
		} else {
			int fadeIn = (0);
			int stay = (999999999);
			int fadeOut = (20);
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);
		}
	}
	
	private void SendTitleNoLogin(Player player) {
		String Title = plugin.getCfg().get(player, "TITLES.NO-LOGIN.TITLE");
		String subTitle = plugin.getCfg().get("TITLES.NO-LOGIN.SUBTITLE");
		if (Utils.CMILib) {
			CMITitleMessage.send(player, Title, subTitle, 0, 999999999, 20);
		} else {
			int fadeIn = (0);
			int stay = (999999999);
			int fadeOut = (20);
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);
		}
	}
	
	private void SendTitleOnRegister(Player player) {
		String Title = plugin.getCfg().get(player, "TITLES.ON-REGISTER.TITLE");
		String subTitle = plugin.getCfg().get(player, "TITLES.ON-REGISTER.SUBTITLE");
		
		int fadeIn = plugin.getCfg().getInt("TITLES.ON-REGISTER.TIME.FADEIN");
        int stay = plugin.getCfg().getInt("TITLES.ON-REGISTER.TIME.STAY");
        int fadeOut = plugin.getCfg().getInt("TITLES.ON-REGISTER.TIME.FADEOUT");
        if (Utils.CMILib) {
			CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
		} else {
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);
		}
	}
	
	private void SendTitleOnLogin(Player player) {
		String Title = plugin.getCfg().get(player, "TITLES.ON-LOGIN.TITLE");
		String subTitle = plugin.getCfg().get(player, "TITLES.ON-LOGIN.SUBTITLE");
		
		int fadeIn = plugin.getCfg().getInt("TITLES.ON-LOGIN.TIME.FADEIN");
        int stay = plugin.getCfg().getInt("TITLES.ON-LOGIN.TIME.STAY");
        int fadeOut = plugin.getCfg().getInt("TITLES.ON-LOGIN.TIME.FADEOUT");
        if (Utils.CMILib) {
			CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
		} else {
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);

		}
	}
    
	private void SendAcOnPremium(Player player) {
		String message = plugin.getCfg().get(player, "ACTIONBAR.AUTO_LOGIN_PREMIUM.MESSAGE");
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
		String message = plugin.getCfg().get(player, "ACTIONBAR.ON_REGISTER.MESSAGE");
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
		String message = plugin.getCfg().get(player, "ACTIONBAR.ON_LOGIN.MESSAGE");
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
