package me.minecraft.theValley;

import org.bukkit.plugin.java.JavaPlugin;

public final class TheValley extends JavaPlugin {
    private DataHandler dataHandler;
    private NametagSetter nametagSetter;

    @Override
    public void onEnable() {
        //dataHandler.save();
        // Plugin startup logic
        this.dataHandler = new DataHandler(this);
        this.nametagSetter = new NametagSetter(this);


        VoteCommand voteCommand = new VoteCommand(this);

        getCommand("vote").setExecutor(new VoteCommand(this));
        getCommand("vote").setTabCompleter(new VoteCommand(this));
        getCommand("votelist").setExecutor(new VoteCommand(this));
        getCommand("myvote").setExecutor(new VoteCommand(this));


        LifeCommands lifeCommands = new LifeCommands(this);

        getCommand("editlives").setExecutor(new LifeCommands(this));
        getCommand("removelife").setExecutor((new LifeCommands(this)));
        getCommand("editlives").setTabCompleter(new LifeCommands(this));
        getCommand("removelife").setTabCompleter((new LifeCommands(this)));


       getServer().getPluginManager().registerEvents(new DeathListener(this), this);
       getServer().getPluginManager().registerEvents(new JoinListener(this), this);
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


