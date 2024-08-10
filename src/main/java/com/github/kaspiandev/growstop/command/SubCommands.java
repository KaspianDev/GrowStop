package com.github.kaspiandev.growstop.command;

public enum SubCommands {

    RELOAD("reload", "growstop.command.reload"),
    ADD("add", "growstop.command.add"),
    REMOVE("remove", "growstop.command.remove");

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
