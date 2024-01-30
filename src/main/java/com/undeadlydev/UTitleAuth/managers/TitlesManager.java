package com.undeadlydev.UTitleAuth.managers;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.utils.Utils;
import net.Zrips.CMILib.TitleMessages.CMITitleMessage;
import org.bukkit.entity.Player;

public class TitlesManager {

    TitleAuth plugin = TitleAuth.get();

    public void SendTitlePremium(Player player) {
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

    public void SendTitleNoRegister(Player player) {
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

    public void SendTitleNoLogin(Player player) {
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

    public void SendTitleOnRegister(Player player) {
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

    public void SendTitleOnLogin(Player player) {
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
}
