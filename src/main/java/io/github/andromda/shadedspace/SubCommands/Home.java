package io.github.andromda.shadedspace.SubCommands;

import io.github.andromda.shadedspace.Interfaces.SubCommand;
import io.github.andromda.shadedspace.ShadedSpace;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

public class Home implements SubCommand {
    private final File homeFile;
    private final FileConfiguration homes;

    public Home(File homeFile, FileConfiguration homes) {
        this.homeFile = homeFile;
        this.homes = homes;
    }

    @Override
    public void onCommand(Player player, Command command, String[] args) {
        String name;
        if (args.length == 2) {
            name = args[1];
            if (!doesHomeExist(uuidString(player), name)) player.sendMessage("This home does not exist");
            else {
                Location loc = homes.getLocation(homeString(player, name));
                player.teleport(loc);
            }
        } else if (args.length == 3) {
            name = args[2];
            switch (args[1].toLowerCase()) {
                case "set":
                    //check if player has enough home slots
                    if (canCreateHome(homeAllowedString(player))) {
                        if (doesHomeExist(uuidString(player), name))
                            player.sendMessage("A home with that name already exists");
                        else {
                            homes.set(player.getUniqueId().toString().concat("." + name), player.getLocation());
                            decrement(true, player);
                            ShadedSpace.saveCustom(homeFile, homes);
                        }
                    } else player.sendMessage("You have reached the limit of homes");
                    //if not, send message stating too many homes
                    //if they do, add home under UUID in homes.yml
                    //check that the name is not already taken
                    //set home location in config,
                    //then decrement "homes available" key
                    break;
                case "remove":
                    if (!doesHomeExist(uuidString(player), name)) player.sendMessage("This home does not exist");
                    else {
                        homes.set(homeString(player, name), null);
                        decrement(false, player);
                        ShadedSpace.saveCustom(homeFile, homes);
                        player.sendMessage("Home removed");
                    }
                    /*
                    check if home name exists for player
                    if not, throw error.
                    if there, set key to null, removing home
                    send player a message confirming
                    increment "homes-available" key.
                     */
                    break;
                case "info":
                    if (!doesHomeExist(uuidString(player), name)) player.sendMessage("This home does not exist");
                    else {
                        Location loc = homes.getLocation(homeString(player, name));
                        String world = Objects.requireNonNull(Objects.requireNonNull(loc).getWorld()).getName();
                        double x, y, z;
                        x = loc.getX();
                        y = loc.getY();
                        z = loc.getZ();
                        player.sendMessage(String.format("Dimension: %s\nX: %.2f, Y: %.2f, Z: %.2f",
                                world, x, y, z));
                    }
                    break;
            }
        }
    }

    private String homeString(Player player, String name) {
        return uuidString(player).concat("." + name);
    }

    private String uuidString(Player player) {
        return player.getUniqueId().toString();
    }

    private String homeAllowedString(Player player) {
        return player.getUniqueId().toString().concat(".homes-allowed");
    }

    private boolean doesHomeExist(String uuid, String name) {
        return homes.get(uuid.concat("." + name)) != null;
    }

    private void decrement(boolean decrement, Player player) {
        int homeAmount = homes.getInt(player.getUniqueId() + ".homes-allowed");
        if (decrement) homes.set(player.getUniqueId().toString().concat(".homes-allowed"), --homeAmount);
        else homes.set(player.getUniqueId().toString().concat(".homes-allowed"), ++homeAmount);
    }

    private boolean canCreateHome(String path) {
        int homesAllowed = homes.getInt(path);
        return homesAllowed > 0;
    }
}
