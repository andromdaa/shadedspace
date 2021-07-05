package io.github.andromda.shadedspace.basic;

import io.github.andromda.shadedspace.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class Warp implements SubCommand, Listener {
    private Player player = null;
    private String name = null;
    private final FileConfiguration warps;
    private final File warpFile;

    public Warp(File warpFile, FileConfiguration warps) {
        this.warps = warps;
        this.warpFile = warpFile;
    }

    @EventHandler
    public void asyncChatEvent(AsyncPlayerChatEvent chatEvent) {
        Player sender = chatEvent.getPlayer();
        if (sender.equals(player)) {
            String message = chatEvent.getMessage();
            if (message.equalsIgnoreCase("confirm")) {
                removeWarp(name);
                chatEvent.setCancelled(true);
                this.name = null;
                this.player = null;
            }
        }
    }

    @Override
    public void onCommand(Player player, Command command, String[] args) {
        this.player = player;
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("list")) {
                Set<String> warpList = warps.getKeys(false);
                if (warpList.isEmpty()) player.sendMessage("There are currently no warps");
                else player.sendMessage("Warps: " + String.valueOf(warpList).replace("[", "").replace("]", ""));
            } else {
                Location location = warps.getLocation(name);
                player.teleport(Objects.requireNonNull(location));
            }
        } else {
            try {
                this.name = args[2];
                switch (args[1].toLowerCase()) {
                    case "create":
                        if (warpExists(name)) throw new NullPointerException("Warp already exists");
                        else setLocation(player, name, "Warp created");
                        break;
                    case "redefine":
                        if (!warpExists(name)) throw new NullPointerException("Warp name does not exist!");
                        else setLocation(player, name, "Warp redefined");
                        break;
                    case "remove":
                        if (!warpExists(name)) throw new NullPointerException("Warp name does not exist!");
                        else player.sendMessage("Please enter \"confirm\" to remove");
                        break;
                    case "info":
                        Location loc = (Location) warps.get(name);
                        String world = Objects.requireNonNull(Objects.requireNonNull(loc).getWorld()).getName();

                        int x = (int) loc.getX();
                        int y = (int) loc.getY();
                        int z = (int) loc.getZ();
                        int distance = (int) loc.toVector().distance(player.getLocation().toVector());

                        player.sendMessage(String.format("World: %s\nX: %s Y: %s Z: %s\nDistance: %d blocks", world, x, y, z, distance));
                        break;
                    default:
                        player.sendMessage("Invalid command!");
                }

            } catch (NullPointerException exception) {
                player.sendMessage(ChatColor.RED + exception.getMessage());
            }
        }
    }

    private void removeWarp(String name) {
        warps.set(name, null);
        saveWarps();
        player.sendMessage("Warp removed");
    }

    private boolean warpExists(String path) {
        return warps.getLocation(path) != null;
    }

    private void setLocation(Player player, String name, String confirmationMessage) {
        Location location = player.getLocation();

        String world = location.getWorld().getName();
        double x, y, z, pitch, yaw;
        x = location.getX();
        y = location.getY();
        z = location.getZ();
        pitch = location.getPitch();
        yaw = location.getYaw();
        warps.set(name + ".world", world);
        warps.set(name + ".x", x);
        warps.set(name + ".y", y);
        warps.set(name + ".z", z);
        warps.set(name + ".pitch", pitch);
        warps.set(name + ".yaw", yaw);
        saveWarps();
        player.sendMessage(ChatColor.RED + confirmationMessage);
    }

    private void saveWarps() {
        try {
            warps.save(warpFile);
        } catch (IOException e) {
            player.sendMessage(ChatColor.RED + "An error occurred. Please contact administrators.");
        }
    }
}
