package me.minecraft.theValley;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.stream.Collectors;


public class LifeCommands implements CommandExecutor, TabCompleter{
    private final TheValley plugin;

    public LifeCommands(TheValley plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        switch (command.getName().toLowerCase()){
            case "editlives":
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                if(!sender.hasPermission("thevalley.editlives")){
                    sender.sendMessage(("You do not have permission to use this command."));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /editlives <player> <# of lives>");
                    return true;
                }

                if(target == null){
                    sender.sendMessage("Player not found.");
                }

                int lives;
                try {
                    lives = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cNumber of lives must be a valid integer.");
                    return true;
                }

                plugin.getdataHandler().addLife(target, lives);
                if (target.isOnline()) {
                    plugin.getNametagSetter().setDeathColor(target.getPlayer());
                }
                sender.sendMessage(lives + " lives to " + target.getName());
                return true;

            case "removelife":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players can use this command.");
                    return true;
                }
                Player player = (Player) sender;
                plugin.getdataHandler().addLife(player, -1);
                plugin.getNametagSetter().setDeathColor(player);
                sender.sendMessage("Life removed");
                return true;

        }
        return true;

        }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(Bukkit.getOfflinePlayers())
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
