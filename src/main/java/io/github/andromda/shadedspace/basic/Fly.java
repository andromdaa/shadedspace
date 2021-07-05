package io.github.andromda.shadedspace.basic;

import io.github.andromda.shadedspace.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashSet;
import java.util.Set;

public class Fly implements SubCommand, Listener {
    Set<Player> flyingPlayers = new HashSet<>();

    @EventHandler
    public void cancelFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (flyingPlayers.contains(player)) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public void onCommand(Player player, Command command, String[] args) {
        // ss fly
        if (player.hasPermission("shadedspace.fly") && args.length == 1) {
            flyingPlayers.add(player);
            player.setFlying(true);
        }
    }
}
