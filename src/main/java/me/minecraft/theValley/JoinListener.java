package me.minecraft.theValley;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.http.WebSocket;

public class JoinListener implements Listener {
    private final TheValley plugin;

    public JoinListener(TheValley plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        plugin.getdataHandler().init(player);
        plugin.getNametagSetter().setDeathColor(player);



}
}

