package com.sakura.client.modules.render;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import com.sakura.client.SakuraClient;

public class Tracers extends Module {
    public Tracers() {
        super("Tracers", "Draws indicators toward entities.", Category.RENDER, SubCategory.RENDER_ENTITY);
    }

    @Override
    public void onEnable() {
        SakuraClient.LOGGER.info("[SakuraClient] Tracers is a placeholder; entity projection is not implemented.");
    }
}
