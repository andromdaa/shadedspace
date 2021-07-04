package io.github.andromda.shadedspace;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ShadedSpaceBase implements CommandExecutor {
    private final Map<String, SubCommand> commands = new HashMap<>();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) player.sendMessage("Help Menu");

            else {
                if (!commands.containsKey(args[0].toLowerCase())) {
                    player.sendMessage("This command does not exist");
                    return false;
                } else commands.get(args[0].toLowerCase()).onCommand(player, command, args);
            }

        }
        return true;
    }


    public void registerCommand(String cmd, SubCommand subCommand) {
        commands.put(cmd, subCommand);
    }
}