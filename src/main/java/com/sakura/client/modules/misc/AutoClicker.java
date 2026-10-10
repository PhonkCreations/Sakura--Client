package com.sakura.client.modules.misc;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;

public class AutoClicker extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private int tickCounter = 0;
    private int cps = 10;
    private boolean releaseAttackKeyNextTick;

    public AutoClicker() {
        super("Auto Clicker", "Pulses the attack key while it is held.", Category.MISC, SubCategory.MISC_UTIL);
    }

    @Override
    public void onTick() {
        boolean attackKeyHeld = client.options.attackKey.isPressed();
        if (releaseAttackKeyNextTick) {
            client.options.attackKey.setPressed(false);
            releaseAttackKeyNextTick = false;
        }

        tickCounter++;
        int ticksPerClick = Math.max(1, 20 / cps);
        if (tickCounter >= ticksPerClick) {
            if (client.player != null && attackKeyHeld) {
                client.options.attackKey.setPressed(true);
                releaseAttackKeyNextTick = true;
            }
            tickCounter = 0;
        }
    }

    @Override
    public void onDisable() {
        if (releaseAttackKeyNextTick) {
            client.options.attackKey.setPressed(false);
            releaseAttackKeyNextTick = false;
        }
    }
}
