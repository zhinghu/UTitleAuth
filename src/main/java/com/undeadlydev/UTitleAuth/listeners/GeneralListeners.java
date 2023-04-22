package com.undeadlydev.UTitleAuth.listeners;

import com.google.common.collect.Sets;
import com.undeadlydev.UTitleAuth.superclass.SpigotUpdater;
import com.undeadlydev.UTitleAuth.utils.CenterMessage;
import com.undeadlydev.UTitleAuth.utils.ChatUtils;
import com.undeadlydev.UTitleAuth.utils.Utils;
import fr.xephi.authme.events.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import com.undeadlydev.UTitleAuth.TitleAuth;

import fr.xephi.authme.api.v3.AuthMeApi;
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
	public void AuthLoginEvent(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		String pl = p.getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				if (!AuthMeApi.getInstance().isRegistered(pl)) {
					SendTitleNoRegister(p);
					SecurePlayerRegister.add(p.getUniqueId());
					if (plugin.getConfig().getBoolean("config.actionbar.enabled"))
						SendAcNoRegister(p);
				} else if(!AuthMeApi.getInstance().isAuthenticated(p)) {
					SendTitleNoLogin(p);
					SecurePlayerLogin.add(p.getUniqueId());
					if (plugin.getConfig().getBoolean("config.actionbar.enabled"))
						SendAcNoLogin(p);
				}
			}
		},  20L);

	}

	private void SendNoLogin(Player p) {
		SendTitleNoLogin(p);
		SecurePlayerLogin.add(p.getUniqueId());
		if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
			SendAcNoLogin(p);
		}
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
		if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
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
		if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
			SendAcNoRegister(p);
		}
    }
	
	@EventHandler
    public void OnRegisterPlayer(RegisterEvent event) {
		Player p = event.getPlayer();
		SecurePlayerRegister.remove(p.getUniqueId());
		if (TitleAuth.getOtherConfig().getBoolean("settings.registration.forceLoginAfterRegister")) {
			SendNoLogin(p);
			return;
		}
		SendTitleOnRegister(p);
		if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
			cancelac.remove(p.getName());
			SendAcOnRegister(p);
		}
		if (plugin.getConfig().getBoolean("config.message.welcome.register.enabled")) {
			SendWOnRegister(p);
		}
	}
	
	@EventHandler
    public void OnLoginPlayer(LoginEvent event) {
		Player p = event.getPlayer();
		SecurePlayerLogin.remove(p.getUniqueId());
		if (AuthMeApi.getInstance().isAuthenticated(p)) {
			if (Utils.FastLogin && JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
				SendTitlePremium(p);
				if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
					cancelac.remove(p.getName());
					SendAcOnPremium(p);
				}
				if (plugin.getConfig().getBoolean("config.message.welcome.autologin.enabled")) {
					SendWPremium(p);
				}
			} else {
				SendTitleOnLogin(p);
				if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
					cancelac.remove(p.getName());
					SendAcOnLogin(p);
				}
				if (plugin.getConfig().getBoolean("config.message.welcome.login.enabled")) {
					SendWOnLogin(p);
				}
			}
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

	private void SendWOnLogin(Player player) {
		String mesage = plugin.getLang().get(player, "message.welcome.login");
		if (plugin.getConfig().getBoolean("config.message.welcome.login.center")) {
			for (String s : mesage.split("\\n")) {
				player.sendMessage(CenterMessage.getCenteredMessage(s));
			}
		} else {
			player.sendMessage(mesage);
		}
	}

    private void SendWPremium(Player player) {
        String mesage = plugin.getLang().get(player, "message.welcome.autologin");
        if (plugin.getConfig().getBoolean("config.message.welcome.autologin.center")) {
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
		if (plugin.getConfig().getBoolean("config.actionbar.enabled")) {
			SendAcNoLogin(p);
		}
	}
    
    private void SendAcNoRegister(Player player) {
		BukkitTask bukkitTask = (new BukkitRunnable() {
			int time = TitleAuth.getOtherConfig().getInt("settings.restrictions.timeout");
			public void run() {
				if (!cancelac.containsKey(player.getName())) {
					cancel();
					return;
				}
				if (time <= 0) {
					cancel();
					return;
				}
				plugin.getVc().getReflection().sendActionBar(plugin.getLang().get(player, "actionbar.noregister").replace("<time>", String.valueOf(time)), player);
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
					return;
				}
				plugin.getVc().getReflection().sendActionBar(plugin.getLang().get(player, "actionbar.nologin").replace("<time>", String.valueOf(time)), player);
				time--;
			}

		}).runTaskTimer(this.plugin, 0L, 20L);
		cancelac.put(player.getName(), bukkitTask);
	}
	
    private void SendTitlePremium(Player player) {
		String Title = plugin.getLang().get(player, "titles.autologin.title");
		String subTitle = plugin.getLang().get(player, "titles.autologin.subtitle");

		int fadeIn = plugin.getConfig().getInt("config.titles.autologin.time.fadein");
		int stay = plugin.getConfig().getInt("config.titles.autologin.time.stay");
		int fadeOut = plugin.getConfig().getInt("config.titles.autologin.time.fadeout");
        if (Utils.CMILib) {
			CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
		} else {
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);

		}
	}
	
	private void SendTitleNoRegister(Player player) {
		String Title = plugin.getLang().get(player, "titles.noregister.title");
		String subTitle = plugin.getLang().get(player, "titles.noregister.subtitle");
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
		String Title = plugin.getLang().get(player, "titles.nologin.title");
		String subTitle = plugin.getLang().get(player, "titles.nologin.subtitle");
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
		String Title = plugin.getLang().get(player, "titles.register.title");
		String subTitle = plugin.getLang().get(player, "titles.register.subtitle");

		int fadeIn = plugin.getConfig().getInt("config.titles.register.time.fadein");
		int stay = plugin.getConfig().getInt("config.titles.register.time.stay");
		int fadeOut = plugin.getConfig().getInt("config.titles.register.time.fadeout");
        if (Utils.CMILib) {
			CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
		} else {
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);
		}
	}
	
	private void SendTitleOnLogin(Player player) {
		String Title = plugin.getLang().get(player, "titles.login.title");
		String subTitle = plugin.getLang().get(player, "titles.login.subtitle");
		
		int fadeIn = plugin.getConfig().getInt("config.titles.login.time.fadein");
        int stay = plugin.getConfig().getInt("config.titles.login.time.stay");
        int fadeOut = plugin.getConfig().getInt("config.titles.login.time.fadeout");
        if (Utils.CMILib) {
			CMITitleMessage.send(player, Title, subTitle, fadeIn, stay, fadeOut);
		} else {
			plugin.getVc().getReflection().sendTitle(Title, subTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut), player);

		}
	}
    
	private void SendAcOnPremium(Player player) {
		String message = plugin.getLang().get(player, "actionbar.autologin");
		new BukkitRunnable() {
			int time = plugin.getConfig().getInt("config.actionbar.autologin.time.stay");
			@Override
			public void run() {
				if (SecurePlayerLogin.contains(player.getUniqueId())) {
					cancel();
					return;
				}
				if (time <= 0) {
					cancel();
					return;
				}
				plugin.getVc().getReflection().sendActionBar(message, player);
				time--;
			}
		}.runTaskTimer(this.plugin, 0L, 20L);
	}
    
	private void SendAcOnRegister(Player player) {
		String message = plugin.getLang().get(player, "actionbar.register");
		new BukkitRunnable() {
			int time = plugin.getConfig().getInt("config.actionbar.register.time.stay");
			@Override
			public void run() {
				if (AuthMeApi.getInstance().isRegistered(player.getName()) && AuthMeApi.getInstance().isAuthenticated(player.getPlayer()) || SecurePlayerRegister.contains(player.getUniqueId())) {
					cancel();
					return;
				}
				if (time <= 0) {
					cancel();
					return;
				}
				plugin.getVc().getReflection().sendActionBar(message, player);
				time--;
			}
		}.runTaskTimer(this.plugin, 0L, 20L);
	}
	
	private void SendAcOnLogin(Player player) {
		String message = plugin.getLang().get(player, "actionbar.login");
		new BukkitRunnable() {
			int time = plugin.getConfig().getInt("config.actionbar.login.time.stay");
			@Override
			public void run() {
				if (SecurePlayerLogin.contains(player.getUniqueId())) {
					cancel();
					return;
				}
				if (time <= 0) {
					cancel();
					return;
				}
				plugin.getVc().getReflection().sendActionBar(message, player);
				time--;
			}
		}.runTaskTimer(this.plugin, 0L, 20L);
	}

	@EventHandler
	public void PlayerJoinUpdateCheck(PlayerJoinEvent e) {
		if (plugin.getConfig().getBoolean("update-check")) {
			final Player p = e.getPlayer();
			if (p.isOp() || p.hasPermission("utitleauth.updatecheck")) {
				new BukkitRunnable() {
					public void run() {
						SpigotUpdater updater = new SpigotUpdater(plugin, 88058);
						try {
							if (updater.checkForUpdates()) {
								p.sendMessage(ChatUtils.parseLegacy("&bAn update for &fUTitleAuth &e(&fUTitleAuth &fv" + updater.getLatestVersion() + "&e)"));
								p.sendMessage(ChatUtils.parseLegacy("&bis available at &e" + updater.getResourceURL()));
							}
						} catch (Exception e) {}
					}
				}.runTaskAsynchronously(plugin);
			}
		}
	}
}
