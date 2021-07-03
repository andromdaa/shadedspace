package io.github.andromda.shadedspace;

import io.github.andromda.shadedspace.BaseCommand.ShadedSpaceBase;
import io.github.andromda.shadedspace.SubCommands.Gamemode;
import io.github.andromda.shadedspace.SubCommands.Inv;
import io.github.andromda.shadedspace.SubCommands.Teleport;
import io.github.andromda.shadedspace.SubCommands.Warp;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ShadedSpace extends JavaPlugin {
    private File warpFile;
    private FileConfiguration warps;

    @Override
    public void onEnable() {
        createWarps();
        this.saveDefaultConfig();

        Inv inv = new Inv();
        Teleport teleport = new Teleport();
        Gamemode gamemode = new Gamemode();
        Warp warp = new Warp(this, warpFile, warps);


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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FileConfiguration getWarps() {
        return this.warps;
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
