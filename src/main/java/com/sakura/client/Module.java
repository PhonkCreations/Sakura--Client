package com.sakura.client;

/**
 * Clase base de todos los módulos del Sakura Client.
 * Cada hack (Fly, Fullbright, etc.) extiende esta clase.
 */
public abstract class Module {

    protected final String name;
    protected final String description;
    protected final Category category;
    protected final SubCategory subCategory;
    protected boolean enabled;
    protected int keybind = -1; // -1 = sin tecla asignada

    protected Module(String name, String description, Category category) {
        this(name, description, category, SubCategory.firstFor(category));
    }

    protected Module(String name, String description, Category category, SubCategory subCategory) {
        this.name = name;
        this.description = description;
        this.category = category;
        if (subCategory.getParent() != category) {
            throw new IllegalArgumentException("Subcategory parent does not match module category");
        }
        this.subCategory = subCategory;
        this.enabled = false;
    }

    // ===== Ciclo de vida =====

    /** Se ejecuta cuando el módulo se activa */
    public void onEnable() {}

    /** Se ejecuta cuando el módulo se desactiva */
    public void onDisable() {}

    /** Se ejecuta cada tick del cliente (20 veces/seg) */
    public void onTick() {}

    // ===== Toggle =====

    public void toggle() {
        this.enabled = !this.enabled;
        if (this.enabled) {
            onEnable();
        } else {
            onDisable();
        }
        Config.save();
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
        Config.save();
    }

    // ===== Getters (necesarios para la GUI) =====

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }
}