package com.sakura.client.modules.hud;

import com.sakura.client.Module;
import com.sakura.client.Category;
import com.sakura.client.SubCategory;

public class Watermark extends Module {
    public Watermark() {
        super("Watermark", "Muestra el nombre del cliente en la pantalla", Category.HUD, SubCategory.HUD_VISUAL);
    }

    public static boolean shouldRender() {
        Module module = com.sakura.client.SakuraClient.MODULES.get("Watermark");
        return module != null && module.isEnabled();
    }
}