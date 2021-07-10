package io.github.andromda.shadedspace.basic;

import io.github.andromda.shadedspace.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;


public class Teleport implements SubCommand {

    @Override
    public void onCommand(Player player, Command command, String[] args) {
        Player destPlayer;
        switch (args.length) {
            case 1:
                player.sendMessage("Usage: /ss tp {player} [toPlayer]");
                break;
            case 2:
                destPlayer = Bukkit.getPlayer(args[1]);
                if (destPlayer == null) {
                    player.sendMessage("Player is not online");
                    break;
                }
                player.teleport(destPlayer.getLocation());
                break;
            case 3:
                destPlayer = Bukkit.getPlayer(args[1]);
                try {
                    destPlayer.teleport(player);
                } catch (NullPointerException e) {
                    player.sendMessage("Player does not exist");
                }
                break;
        }
    }

}
