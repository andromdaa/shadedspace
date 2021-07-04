package io.github.andromda.shadedspace.basic;

import io.github.andromda.shadedspace.SubCommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class Time implements SubCommand {

    @Override
    public void onCommand(Player player, Command command, String[] args) {
        World world = player.getWorld();
        switch (args[0]) {
            case "day":
                setTime(player, world, 0L);
                break;
            case "night":
                setTime(player, world, 12541L);
                break;
            case "noon":
                setTime(player, world, 6000L);
                break;
        }
    }

    private void setTime(Player player, World world, long time) {
        world.setTime(time);
        player.sendMessage("Time changed");
    }
}
