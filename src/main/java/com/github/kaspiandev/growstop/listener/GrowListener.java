package com.github.kaspiandev.growstop.listener;

import com.github.kaspiandev.growstop.GrowStop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class GrowListener implements Listener {

    private final GrowStop plugin;

    public GrowListener(GrowStop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGrow(BlockSpreadEvent event) {

    }

}
