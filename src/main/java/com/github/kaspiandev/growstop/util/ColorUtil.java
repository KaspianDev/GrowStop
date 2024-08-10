package com.github.kaspiandev.growstop.util;

import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;

public class ColorUtil {

    private ColorUtil() {}

    public static String string(String message) {
        return BaseComponent.toLegacyText(component(message));
    }

    public static BaseComponent[] component(String message) {
        return new MineDown(message).toComponent();
    }

}
