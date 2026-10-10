package com.sakura.client.modules.movement;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class Jesus extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public Jesus() {
        super("Jesus", "Enables movement across liquid surfaces.", Category.MOVEMENT, SubCategory.MOVEMENT_GROUND);
    }

    @Override
    public void onTick() {
        if (client.player != null && client.player.isTouchingWater() && !client.player.isSneaking()) {
            Vec3d velocity = client.player.getVelocity();
            client.player.setVelocity(velocity.x, 0.15, velocity.z);
        }
    }
}
