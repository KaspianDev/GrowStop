package com.github.kaspiandev.growstop.command;


import com.github.kaspiandev.growstop.GrowStop;
import com.github.kaspiandev.growstop.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements TabExecutor {

    private final GrowStop plugin;
    private final SubCommandRegistry registry;

    public MainCommand(GrowStop plugin, SubCommandRegistry registry) {
        this.plugin = plugin;
        this.registry = registry;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (args.length < 1) {
            sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.no-args")));
            return false;
        } else {
            SubCommand cmd = registry.findById(args[0]);
            if (cmd == null) {
                sender.spigot().sendMessage(ColorUtil.component(plugin.getConfig().getString("message.unknown-arg")));
                return false;
            }
            cmd.checkPerms(sender, args);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
                                      String label, String[] args) {
        if (args.length <= 1) {
            return new ArrayList<>(registry.getRegistry().keySet());
        } else {
            SubCommand subCommand = registry.findById(args[0]);
            if (subCommand == null) return null;

            return subCommand.suggestions(sender, args);
        }
    }

}
