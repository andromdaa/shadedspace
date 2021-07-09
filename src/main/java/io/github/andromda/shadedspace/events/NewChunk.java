package io.github.andromda.shadedspace.events;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
                   ItemMeta meta = fragment.getItemMeta();

                   List<String> lore;
                   if(meta != null) lore = meta.getLore();
                   else lore = new ArrayList<>();

                   lore.add("This is a test");

                   meta.setLore(lore);
                   fragment.setItemMeta(meta);

                   inventory.addItem(fragment);
               }
           }
        }
    }

}
