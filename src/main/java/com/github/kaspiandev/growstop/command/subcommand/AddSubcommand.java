package com.github.kaspiandev.growstop.command.subcommand;

import com.github.kaspiandev.growstop.GrowStop;
import com.github.kaspiandev.growstop.command.SubCommand;
import com.github.kaspiandev.growstop.command.SubCommands;
import com.github.kaspiandev.growstop.util.ColorUtil;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AddSubcommand extends SubCommand {

    private final GrowStop plugin;
    private final Supplier<List<String>> materialNamecache;

    public AddSubcommand(GrowStop plugin) {
        super(SubCommands.ADD, plugin.getConfig().getString("message.no-perms"));
        this.plugin = plugin;
        this.materialNamecache = Suppliers.memoizeWithExpiration(
                () -> Arrays.stream(Material.values())
                            .filter(Material::isBlock)
                            .filter((material) -> {
                                BlockData blockData = material.createBlockData();
                                return blockData instanceof Ageable || blockData instanceof Sapling;
                            })
                            .filter((material) -> !plugin.isSupported(material))
                            .map(Material::name)
                            .map(String::toLowerCase)
                            .toList(),
                30,
                TimeUnit.SECONDS);
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.no-args")));
            return;
        }

        try {
            Material blockType = Material.valueOf(args[1].toUpperCase());
            if (plugin.isSupported(blockType)) {
                sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.already-added")));
            } else {
                plugin.addSupportedBlock(blockType);
                sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.added")));
            }
        } catch (IllegalArgumentException ex) {
            sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.invalid-block")));
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], materialNamecache.get(), new ArrayList<>());
        }
        return null;
    }

}
