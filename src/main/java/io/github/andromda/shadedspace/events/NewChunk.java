package io.github.andromda.shadedspace.events;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NewChunk implements Listener {

    @EventHandler
    public void chunkLoadEvent(ChunkLoadEvent e) {
        if(e.isNewChunk()) {
           BlockState[] tileEntities = e.getChunk().getTileEntities();
           for(BlockState blockState : tileEntities) {
               if(blockState instanceof Chest) {
                   Chest chest = (Chest) blockState;
                   Inventory inventory = chest.getBlockInventory();

                   ItemStack fragment = new ItemStack(Material.GOLD_NUGGET);
                   inventory.addItem(fragment);

                   System.out.println(blockState.getX() + " " + blockState.getY() + " " + blockState.getZ());
               }
           }
        }
    }

}
