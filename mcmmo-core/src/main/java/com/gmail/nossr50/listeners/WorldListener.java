package com.gmail.nossr50.listeners;

import com.gmail.nossr50.mcMMO;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.io.File;

public class WorldListener implements Listener {
    private final mcMMO pluginRef;

    public WorldListener(final mcMMO pluginRef) {
        this.pluginRef = pluginRef;
    }

    /**
     * Monitor StructureGrow events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        /* WORLD BLACKLIST CHECK */
        if (pluginRef.getDynamicSettingsManager().isWorldBlacklisted(event.getWorld().getName()))
            return;

        if (!pluginRef.getPlaceStore().isTrue(event.getLocation().getBlock())) {
            return;
        }

        for (BlockState blockState : event.getBlocks()) {
            pluginRef.getPlaceStore().setFalse(blockState);
        }
    }

    /**
     * Monitor WorldInit events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldInit(WorldInitEvent event) {
        /* WORLD BLACKLIST CHECK */
        if (pluginRef.getDynamicSettingsManager().isWorldBlacklisted(event.getWorld().getName()))
            return;

        World world = event.getWorld();

        if (!new File(world.getWorldFolder(), "mcmmo_data").exists() || pluginRef == null) {
            return;
        }

        pluginRef.getLogger().info("Converting block storage for " + world.getName() + " to a new format.");

        //new BlockStoreConversionMain(world).run();
    }

    /**
     * Monitor WorldUnload events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        /* WORLD BLACKLIST CHECK */
        if (pluginRef.getDynamicSettingsManager().isWorldBlacklisted(event.getWorld().getName()))
            return;

        pluginRef.getPlaceStore().unloadWorld(event.getWorld());
    }

    /**
     * Monitor ChunkUnload events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        /* WORLD BLACKLIST CHECK */
        if (pluginRef.getDynamicSettingsManager().isWorldBlacklisted(event.getWorld().getName()))
            return;

        Chunk chunk = event.getChunk();

        pluginRef.getPlaceStore().chunkUnloaded(chunk.getX(), chunk.getZ(), event.getWorld());
    }
}