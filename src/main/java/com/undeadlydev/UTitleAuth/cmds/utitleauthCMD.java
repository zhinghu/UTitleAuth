package com.undeadlydev.UTitleAuth.cmds;

import com.undeadlydev.UTitleAuth.utls.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.utls.ChatUtils;

public class utitleauthCMD extends CommandUtils<TitleAuth> {
	public TitleAuth plugin;
    public utitleauthCMD(TitleAuth plugin) {
        super(plugin, "utitleauth");
        registerCommand();
        this.plugin = plugin;
    }
  
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player))
        	if (args.length == 0) {
        		commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&c&lAdmin Commands."));
                commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&e/utitleauth reload &7(Reload Title, Subtitle and ActionBar)"));
                return true;
           }
           if (args.length == 1) {
               if (args[0].equalsIgnoreCase("reload")) {
               	plugin.getCfg().reload();
               	commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + plugin.getCfg().get("MESSAGE.RELOAD")));
               } else {
            	   commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&c&lAdmin Commands."));
                   commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + "&e/utitleauth reload &7(Reload Title, Subtitle and ActionBar)"));  
               }
           }
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (player.hasPermission("utitleauth.admin")) {
                if (args.length == 0) {
                     player.sendMessage(ChatUtils.colorCodes("&c&lAdmin Commands."));
                     player.sendMessage(ChatUtils.colorCodes("&e/utitleauth reload &7(Reload Title, Subtitle and ActionBar)"));
                     return true;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        plugin.getCfg().reload();
                    	player.sendMessage(ChatUtils.colorCodes(plugin.getCfg().get("MESSAGE.RELOAD")));
                    }
                }
            } else {
            	player.sendMessage(ChatUtils.colorCodes(plugin.getCfg().get("MESSAGE.NO_AUTHORIZED")));
            	return true;
            }
        }
		return true;
    }
}
