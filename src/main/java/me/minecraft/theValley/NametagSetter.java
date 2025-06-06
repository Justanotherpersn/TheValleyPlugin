package me.minecraft.theValley;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
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



    String[] teamNames = { "f0 Life", "e1 Life", "d2 Life", "c3 Life", "b4 Life", "a5 Life" };
    //String[] teamColor = { "RED", "DARK_PURPLE", "BLUE", "GREEN", "GREEN", "GREEN"};
    ChatColor[] teamcolors = { ChatColor.RED, ChatColor.DARK_PURPLE, ChatColor.BLUE, ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.DARK_GREEN};
    String[] teamPrefix = {"", "", "", "", "[+] ", "[++] "};



    public void createColorTeams(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for(int i = 0; i < teamNames.length; i++) {
            Team team = scoreboard.getTeam(teamNames[i]);

            if(team == null){
                team = scoreboard.registerNewTeam(teamNames[i]);
                team.setColor(teamcolors[i]);
                team.setPrefix(ChatColor.WHITE + teamPrefix[i]);
            }
        }
    }

    public void setNametagColor(Player player, int lives){
        int index = Math.max(0, Math.min(lives, 5));
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(teamNames[index]);
        for (Team t : scoreboard.getTeams()) {
            t.removeEntry(player.getName());
        }
        team.addEntry(player.getName());
        player.setPlayerListName(ChatColor.WHITE.toString() + teamPrefix[index] + teamcolors[index].toString() + player.getName());
        player.setScoreboard(scoreboard);
    }

    public void setDeathColor(Player player){
        int lives = plugin.getdataHandler().getLives(player);
        setNametagColor(player, lives);
    }

    }


