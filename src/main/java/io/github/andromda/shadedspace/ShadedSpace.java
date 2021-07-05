package io.github.andromda.shadedspace;

import io.github.andromda.shadedspace.basic.*;
import io.github.andromda.shadedspace.utilities.Utilities;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class ShadedSpace extends JavaPlugin implements Listener {
    private File warpFile;
    private FileConfiguration warps;
    private File homeFile;
    private FileConfiguration homes;
    public final HashMap<String, SubCommand> commands = new HashMap<>();

    /*
    !!!!! CONSTRUCTOR !!!!!
     */

    public ShadedSpace() {
        createWarps();
        createHomes();

        commands.put("inv", new Inv());
        commands.put("tp", new Teleport());
        commands.put("gm", new Gamemode());
        commands.put("time", new Time());
        commands.put("fly", new Fly());
        commands.put("warp", new Warp(warpFile, warps));
        commands.put("home", new Home(homeFile, homes));
    }

    @Override
    public void onEnable() {
        ShadedSpaceBase base = new ShadedSpaceBase();
        this.getCommand("ss").setExecutor(base);

        registerCommands(base);
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

    private void registerCommands(ShadedSpaceBase base) {
        for (String str : commands.keySet()) {
            base.registerCommand(str, commands.get(str));
        }
    }

    private void registerEvent(SubCommand cmd) {
        if (cmd instanceof Listener) {
            getServer().getPluginManager().registerEvents((Listener) cmd, this);
        }
    }

    private void registerEvents() {
        for (SubCommand cmd : commands.values()) {
            registerEvent(cmd);
        }
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
