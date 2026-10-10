package com.sakura.client.modules.player;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;

public class FastPlace extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private int tickCounter = 0;
    private boolean releaseUseKeyNextTick;

    public FastPlace() {
        super("Fast Place", "Adjusts the delay between placing blocks.", Category.PLAYER, SubCategory.PLAYER_AUTO);
    }

    @Override
    public void onTick() {
        boolean useKeyHeld = client.options.useKey.isPressed();
        if (releaseUseKeyNextTick) {
            client.options.useKey.setPressed(false);
            releaseUseKeyNextTick = false;
        }

        tickCounter++;
        if (client.player != null && client.currentScreen == null
                && useKeyHeld && tickCounter >= 2) {
            client.options.useKey.setPressed(true);
            releaseUseKeyNextTick = true;
            tickCounter = 0;
        }
    }

    @Override
    public void onDisable() {
        if (releaseUseKeyNextTick) {
            client.options.useKey.setPressed(false);
            releaseUseKeyNextTick = false;
        }
        tickCounter = 0;
    }
}
