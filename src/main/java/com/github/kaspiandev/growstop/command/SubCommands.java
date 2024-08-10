package com.github.kaspiandev.growstop.command;

public enum SubCommands {

    RELOAD("reload", "growstop.command.reload");

    private final String key;
    private final String permission;

    SubCommands(String key, String permission) {
        this.key = key;
        this.permission = permission;
    }

    public String getKey() {
        return key;
    }

    public String getPermission() {
        return permission;
    }

}
