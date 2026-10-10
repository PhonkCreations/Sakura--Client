package com.sakura.client.modules.render;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;

public class Fullbright extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public Fullbright() {
        super("Fullbright", "Brightens the world", Category.RENDER, SubCategory.RENDER_WORLD);
    }

    @Override
    public void onEnable() {
        client.options.getGamma().setValue(10.0);
    }

    @Override
    public void onTick() {
        client.options.getGamma().setValue(10.0);
    }

    @Override
    public void onDisable() {
        client.options.getGamma().setValue(1.0);
    }
}
