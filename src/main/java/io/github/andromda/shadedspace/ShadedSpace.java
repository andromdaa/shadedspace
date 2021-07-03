package io.github.andromda.shadedspace;

import io.github.andromda.shadedspace.BaseCommand.ShadedSpaceBase;
import io.github.andromda.shadedspace.SubCommands.Gamemode;
import io.github.andromda.shadedspace.SubCommands.Inv;
import io.github.andromda.shadedspace.SubCommands.Teleport;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShadedSpace extends JavaPlugin {

    @Override
    public void onEnable() {
        Inv inv = new Inv();
        Teleport teleport = new Teleport();
        Gamemode gamemode = new Gamemode();

        getServer().getPluginManager().registerEvents(inv, this);

        ShadedSpaceBase base = new ShadedSpaceBase();
        this.getCommand("ss").setExecutor(base);

        base.registerCommand("inv", inv);
        base.registerCommand("tp", teleport);
        base.registerCommand("teleport", teleport);
        base.registerCommand("gm", gamemode);
        base.registerCommand("gamemode", gamemode);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
