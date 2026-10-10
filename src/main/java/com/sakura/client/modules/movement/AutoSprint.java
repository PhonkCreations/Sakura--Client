package com.sakura.client.modules.movement;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;

public class AutoSprint extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public AutoSprint() {
        super("Auto Sprint", "Sprints while the forward key is pressed.", Category.MOVEMENT, SubCategory.MOVEMENT_BASIC);
    }

    @Override
    public void onTick() {
        if (client.player != null && client.options.forwardKey.isPressed()) {
            client.player.setSprinting(true);
        }
    }
}
