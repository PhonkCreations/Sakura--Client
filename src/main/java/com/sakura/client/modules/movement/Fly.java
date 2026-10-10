package com.sakura.client.modules.movement;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;

public class Fly extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public Fly() {
        super("Fly", "Enables configurable flight movement.", Category.MOVEMENT, SubCategory.MOVEMENT_AIR);
    }

    @Override
    public void onEnable() {
        if (client.player != null) {
            client.player.getAbilities().allowFlying = true;
        }
    }

    @Override
    public void onDisable() {
        if (client.player != null && !client.player.isCreative()) {
            client.player.getAbilities().allowFlying = false;
            client.player.getAbilities().flying = false;
        }
    }
}
