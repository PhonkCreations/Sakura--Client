package com.sakura.client.modules.render;

import com.sakura.client.Category;
import com.sakura.client.Module;
import com.sakura.client.SubCategory;
import com.sakura.client.SakuraClient;

public class XRay extends Module {
    public XRay() {
        super("X-Ray", "Highlights selected blocks through terrain.", Category.RENDER, SubCategory.RENDER_WORLD);
    }

    @Override
    public void onEnable() {
        SakuraClient.LOGGER.info("[SakuraClient] X-Ray is a placeholder; chunk rendering is not modified.");
    }

    @Override
    public void onDisable() {
        SakuraClient.LOGGER.info("[SakuraClient] X-Ray disabled.");
    }
}
