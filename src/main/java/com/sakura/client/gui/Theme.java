package com.sakura.client.gui;

/**
 * Paleta de colores del tema Sakura.
 * Centralizado aquí para poder cambiar todo el look desde un solo archivo.
 */
public final class Theme {

    private Theme() {} // no instanciable

    // ====== COLORES PRINCIPALES (ARGB) ======
    public static final int BACKGROUND = 0xCC1A0A14;
    public static final int PANEL = 0xCC26121E;
    public static final int BORDER = 0xFFE91E63;
    public static final int BORDER_INNER = 0x80FF80AB;

    // ====== COLORES DE ACENTO ======
    public static final int SAKURA_PINK = 0xFFFF69B4;
    public static final int SAKURA_LIGHT = 0xFFFFB7D5;
    public static final int SAKURA_WHITE = 0xFFFFE0EC;
    public static final int ENABLED = 0xFF4CAF50;
    public static final int DISABLED = 0xFFE53935;
    public static final int HOVER = 0xFFFFEB3B;

    // ====== COLORES DE PÉTALOS ======
    public static final int[] PETAL_COLORS = {
            0xFFFFB7C5,
            0xFFFF69B4,
            0xFFFFC0CB,
            0xFFFFE0EC,
            0xFFF8BBD0,
    };

    // ====== DIMENSIONES ======
    public static final int HEADER_HEIGHT = 24;
    public static final int MODULE_HEIGHT = 18;
    public static final int PANEL_WIDTH = 120;
    public static final int PANEL_SPACING = 8;
    public static final int PADDING = 4;
}
