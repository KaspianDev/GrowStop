package com.github.kaspiandev.growstop;

import com.github.kaspiandev.growstop.command.MainCommand;
import com.github.kaspiandev.growstop.command.SubCommandRegistry;
import com.github.kaspiandev.growstop.listener.GrowListener;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public final class GrowStop extends JavaPlugin {

    private List<Material> supportedBlocks;
    private final Supplier<List<String>> blockNameCache = Suppliers.memoizeWithExpiration(
            () -> getSupportedBlocks().stream()
                                      .map(Material::name)
                                      .map(String::toLowerCase)
                                      .toList(),
            1,
            TimeUnit.MINUTES);

    @Override
    public void onEnable() {
        CustomBlockData.registerListener(this);

        getConfig().options().copyDefaults(true);
        saveConfig();

        loadBlocks();

        PluginCommand pluginCommand = getCommand("growstop");
        if (pluginCommand != null) {
            SubCommandRegistry subCommandRegistry = new SubCommandRegistry(this);
            MainCommand mainCommand = new MainCommand(this, subCommandRegistry);

            pluginCommand.setTabCompleter(mainCommand);
            pluginCommand.setExecutor(mainCommand);
        }

        getServer().getPluginManager().registerEvents(new GrowListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public List<Material> getSupportedBlocks() {
        return supportedBlocks;
    }

    public List<String> getBlockNameCache() {
        return blockNameCache.get();
    }

    public void removeSupportedBlock(Material blockType) {
        if (!supportedBlocks.contains(blockType)) return;

        supportedBlocks = supportedBlocks.stream()
                                         .filter((type) -> type != blockType)
                                         .toList();
        getConfig().set("supported-blocks", supportedBlocks.stream()
                                                           .map(Material::name)
                                                           .toList());
        saveConfig();
    }

    public void addSupportedBlock(Material blockType) {
        if (supportedBlocks.contains(blockType)) return;

        supportedBlocks = Stream.concat(supportedBlocks.stream(), Stream.of(blockType))
                                .toList();
        getConfig().set("supported-blocks", supportedBlocks.stream()
                                                           .map(Material::name)
                                                           .toList());
        saveConfig();
    }

    public boolean isSupported(Material blockType) {
        return supportedBlocks.contains(blockType);
    }

    public void loadBlocks() {
        supportedBlocks = getConfig().getStringList("supported-blocks").stream()
                                     .map(Material::valueOf)
                                     .toList();

    }

}
