package com.sakura.client.modules.movement;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class NoSlow extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public NoSlow() {
        super("No Slow", "Reduces movement slowdown from actions.", Category.MOVEMENT, SubCategory.MOVEMENT_GROUND);
    }

    @Override
    public void onTick() {
        if (client.player != null && client.player.isUsingItem()) {
            Vec3d velocity = client.player.getVelocity();
            client.player.setVelocity(velocity.x * 1.2, velocity.y, velocity.z);
        }
    }
}
