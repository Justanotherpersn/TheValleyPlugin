package me.minecraft.theValley;

import org.apache.logging.log4j.util.StringBuilders;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataHandler {
    public File file;
    private FileConfiguration config;
    private final Plugin plugin;


    public DataHandler(TheValley plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        file = new File(plugin.getDataFolder(), "deaths.yml");

        if (!file.exists()) {
            try {
                file.createNewFile(); // Create empty file if it doesn't exist
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    //region Data Manager

    public void init(Player player) {
        String playerName = player.getName();
        if (!config.contains(playerName)) {
            config.set(playerName + ".lives", 3);
            config.set(playerName + ".votes", 0);
            config.set(playerName + ".votePointer", "");
            save();
        }
    }

    public void save() {
        try {
            config.save(file);
            config = YamlConfiguration.loadConfiguration(file); // Refresh in-memory config
            plugin.getLogger().info("Saved deaths.yml successfully to: " + file.getAbsolutePath());
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save deaths.yml");
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig(){
        return config;
    }

    //endregion


    //region Life Handler

    public void addLife(Player player, int targetLives) {
        String playerName = player.getName();
        int lives = config.getInt(playerName + ".lives", 0) + targetLives;
        int newlives = Math.max(0, Math.min(lives, 5));
        config.set(playerName + ".lives", newlives);
        save();
    }

    public int getLives(Player player) {
        return config.getInt(player.getName() + ".lives", 0);
    }

    //endregion

    //region Vote Handler

    public void addVote(Player player, OfflinePlayer target){
        String playername = player.getName();
        String targetplayer = target.getName();

        if(Objects.requireNonNull(config.getString(playername + ".votePointer", "")).isEmpty()){
            int votes = config.getInt(targetplayer + ".votes", 0) + 1;
            config.set(targetplayer + ".votes", votes);
            config.set(playername + ".votePointer", targetplayer);
            save();
            return;
        }

        String oldVoteName = config.getString(playername + ".votePointer");

        if (targetplayer.equals(oldVoteName)) {
            return; // already voted for this player, do nothing
        }

        int oldVoteNum = config.getInt(oldVoteName + ".votes") - 1;
        int newVoteNum = config.getInt(targetplayer + ".votes", 0) + 1;

        config.set(oldVoteName + ".votes", oldVoteNum);
        config.set(targetplayer + ".votes", newVoteNum);
        config.set(playername + ".votePointer", targetplayer);
        save();
    }

    public boolean checkEligible(Player player){
        String playername = player.getName();
        if(config.getInt(playername + ".lives") > 0){
            return true;
        }
        return false;
    }

    public String getVote(Player player){
        String myvote = config.getString(player.getName() + ".votePointer", "");
        return myvote;
    }

    public void listVotes(CommandSender sender) {
        Map<String, Integer> playerVotes = new HashMap<>();
        Set<String> allPlayerNames = getConfig().getKeys(false);

        for (String name : allPlayerNames) {
            int votes = getConfig().getInt(name + ".votes", 0);
            if (votes > 0) {
                playerVotes.put(name, votes);
            }
        }

        LinkedHashMap<String, Integer> sortedVotes = playerVotes.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        sender.sendMessage(" |------------------| ");
        for (Map.Entry<String, Integer> entry : sortedVotes.entrySet()) {
            sender.sendMessage(" - " + entry.getKey() + ": " + entry.getValue());
        }
        sender.sendMessage(" |------------------| ");


    }
    //endregion


}