package com.github.kaspiandev.growstop.command;

import com.github.kaspiandev.growstop.util.ColorUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    private final SubCommands type;
    private final String message;
    private final String permission;

    protected SubCommand(SubCommands type, String message) {
        this.type = type;
        this.message = message;
        this.permission = type.getPermission();
    }

    public SubCommands getType() {
        return type;
    }

    protected abstract void execute(CommandSender sender, String[] args);

    public abstract List<String> suggestions(CommandSender sender, String[] args);

    public void checkPerms(CommandSender sender, String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.spigot().sendMessage(ColorUtil.component(message));
            return;
        }

        execute(sender, args);
    }

}
