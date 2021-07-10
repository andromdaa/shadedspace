package io.github.andromda.shadedspace.basic;
import io.github.andromda.shadedspace.SubCommand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;


public class TPPOS implements SubCommand {
    @Override
    public void onCommand(Player player, Command command, String[] args) {
        // ss tppos x y z
        if(args.length != 4) player.sendMessage("Please provide X Y Z");
        else {
            World world = player.getWorld();
            Location loc = new Location(world,
                    Double.parseDouble(args[1]),
                    Double.parseDouble(args[2]),
                    Double.parseDouble(args[3]));
            player.teleport(loc);
        }

    }
}
