package com.sakura.client;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    WORLD("World"),
    MISC("Misc"),
    HUD("HUD");

    private final String display;

    Category(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
