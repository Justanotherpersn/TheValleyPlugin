package me.minecraft.theValley;

import org.bukkit.plugin.java.JavaPlugin;

public final class TheValley extends JavaPlugin {
    private DataHandler dataHandler;
    private NametagSetter nametagSetter;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.dataHandler = new DataHandler(this);
        this.nametagSetter = new NametagSetter(this);

        dataHandler.serverVoteInit();

        VoteCommand voteCommand = new VoteCommand(this);
        LifeCommands lifeCommands = new LifeCommands(this);

        // Register Vote Commands
        getCommand("vote").setExecutor(voteCommand);
        getCommand("vote").setTabCompleter(voteCommand);
        getCommand("votelist").setExecutor(voteCommand);
        getCommand("myvote").setExecutor(voteCommand);
        getCommand("startvoting").setExecutor(voteCommand);
        getCommand("stopvoting").setExecutor(voteCommand);
        getCommand("resetvoting").setExecutor(voteCommand);

        // Register Life Commands
        getCommand("editlives").setExecutor(lifeCommands);
        getCommand("removelife").setExecutor(lifeCommands);
        getCommand("editlives").setTabCompleter(lifeCommands);
        getCommand("removelife").setTabCompleter(lifeCommands);

        nametagSetter.createColorTeams();
    }

    public DataHandler getdataHandler() {
        return dataHandler;
    }

    public NametagSetter getNametagSetter() {
        return nametagSetter;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        dataHandler.save();
    }
}


