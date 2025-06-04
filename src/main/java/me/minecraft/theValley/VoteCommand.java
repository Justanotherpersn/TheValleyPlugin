package me.minecraft.theValley;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class VoteCommand implements CommandExecutor, TabCompleter {
    private static final Logger log = LoggerFactory.getLogger(VoteCommand.class);
    private final TheValley plugin;

    public VoteCommand(TheValley plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player player)) return false;


        switch (command.getName().toLowerCase()) {
            case "vote":

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                if (!plugin.getdataHandler().getVoteStatus()){
                    sender.sendMessage("Voting is not enabled");
                    return true;
                }

                if (args.length != 1) {
                    sender.sendMessage("§cUsage: /addlife <player> <# of lives>");
                    return true;
                }

                if (target == null) {
                    sender.sendMessage("Player not found.");
                }

                if (!plugin.getdataHandler().checkEligible(player)) {
                    sender.sendMessage("You're not eligible to vote.");
                }

                plugin.getdataHandler().addVote(player, target);
                    sender.sendMessage("Your vote is now: " + target.getName());
                return true;

            case "votelist":
                plugin.getdataHandler().listVotes(sender);
                return true;

            case "myvote":
                sender.sendMessage("Your Vote is: " + plugin.getdataHandler().getVote(player));
                return true;

            case "startvoting":
                plugin.getdataHandler().voteControl(true);
                sender.sendMessage("Voting Started");
                return true;

            case "stopvoting":
                plugin.getdataHandler().voteControl(false);
                sender.sendMessage("Voting Stopped");
                return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {

            Set<String> allPlayerNames = plugin.getdataHandler().getConfig().getKeys(false);

            return allPlayerNames.stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .filter(name -> !name.equals("server"))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
