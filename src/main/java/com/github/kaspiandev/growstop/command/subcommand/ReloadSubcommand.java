package com.github.kaspiandev.growstop.command.subcommand;

import com.github.kaspiandev.growstop.GrowStop;
import com.github.kaspiandev.growstop.command.SubCommand;
import com.github.kaspiandev.growstop.command.SubCommands;
import com.github.kaspiandev.growstop.util.ColorUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadSubcommand extends SubCommand {

    private final GrowStop plugin;

    public ReloadSubcommand(GrowStop plugin) {
        super(SubCommands.RELOAD, plugin.getConfig().getString("message.no-perms"));
        this.plugin = plugin;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        plugin.loadBlocks();
        sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.reloaded")));
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        return List.of();
    }

}
