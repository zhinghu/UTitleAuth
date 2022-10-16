package com.github.undeadlydev.UTitleAuth.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.undeadlydev.UTitleAuth.Main;
import com.github.undeadlydev.UTitleAuth.Utils.ChatUtils;
import com.github.undeadlydev.UTitleAuth.Utils.CommandUtils;

public class UTitleAuthCommand extends CommandUtils<Main> {
	public Main plugin;
    public UTitleAuthCommand(Main plugin) {
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
               	Main.GetCfg().Reload();
               	commandSender.sendMessage(ChatUtils.colorCodes("&e[UTitleAuth] " + Main.GetCfg().getString("MESSAGE.RELOAD")));
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
                    	Main.GetCfg().Reload();
                    	player.sendMessage(ChatUtils.colorCodes(Main.GetCfg().getString("MESSAGE.RELOAD")));
                    }
                }
            } else {
            	player.sendMessage(ChatUtils.colorCodes(Main.GetCfg().getString("MESSAGE.NO_AUTHORIZED")));
            	return true;
            }
        }
		return true;
    }
}
