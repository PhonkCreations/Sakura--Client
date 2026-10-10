package com.sakura.client.modules.hud;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;

public class FPS extends Module {
    public FPS() {
        super("FPS", "Shows current FPS on screen", Category.HUD, SubCategory.HUD_INFO);
    }
}
