package com.github.kaspiandev.growstop.command.subcommand;

import com.github.kaspiandev.growstop.GrowStop;
import com.github.kaspiandev.growstop.command.SubCommand;
import com.github.kaspiandev.growstop.command.SubCommands;
import com.github.kaspiandev.growstop.util.ColorUtil;
import com.github.kaspiandev.growstop.util.MaterialUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListSubcommand extends SubCommand {

    private final GrowStop plugin;

    public ListSubcommand(GrowStop plugin) {
        super(SubCommands.LIST, plugin.getConfig().getString("message.no-perms"));
        this.plugin = plugin;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.list")));
        plugin.getSupportedBlocks().forEach((blockType) -> {
            sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.list-element")
                                                                  .replace("${block}", MaterialUtil.formatName(blockType))));
        });
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        return List.of();
    }

}
