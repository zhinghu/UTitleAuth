package com.undeadlydev.UTitleAuth.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.Utils.ChatUtils;
import com.undeadlydev.UTitleAuth.Utils.CommandUtils;

public class UTitleAuthCommand extends CommandUtils<TitleAuth> {
	public TitleAuth plugin;
    public UTitleAuthCommand(TitleAuth plugin) {
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
               	TitleAuth.GetCfg().Reload();
               	commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + TitleAuth.GetCfg().getString("MESSAGE.RELOAD")));
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
                    	TitleAuth.GetCfg().Reload();
                    	player.sendMessage(ChatUtils.colorCodes(TitleAuth.GetCfg().getString("MESSAGE.RELOAD")));
                    }
                }
            } else {
            	player.sendMessage(ChatUtils.colorCodes(TitleAuth.GetCfg().getString("MESSAGE.NO_AUTHORIZED")));
            	return true;
            }
        }
		return true;
    }
}
