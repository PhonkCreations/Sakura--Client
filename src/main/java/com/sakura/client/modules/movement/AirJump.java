package com.sakura.client.modules.movement;

import com.sakura.client.Module;
import com.sakura.client.Category;
import com.sakura.client.SubCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class AirJump extends Module {
    private boolean wasJumpKeyPressed = false;
    private int airJumpsUsed = 0;
    private final int MAX_AIR_JUMPS = 1;

    public AirJump() {
        super("AirJump", "Permite un salto extra en el aire", Category.MOVEMENT, SubCategory.MOVEMENT_AIR);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        PlayerEntity player = mc.player;
        boolean jumpKeyCurrentlyPressed = mc.options.jumpKey.isPressed();

        // Resetear contador cuando el jugador toca el suelo
        if (player.isOnGround()) {
            airJumpsUsed = 0;
        }

        // Detectar una NUEVA pulsación de salto (transición de no-presionado a presionado)
        boolean jumpKeyJustPressed = jumpKeyCurrentlyPressed && !wasJumpKeyPressed;

        if (jumpKeyJustPressed && !player.isOnGround() && airJumpsUsed < MAX_AIR_JUMPS) {
            // Ejecutar el salto aéreo: establecer velocidad Y a 0.42
            player.setVelocity(player.getVelocity().x, 0.42, player.getVelocity().z);
            airJumpsUsed++;
        }

        // Guardar el estado anterior de la tecla para el próximo tick
        wasJumpKeyPressed = jumpKeyCurrentlyPressed;
    }
}