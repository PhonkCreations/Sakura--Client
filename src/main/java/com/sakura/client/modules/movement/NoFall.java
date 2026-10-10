package com.sakura.client.modules.movement;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public NoFall() {
        super("No Fall", "Mitigates fall damage.", Category.MOVEMENT, SubCategory.MOVEMENT_MISC);
    }

    @Override
    public void onTick() {
        if (client.player != null && client.player.fallDistance > 2.0f) {
            client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, false));
        }
    }
}
