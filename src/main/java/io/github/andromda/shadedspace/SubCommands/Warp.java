package io.github.andromda.shadedspace.SubCommands;

import io.github.andromda.shadedspace.Interfaces.SubCommand;
import io.github.andromda.shadedspace.ShadedSpace;
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

public class Warp implements SubCommand, Listener {
    private final ShadedSpace plugin;
    private Player player = null;
    private String name = null;
    private final FileConfiguration warps;
    private final File warpFile;

    public Warp(final ShadedSpace plugin, File warpFile, FileConfiguration warps) {
        this.plugin = plugin;
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
        this.name = args[2];
        try {
            switch (args[1].toLowerCase()) {
                case "create":
                    if (warpExists(name)) throw new NullPointerException("Warp already exists");
                    else setLocation(player, name, "Warp created");
                    break;
                case "redefine":
                    if (!warpExists(name)) throw new NullPointerException("Warp name does not exist!");
                    else setLocation(player, name, "Warp refined");
                    break;
                case "remove":
                    if (!warpExists(name)) throw new NullPointerException("Warp name does not exist!");
                    else {
                        this.player = player;
                        player.sendMessage("Please enter \"confirm\" to remove");
                        break;
                    }
                case "info":
                    break;
                default:
                    Location location = warps.getLocation(name);
                    player.sendMessage(String.valueOf(location));
                    break;
            }
        } catch (NullPointerException exception) {
            player.sendMessage(ChatColor.RED + exception.getMessage());
        }

    }

    private void removeWarp(String name) {
        warps.set(name, null);
        saveWarps();
    }

    private boolean warpExists(String path) {
        return warps.getLocation(path) != null;
    }

    private void setLocation(Player player, String name, String confirmationMessage) {
        warps.set(name, player.getLocation());
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
