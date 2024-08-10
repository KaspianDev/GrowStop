package com.github.kaspiandev.growstop;

import com.github.kaspiandev.growstop.listener.GrowListener;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class GrowStop extends JavaPlugin {

    private List<Material> supportedBlocks;

    @Override
    public void onEnable() {
        CustomBlockData.registerListener(this);

        getConfig().options().copyDefaults(true);
        saveConfig();

        supportedBlocks = getConfig().getStringList("supported-blocks").stream()
                                     .map(Material::valueOf)
                                     .toList();

        getServer().getPluginManager().registerEvents(new GrowListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean isSupported(Material blockType) {
        return supportedBlocks.contains(blockType);
    }

}
