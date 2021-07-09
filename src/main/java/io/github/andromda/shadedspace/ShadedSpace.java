package io.github.andromda.shadedspace;

import io.github.andromda.shadedspace.basic.*;
import io.github.andromda.shadedspace.events.NewChunk;
import io.github.andromda.shadedspace.utilities.Utilities;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class ShadedSpace extends JavaPlugin implements Listener {
    private File warpFile;
    private FileConfiguration warps;
    private File homeFile;
    private FileConfiguration homes;
    public final HashMap<String, SubCommand> commands = new HashMap<>();
    private final HashMap<String, Listener> listeners = new HashMap<>();

    /*
    !!!!! CONSTRUCTOR !!!!!
     */

    public ShadedSpace() {
        createWarps();
        createHomes();
        initClasses();
    }

    @Override
    public void onEnable() {
        ShadedSpaceBase base = new ShadedSpaceBase();
        this.getCommand("ss").setExecutor(base);

        registerEvents();

        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        Utilities.saveCustom(homeFile, homes);
        Utilities.saveCustom(warpFile, warps);
    }

    /*
    !!!!! HELPER METHODS !!!!!
     */

    private void initClasses() {
        //init command classes and register them
        commands.put("inv", new Inv());
        commands.put("tp",  new Teleport());
        commands.put("gm",  new Gamemode());
        commands.put("time",  new Time());
        commands.put("fly",  new Fly());
        commands.put("warp",  new Warp(warpFile, warps));
        commands.put("home",  new Home(homeFile, homes));

        //init listeners and register them
        listeners.put("newChunk", new NewChunk());
        registerEvents();
    }

    private void registerEvent(List<Listener> listeners) {
        for(Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerEvents() {
        List<Listener> listeners = new ArrayList<>();
        for(String str : commands.keySet()) {
            SubCommand sub = commands.get(str);
            if(sub instanceof Listener) listeners.add((Listener) sub);
        }
        registerEvent(listeners);
    }


    /*
    !!!!! STATIC METHODS !!!!!
     */



    /*
    !!!!! CUSTOM CONFIG METHODS !!!!!
     */

    private void createHomes() {
        homeFile = new File(getDataFolder(), "homes.yml");

        if (!homeFile.exists()) {
            homeFile.getParentFile().mkdirs();
            saveResource("homes.yml", false);
        }

        homes = new YamlConfiguration();

        try {
            System.out.println(homeFile);
            homes.load(homeFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void createWarps() {
        warpFile = new File(getDataFolder(), "warps.yml");

        if (!warpFile.exists()) {
            warpFile.getParentFile().mkdirs();
            saveResource("warps.yml", false);
        }

        warps = new YamlConfiguration();

        try {
            warps.load(warpFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
