package com.sakura.client.modules.movement;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;

public class Sprint extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public Sprint() {
        super("Sprint", "Automatically maintains sprinting.", Category.MOVEMENT, SubCategory.MOVEMENT_BASIC);
    }

    @Override
    public void onTick() {
        if (client.player != null && client.player.input != null) {
            client.player.setSprinting(true);
        }
    }
}
