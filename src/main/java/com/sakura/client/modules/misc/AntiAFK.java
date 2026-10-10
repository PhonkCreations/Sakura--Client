package com.sakura.client.modules.misc;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;

public class AntiAFK extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private int tickCounter = 0;

    public AntiAFK() {
        super("Anti AFK", "Performs actions to avoid inactivity.", Category.MISC, SubCategory.MISC_ANTI);
    }

    @Override
    public void onTick() {
        if (client.player == null) {
            return;
        }

        tickCounter++;
        if (tickCounter >= 100) {
            client.player.setYaw(client.player.getYaw() + 1.0f);
            tickCounter = 0;
        }
    }
}
