package com.github.kaspiandev.growstop.listener;

import com.github.kaspiandev.growstop.GrowStop;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;

public class GrowListener implements Listener {

    private final GrowStop plugin;
    private final NamespacedKey growStopKey;

    public GrowListener(GrowStop plugin) {
        this.plugin = plugin;
        this.growStopKey = new NamespacedKey(plugin, "grow-stop");
    }

    @EventHandler(ignoreCancelled = true)
    public void onShearUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        EquipmentSlot hand = event.getHand();
        if (hand == null) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(hand);
        if (item == null || item.getType() != Material.SHEARS) return;

        Block block = event.getClickedBlock();
        assert block != null; // Block cannot be null on RIGHT_CLICK_BLOCK
        if (!plugin.isSupported(block.getType())) return;

        CustomBlockData blockData = new CustomBlockData(block, plugin);
        if (blockData.has(growStopKey)) return;

        blockData.set(growStopKey, PersistentDataType.BOOLEAN, true);

        Damageable meta = (Damageable) item.getItemMeta();
        assert meta != null;

        meta.setDamage(meta.getDamage() + 1);
        item.setItemMeta(meta);

        if (hand == EquipmentSlot.HAND) player.swingMainHand();
        else player.swingOffHand();

        player.playSound(block.getLocation(), Sound.BLOCK_GROWING_PLANT_CROP, 1, 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSpread(BlockSpreadEvent event) {
        CustomBlockData blockData = new CustomBlockData(event.getSource(), plugin);
        if (blockData.getOrDefault(growStopKey, PersistentDataType.BOOLEAN, false)) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGrow(BlockGrowEvent event) {
        CustomBlockData blockData = new CustomBlockData(event.getBlock(), plugin);
        if (blockData.getOrDefault(growStopKey, PersistentDataType.BOOLEAN, false)) event.setCancelled(true);
    }

}
