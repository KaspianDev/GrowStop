package com.github.kaspiandev.growstop.command.subcommand;

import com.github.kaspiandev.growstop.GrowStop;
import com.github.kaspiandev.growstop.command.SubCommand;
import com.github.kaspiandev.growstop.command.SubCommands;
import com.github.kaspiandev.growstop.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RemoveSubcommand extends SubCommand {

    private final GrowStop plugin;

    public RemoveSubcommand(GrowStop plugin) {
        super(SubCommands.REMOVE, plugin.getConfig().getString("message.no-perms"));
        this.plugin = plugin;
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
                plugin.removeSupportedBlock(blockType);
                sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.removed")));
            } else {
                sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.cannot-remove")));
            }
        } catch (IllegalArgumentException ex) {
            sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.invalid-block")));
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return plugin.getBlockNameCache();
        }
        return null;
    }

}
