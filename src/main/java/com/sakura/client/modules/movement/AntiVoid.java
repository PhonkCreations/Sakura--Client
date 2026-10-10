package com.sakura.client.modules.movement;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;

public class AntiVoid extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public AntiVoid() {
        super("Anti Void", "Helps prevent falling into the void.", Category.MOVEMENT, SubCategory.MOVEMENT_MISC);
    }

    @Override
    public void onTick() {
        if (client.player != null && client.player.getY() < -60) {
            client.player.setPosition(client.player.getX(), 100, client.player.getZ());
        }
    }
}
