package io.github.andromda.shadedspace.basic;

import io.github.andromda.shadedspace.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class Inv implements SubCommand, Listener {
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        List<HumanEntity> viewers = e.getViewers();
        for (HumanEntity entity : viewers) {
            Player player = (Player) entity;
            if (!player.hasPermission("ss.inv.interact")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to interact with this player's inventory");
                e.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
        }
    }

    @Override
    public void onCommand(Player player, Command command, String[] args) {
        if (args.length == 2) {
            Player otherPlayer = Bukkit.getPlayer(args[1]);
            if (otherPlayer != null) {
                Inventory playerInv = otherPlayer.getInventory();
                player.openInventory(playerInv);
            } else player.sendMessage(ChatColor.RED + "That player does not exist.");
        } else
            player.sendMessage(ChatColor.RED + "Invalid amount of arguments\n/ss inv {player}");
    }
}
