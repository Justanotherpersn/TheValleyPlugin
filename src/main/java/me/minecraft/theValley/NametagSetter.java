package me.minecraft.theValley;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NametagSetter {
    private final TheValley plugin;

    public NametagSetter(TheValley plugin){
        this.plugin = plugin;
    }



    String[] teamNames = { "RED", "DARK_PURPLE", "BLUE", "GREEN"};

    public void createColorTeams(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for(int i = 0; i < teamNames.length; i++) {
            Team team = scoreboard.getTeam(teamNames[i]);

            if(team == null){
                team = scoreboard.registerNewTeam(teamNames[i]);
                team.setColor(ChatColor.valueOf(teamNames[i].toUpperCase()));
            }
        }
    }

    public void setNametagColor(Player player, int lives){
        int index = Math.min(3, Math.max(0, lives));
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(teamNames[index]);
        for (Team t : scoreboard.getTeams()) {
            t.removeEntry(player.getName());
        }
        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);
    }

    public void setDeathColor(Player player){
        int lives = plugin.getdataHandler().getLives(player);
        setNametagColor(player, lives);
    }

    }


