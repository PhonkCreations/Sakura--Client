package com.sakura.client.modules.player;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import com.sakura.client.SakuraClient;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.util.hit.BlockHitResult;

public class AutoTool extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public AutoTool() {
        super("Auto Tool", "Selects an appropriate tool for mining.", Category.PLAYER, SubCategory.PLAYER_AUTO);
    }

    @Override
    public void onEnable() {
        SakuraClient.LOGGER.info("[SakuraClient] AutoTool is a placeholder; tool selection is not implemented yet.");
    }

    @Override
    public void onDisable() {
        SakuraClient.LOGGER.info("[SakuraClient] AutoTool disabled.");
    }

    @Override
    public void onTick() {
        if (client.player == null || client.world == null || client.interactionManager == null
                || !client.interactionManager.isBreakingBlock()
                || !(client.crosshairTarget instanceof BlockHitResult blockHit)) {
            return;
        }

        BlockState blockState = client.world.getBlockState(blockHit.getBlockPos());
        if (blockState.getBlock() == Blocks.AIR) {
            return;
        }

        PlayerInventory inventory = client.player.getInventory();
        int bestSlot = -1;
        float bestSpeed = 0.0f;
        for (int slot = 0; slot < PlayerInventory.getHotbarSize(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof MiningToolItem)) {
                continue;
            }

            float speed = stack.getMiningSpeedMultiplier(blockState);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = slot;
            }
        }

        if (bestSlot >= 0) {
            inventory.selectedSlot = bestSlot;
        }
    }
}
