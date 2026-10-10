package com.sakura.client.modules.hud;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;

public class PingHUD extends Module {
    public PingHUD() {
        super("PingHUD", "Shows your ping to the server", Category.HUD, SubCategory.HUD_INFO);
    }
}
