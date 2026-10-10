package com.sakura.client.modules.movement;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public Speed() {
        super("Speed", "Adjusts player movement speed.", Category.MOVEMENT, SubCategory.MOVEMENT_GROUND);
    }

    @Override
    public void onTick() {
        if (client.player != null && client.player.isOnGround()) {
            Vec3d velocity = client.player.getVelocity();
            if (velocity.x != 0 || velocity.z != 0) {
                client.player.setVelocity(velocity.x * 1.3, velocity.y, velocity.z * 1.3);
            }
        }
    }
}
