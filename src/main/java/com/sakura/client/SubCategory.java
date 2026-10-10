package com.sakura.client;

public enum SubCategory {
    MOVEMENT_BASIC(Category.MOVEMENT, "Basic"),
    MOVEMENT_AIR(Category.MOVEMENT, "Air"),
    MOVEMENT_GROUND(Category.MOVEMENT, "Ground"),
    MOVEMENT_MISC(Category.MOVEMENT, "Misc"),
    COMBAT_BASIC(Category.COMBAT, "Basic"),
    COMBAT_AUTO(Category.COMBAT, "Auto"),
    RENDER_ENTITY(Category.RENDER, "Entity"),
    RENDER_WORLD(Category.RENDER, "World"),
    RENDER_HUD(Category.RENDER, "HUD"),
    PLAYER_AUTO(Category.PLAYER, "Auto"),
    PLAYER_INVENTORY(Category.PLAYER, "Inventory"),
    WORLD_BUILD(Category.WORLD, "Build"),
    WORLD_BREAK(Category.WORLD, "Break"),
    MISC_UTIL(Category.MISC, "Utilities"),
    MISC_ANTI(Category.MISC, "Anti"),
    HUD_INFO(Category.HUD, "Info"),
    HUD_VISUAL(Category.HUD, "Visual");

    private final Category parent;
    private final String displayName;

    SubCategory(Category parent, String displayName) {
        this.parent = parent;
        this.displayName = displayName;
    }

    public Category getParent() {
        return parent;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SubCategory firstFor(Category category) {
        for (SubCategory subCategory : values()) {
            if (subCategory.parent == category) {
                return subCategory;
            }
        }
        throw new IllegalArgumentException("No subcategory defined for " + category);
    }
}
