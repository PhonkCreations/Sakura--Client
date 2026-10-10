package com.sakura.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sistema de pétalos sakura cayendo.
 */
public class PetalRenderer {

    public static class Petal {
        public float x, y;
        public float speedY;
        public float sway;
        public float swaySpeed;
        public float swayPhase;
        public int size;
        public int color;
        public float rotation;
        public float rotationSpeed;

        public Petal(float x, float y, int size, int color, Random random) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.speedY = 0.3f + random.nextFloat() * 0.7f;
            this.sway = 20f + random.nextFloat() * 30f;
            this.swaySpeed = 0.02f + random.nextFloat() * 0.03f;
            this.swayPhase = random.nextFloat() * (float) (Math.PI * 2);
            this.rotation = random.nextFloat() * 360f;
            this.rotationSpeed = (random.nextFloat() - 0.5f) * 2f;
        }
    }

    private final List<Petal> petals = new ArrayList<>();
    private final Random random = new Random();
    private int screenWidth = 1920;
    private int screenHeight = 1080;

    public PetalRenderer(int initialCount) {
        for (int i = 0; i < initialCount; i++) {
            petals.add(createRandomPetal(true));
        }
    }

    private Petal createRandomPetal(boolean randomY) {
        int size = 6 + random.nextInt(10);
        int color = Theme.PETAL_COLORS[random.nextInt(Theme.PETAL_COLORS.length)];
        float x = random.nextFloat() * screenWidth;
        float y = randomY ? random.nextFloat() * screenHeight : -20f;
        return new Petal(x, y, size, color, random);
    }

    public void update() {
        for (Petal p : petals) {
            p.y += p.speedY;
            p.swayPhase += p.swaySpeed;
            p.x += (float) Math.sin(p.swayPhase) * 0.5f;
            p.rotation += p.rotationSpeed;

            if (p.y > screenHeight + 20) {
                p.y = -20f;
                p.x = random.nextFloat() * screenWidth;
            }
            if (p.x < -20) p.x = screenWidth + 20;
            if (p.x > screenWidth + 20) p.x = -20;
        }
    }

    public List<Petal> getPetals() {
        return petals;
    }

    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }
}