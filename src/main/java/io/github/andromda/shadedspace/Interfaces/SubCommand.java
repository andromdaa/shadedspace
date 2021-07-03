package io.github.andromda.shadedspace.Interfaces;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface SubCommand {
    void onCommand(Player player, Command command, String[] args);
}
