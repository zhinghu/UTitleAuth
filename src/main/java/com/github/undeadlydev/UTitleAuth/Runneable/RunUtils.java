package com.github.undeadlydev.UTitleAuth.Runneable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.github.undeadlydev.UTitleAuth.Main;

public class RunUtils {
    private Main plugin;
  
    private static List<Integer> timers = new ArrayList<>();
  
    public RunUtils(Main plugin) {
        this.plugin = plugin;
        int interval = Integer.valueOf(3);
        timers.add(Integer.valueOf((new RunLogin(plugin)).runTaskTimer((Plugin)plugin, 0L, interval).getTaskId())); 
    }
  
    public void reload() {
        cancelTimer();
        int interval = Integer.valueOf(3);
        timers.add(Integer.valueOf((new RunLogin(this.plugin)).runTaskTimer((Plugin)this.plugin, 0L, interval).getTaskId()));
    }
  
    public static void cancelTimer() {
        for (Integer timerID : timers) {
            if (timerID != null)
                Bukkit.getScheduler().cancelTask(timerID.intValue()); 
        } 
        timers = new ArrayList<>();
    }
}

