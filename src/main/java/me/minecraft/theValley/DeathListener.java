package me.minecraft.theValley;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    private final TheValley plugin;

    public DeathListener(TheValley plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player target = event.getEntity();
        OfflinePlayer killer = target.getKiller();

        if(plugin.getdataHandler().getGraceStatus()){
            return;
        }
        if(killer != null){
           plugin.getdataHandler().addLife(target, -1);
           plugin.getNametagSetter().setDeathColor(target);
        }
    }
}
