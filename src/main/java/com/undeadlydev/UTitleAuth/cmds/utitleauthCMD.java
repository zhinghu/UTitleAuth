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
        setPermissionMessage(plugin.getCfg().get("MESSAGE.NO_AUTHORIZED"));
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
                    plugin.getCfg().reload();
                    commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getCfg().get("MESSAGE.RELOAD")));
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
                    plugin.getCfg().reload();
                    p.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getCfg().get("MESSAGE.RELOAD")));
                    break;
                default:
                    sendHelp(p);
                    break;
            }
        }
		return true;
    }

    private void sendHelp(CommandSender s) {
        s.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&c&lAdmin Commands."));
        s.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&e/utitleauth reload &7(Reload all Message and booleans)"));
    }
}
