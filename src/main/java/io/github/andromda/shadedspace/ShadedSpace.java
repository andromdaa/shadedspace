package io.github.andromda.shadedspace;

import io.github.andromda.shadedspace.basic.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ShadedSpace extends JavaPlugin implements Listener {
    private File warpFile;
    private FileConfiguration warps;
    private File homeFile;
    private FileConfiguration homes;

    @EventHandler
    public void firstJoin(PlayerJoinEvent joinEvent) {
        if (homeIsNull(joinEvent.getPlayer().getUniqueId().toString())) {
            homes.set(joinEvent.getPlayer().getUniqueId().toString().concat(".homes-allowed"), 3);
            saveCustom(homeFile, homes);
        }
    }

    public static void saveCustom(File file, FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean homeIsNull(String uuid) {
        return (homes.get(uuid) == null);
    }

    @Override
    public void onEnable() {
        createWarps();
        createHomes();
        this.saveDefaultConfig();

        Inv inv = new Inv();
        Teleport teleport = new Teleport();
        Gamemode gamemode = new Gamemode();
        Warp warp = new Warp(warpFile, warps);
        Home home = new Home(homeFile, homes);
        Time time = new Time();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(inv, this);
        getServer().getPluginManager().registerEvents(warp, this);
        ShadedSpaceBase base = new ShadedSpaceBase();
        this.getCommand("ss").setExecutor(base);

        base.registerCommand("inv", inv);
        base.registerCommand("tp", teleport);
        base.registerCommand("teleport", teleport);
        base.registerCommand("gm", gamemode);
        base.registerCommand("gamemode", gamemode);
        base.registerCommand("warp", warp);
        base.registerCommand("home", home);
        base.registerCommand("time", time);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void createHomes() {
        homeFile = new File(getDataFolder(), "homes.yml");
        if (!homeFile.exists()) {
            homeFile.getParentFile().mkdirs();
            saveResource("homes.yml", false);
        }

        homes = new YamlConfiguration();

        try {
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
