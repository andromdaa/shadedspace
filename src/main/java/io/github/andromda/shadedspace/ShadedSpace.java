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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ShadedSpace extends JavaPlugin implements Listener {
    private final File warpFile = new File(getDataFolder(), "warps.yml");
    private FileConfiguration warps = createWarps();
    private final File homeFile = new File(getDataFolder(), "homes.yml");;
    private FileConfiguration homes = createHomes();
    public final Map<String, SubCommand> commands = Map.of(
            "inv", new Inv(),
            "tp",  new Teleport(),
            "gm",  new Gamemode(),
            "time",  new Time(),
            "fly",  new Fly(),
            "warp",  new Warp(warpFile, warps),
            "home",  new Home(homeFile, homes),
            "tppos", new TPPOS()
    );
    private final Map<String, Listener> listeners = Map.of("newChunk", new NewChunk());

    @Override
    public void onEnable() {
        ShadedSpaceBase base = new ShadedSpaceBase();
        Objects.requireNonNull(this.getCommand("ss")).setExecutor(base);

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

    private void registerCommands(ShadedSpaceBase base)  {
        commands.keySet().forEach(cmd -> {
            base.registerCommand(cmd, commands.get(cmd));
        });
    }

    private void registerEvents() {
        List<Listener> listeners = new ArrayList<>();

        commands.values().forEach(command -> {
            if(command instanceof Listener) listeners.add((Listener) command);
        });

        this.listeners.values().forEach(listener -> {
            if(listener != null) listeners.add(listener);
        });

        for(Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }


    /*
    !!!!! STATIC METHODS !!!!!
     */



    /*
    !!!!! CUSTOM CONFIG METHODS !!!!!
     */

    private FileConfiguration createHomes() {
//        homeFile = new File(getDataFolder(), "homes.yml");

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

        return homes;
    }

    private FileConfiguration createWarps() {
//        warpFile = new File(getDataFolder(), "warps.yml");

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
        return warps;
    }
}
