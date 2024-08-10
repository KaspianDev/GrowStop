package com.github.kaspiandev.growstop.command;

import com.github.kaspiandev.growstop.GrowStop;
import com.github.kaspiandev.growstop.command.subcommand.ReloadSubcommand;

import java.util.HashMap;
import java.util.Map;

public class SubCommandRegistry {

    private final GrowStop plugin;
    private final Map<String, SubCommand> registry;

    public SubCommandRegistry(GrowStop plugin) {
        this.plugin = plugin;
        this.registry = new HashMap<>();
        load();
    }

    private void load() {
        ReloadSubcommand reloadSubcommand = new ReloadSubcommand(plugin);
        registry.put(reloadSubcommand.getType().getKey(), reloadSubcommand);
    }

    public Map<String, SubCommand> getRegistry() {
        return registry;
    }

    public SubCommand findById(String id) {
        return registry.get(id);
    }

}
