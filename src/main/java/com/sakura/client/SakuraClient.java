package com.sakura.client;

import com.sakura.client.gui.SakuraClickGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SakuraClient implements ClientModInitializer {

    public static final String MOD_ID = "sakuraclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ModuleManager MODULES = new ModuleManager();

    /** Keybind para abrir/cerrar la GUI. Tecla fija: Right Shift. */
    private static KeyBinding guiKeybind;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[SakuraClient] Blooming... 🌸");

        // ===== Keybind (Right Shift) =====
        guiKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sakuraclient.opengui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.sakuraclient"
        ));

        // ===== Registro de módulos =====
        MODULES.register(new com.sakura.client.modules.render.Fullbright());

        MODULES.register(new com.sakura.client.modules.movement.Sprint());
        MODULES.register(new com.sakura.client.modules.movement.Fly());
        MODULES.register(new com.sakura.client.modules.movement.NoFall());
        MODULES.register(new com.sakura.client.modules.movement.AirJump());

        MODULES.register(new com.sakura.client.modules.hud.Watermark());
        MODULES.register(new com.sakura.client.modules.hud.FPS());
        MODULES.register(new com.sakura.client.modules.hud.Coords());
        MODULES.register(new com.sakura.client.modules.hud.ArmorHUD());
        MODULES.register(new com.sakura.client.modules.hud.PotionHUD());
        MODULES.register(new com.sakura.client.modules.hud.PingHUD());

        MODULES.register(new com.sakura.client.modules.movement.Speed());
        MODULES.register(new com.sakura.client.modules.movement.NoSlow());
        MODULES.register(new com.sakura.client.modules.movement.AntiVoid());
        MODULES.register(new com.sakura.client.modules.movement.Jesus());
        MODULES.register(new com.sakura.client.modules.movement.AutoSprint());
        MODULES.register(new com.sakura.client.modules.movement.Step());
        MODULES.register(new com.sakura.client.modules.misc.AutoClicker());
        MODULES.register(new com.sakura.client.modules.misc.AntiAFK());
        MODULES.register(new com.sakura.client.modules.player.FastPlace());
        MODULES.register(new com.sakura.client.modules.player.AutoTool());
        MODULES.register(new com.sakura.client.modules.render.XRay());
        MODULES.register(new com.sakura.client.modules.render.Tracers());

        Config.load();
        Runtime.getRuntime().addShutdownHook(new Thread(Config::save, "SakuraClient config save"));

        // ===== HUD Render (legacy, simple y funcional) =====
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) {
                return;
            }

            int y = 5;
            if (Config.isWatermarkVisible() && com.sakura.client.modules.hud.Watermark.shouldRender()) {
                y = drawHudText(drawContext, client, "Sakura Client v1.0", y);
            }
            if (isModuleEnabled("FPS")) {
                y = drawHudText(drawContext, client, "FPS: " + client.getCurrentFps(), y);
            }
            if (isModuleEnabled("Coords")) {
                y = drawHudText(drawContext, client, "XYZ: " + (int) client.player.getX() + " / "
                        + (int) client.player.getY() + " / " + (int) client.player.getZ(), y);
            }
            if (isModuleEnabled("PingHUD")) {
                var networkHandler = client.getNetworkHandler();
                var playerListEntry = networkHandler == null ? null
                        : networkHandler.getPlayerListEntry(client.player.getUuid());
                if (playerListEntry != null) {
                    y = drawHudText(drawContext, client, "Ping: " + playerListEntry.getLatency() + "ms", y);
                }
            }
            if (isModuleEnabled("ArmorHUD")) {
                int armorCount = 0;
                for (var stack : client.player.getInventory().armor) {
                    if (!stack.isEmpty()) {
                        armorCount++;
                    }
                }
                y = drawHudText(drawContext, client, "Armor: " + armorCount + "/4", y);
            }
            if (isModuleEnabled("PotionHUD")) {
                for (var effect : client.player.getStatusEffects()) {
                    String effectName = net.minecraft.text.Text.translatable(
                            effect.getEffectType().value().getTranslationKey()
                    ).getString();
                    int durationSeconds = effect.getDuration() / 20;
                    y = drawHudText(drawContext, client, effectName + " " + durationSeconds + "s", y);
                }
            }
        });

        // ===== Tick del cliente =====
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MODULES.onTick();

            while (guiKeybind.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new SakuraClickGUI(MODULES));
                } else if (client.currentScreen instanceof SakuraClickGUI) {
                    client.setScreen(null);
                }
            }
        });
    }

    private static boolean isModuleEnabled(String name) {
        Module module = MODULES.get(name);
        return module != null && module.isEnabled();
    }

    private static int drawHudText(net.minecraft.client.gui.DrawContext context, MinecraftClient client, String text, int y) {
        context.drawText(client.textRenderer, text, 5, y, 0xFFFFE0EC, true);
        return y + 12;
    }
}