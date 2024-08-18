package com.github.kaspiandev.growstop.listener;

import com.github.kaspiandev.growstop.GrowStop;
import com.github.kaspiandev.growstop.event.GrowStopTagEvent;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
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

        Block block = event.getClickedBlock();
        assert block != null; // Block cannot be null on RIGHT_CLICK_BLOCK

        Material blockType = block.getType();
        if (!plugin.isSupported(blockType)) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("growstop.use.*")
                && !player.hasPermission("growstop.use." + blockType.name().toLowerCase())) return;

        ItemStack item = player.getInventory().getItem(hand);
        if (item == null) return;

        Material type = item.getType();
        if (type != Material.SHEARS) return;

        CustomBlockData blockData = new CustomBlockData(block, plugin);
        if (blockData.has(growStopKey)) return;

        Damageable meta = (Damageable) item.getItemMeta();
        assert meta != null;

        int damage = meta.getDamage();
        int durabilityCost = plugin.getConfig().getInt("durability-cost");
        int newDamage = damage + durabilityCost;

        int newDurability = type.getMaxDurability() - newDamage;
        if (newDurability == 0) {
            GrowStopTagEvent growStopTagEvent = new GrowStopTagEvent(player);
            Bukkit.getServer().getPluginManager().callEvent(growStopTagEvent);
            if (growStopTagEvent.isCancelled()) return;

            item.setType(Material.AIR);
        } else if (newDurability < 0) {
            return;
        } else {
            GrowStopTagEvent growStopTagEvent = new GrowStopTagEvent(player);
            Bukkit.getServer().getPluginManager().callEvent(growStopTagEvent);
            if (growStopTagEvent.isCancelled()) return;

            meta.setDamage(newDamage);
            item.setItemMeta(meta);
        }

        blockData.set(growStopKey, PersistentDataType.BOOLEAN, true);

        if (hand == EquipmentSlot.HAND) player.swingMainHand();
        else player.swingOffHand();

        player.playSound(block.getLocation(), Sound.BLOCK_GROWING_PLANT_CROP, 1, 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSpread(BlockSpreadEvent event) {
        if (checkBlock(event.getSource())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGrow(BlockGrowEvent event) {
        Block block = event.getBlock();
        if (block.getType().isAir()) {
            block = block.getRelative(BlockFace.DOWN); // Special cases like SUGAR_CANE
        }
        if (checkBlock(block)) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGrow(StructureGrowEvent event) {
        Block block = event.getLocation().getBlock();
        if (checkBlock(block)) {
            if (block instanceof Sapling sapling) {
                sapling.setStage(Math.max(0, sapling.getStage()) - 1);
            }
            event.setCancelled(true);
        }
    }

    private boolean checkBlock(Block block) {
        CustomBlockData blockData = new CustomBlockData(block, plugin);
        Material blockType = block.getType();
        boolean hasData = blockData.getOrDefault(growStopKey, PersistentDataType.BOOLEAN, false);
        if (hasData) {
            if (plugin.isSupported(blockType)) return true;
            blockData.remove(growStopKey);
        }
        return false;
    }

}
