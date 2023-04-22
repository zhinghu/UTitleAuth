package com.undeadlydev.UTitleAuth.cmds;

import com.undeadlydev.UTitleAuth.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.utils.ChatUtils;

public class utitleauthCMD extends CommandUtils<TitleAuth> {
	public TitleAuth plugin;
    public utitleauthCMD(TitleAuth plugin) {
        super(plugin, "utitleauth");
        setPermission("utitleauth.admin");
        setPermissionMessage(plugin.getLang().get("messages.noPermission"));
        addTabbComplete(0, "reload");
        registerCommand();
        this.plugin = plugin;
    }
  
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            if (args.length < 1) {
                sendHelp(commandSender);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                    plugin.reloadConfig();
                    plugin.getLang().reload();
                    commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getLang().get("message.reload")));
                    break;
                default:
                    sendHelp(commandSender);
                    break;
            }
        }
        if (commandSender instanceof Player) {
            Player p = (Player)commandSender;
            if (args.length < 1) {
                sendHelp(p);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                    if (args.length == 1) {
                        plugin.reloadConfig();
                        plugin.getLang().reload();
                        p.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getLang().get(p, "message.reload")));
                        return true;
                    }
                    switch (args[1].toLowerCase()) {
                        case "lang":
                            plugin.getLang().reload();
                            p.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getLang().get(p, "message.reloadLang")));
                            break;
                        case "config":
                            plugin.reloadConfig();
                            p.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getLang().get(p, "message.reload")));
                            break;
                        default:
                            sendHelp(p);
                            break;
                    }
                    break;
            }
        }
		return true;
    }

    private void sendHelp(CommandSender s) {
        s.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&c&lAdmin Commands."));
        s.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&e/utitleauth reload &7(Reload all configs)"));
        s.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + " "));
        s.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&e/utitleauth reload config &7(Reload only config file)"));
        s.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&e/utitleauth reload lang &7(Reload only lang file)"));
    }
}
