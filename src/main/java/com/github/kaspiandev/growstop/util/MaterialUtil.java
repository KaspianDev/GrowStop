package com.github.kaspiandev.growstop.util;

import org.bukkit.Material;

import java.util.StringJoiner;

public class MaterialUtil {

    private MaterialUtil() {}

    public static String formatName(Material material) {
        String name = material.name().toLowerCase();

        if (name.indexOf('_') == -1) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            String[] nameParts = name.split("_");

            StringJoiner nameJoiner = new StringJoiner(" ");
            for (String part : nameParts) {
                nameJoiner.add(part.substring(0, 1).toUpperCase() + part.substring(1));
            }

            return nameJoiner.toString();
        }
    }

}
