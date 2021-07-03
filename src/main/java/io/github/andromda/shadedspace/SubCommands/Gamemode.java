package io.github.andromda.shadedspace.SubCommands;

import io.github.andromda.shadedspace.Interfaces.SubCommand;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class Gamemode implements SubCommand {

    @Override
    public void onCommand(Player player, Command command, String[] args) {
        if (args.length == 1) {
            player.sendMessage("You must specify a gamemode");
        } else {
            switch (args[1].toLowerCase()) {
                case "1":
                case "c":
                case "creative":
                    player.setGameMode(GameMode.CREATIVE);
                    break;
                case "0":
                case "s":
                case "survival":
                    player.setGameMode(GameMode.SURVIVAL);
                    break;
            }
        }
    }
}
