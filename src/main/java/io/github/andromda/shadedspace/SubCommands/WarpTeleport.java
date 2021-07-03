package io.github.andromda.shadedspace.SubCommands;

import io.github.andromda.shadedspace.Interfaces.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class WarpTeleport implements SubCommand {
    @Override
    public void onCommand(Player player, Command command, String[] args) {
        // correct format is /ss warp {warpName}

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "/ss warp {destination}");
        } else {

        }
    }
}
