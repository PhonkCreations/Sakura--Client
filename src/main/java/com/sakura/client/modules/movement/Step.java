package com.sakura.client.modules.movement;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;

public class Step extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public Step() {
        super("Step", "Adjusts the height of automatic stepping.", Category.MOVEMENT, SubCategory.MOVEMENT_GROUND);
    }

    @Override
    public void onTick() {
        if (client.player != null && client.player.isOnGround() && client.player.horizontalCollision) {
            client.player.setPosition(client.player.getX(), client.player.getY() + 0.5, client.player.getZ());
        }
    }
}
