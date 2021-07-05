package io.github.andromda.shadedspace.basic;

import io.github.andromda.shadedspace.SubCommand;
import io.github.andromda.shadedspace.utilities.Utilities;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class Home implements SubCommand, Listener {
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
                if (loc != null) player.teleport(loc);
            }
        } else if (args.length == 3) {
            name = args[2];
            switch (args[1].toLowerCase()) {
                case "set":
                    //check if player has enough home slots
                    if (canCreateHome(homeAllowedString(player))) {
                        if (doesHomeExist(uuidString(player), name))
                            player.sendMessage("A home with that name already exists");
                        else createHome(player, name, false, "Home created");
                    } else player.sendMessage("You have reached the limit of homes");
                    //if not, send message stating too many homes
                    //if they do, add home under UUID in homes.yml
                    //check that the name is not already taken
                    //set home location in config,
                    //then decrement "homes available" key
                    break;
                case "remove":
                    if (!doesHomeExist(uuidString(player), name)) player.sendMessage("This home does not exist");
                    else createHome(player, name, true, "Home removed");
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
                        if (loc != null) {
                            Object[] locData = Utilities.extractLocationData(loc);
                            if (locData.length != 6) throw new UnsupportedOperationException();
                            else player.sendMessage(String.format("Dimension: %s\nX: %.2f, Y: %.2f, Z: %.2f",
                                    (String) locData[0], (float) locData[1], (float) locData[2], (float) locData[3]));
                        }
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

    private void createHome(Player player, String name, boolean remove, String confirmationMessage) {
        if (!remove) {
            Location location = player.getLocation();

            String world = location.getWorld().getName();
            double x, y, z, yaw, pitch;

            x = location.getX();
            y = location.getY();
            z = location.getZ();
            yaw = location.getYaw();
            pitch = location.getPitch();
            String path = homeString(player, name) + ".";

            homes.set(path + "world", world);
            homes.set(path + "x", x);
            homes.set(path + "y", y);
            homes.set(path + "z", z);
            homes.set(path + "pitch", pitch);
            homes.set(path + "yaw", yaw);

            decrement(true, player);
        } else {
            homes.set(homeString(player, name), null);
            decrement(false, player);
        }
        Utilities.saveCustom(homeFile, homes);
        player.sendMessage(confirmationMessage);
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

    private boolean homeIsNull(String uuid) {
        return (homes.get(uuid) == null);
    }

    @EventHandler
    public void firstJoin(PlayerJoinEvent joinEvent) {
        if (homeIsNull(joinEvent.getPlayer().getUniqueId().toString())) {
            homes.set(joinEvent.getPlayer().getUniqueId().toString().concat(".homes-allowed"), 3);
            Utilities.saveCustom(homeFile, homes);
        }
    }
}
