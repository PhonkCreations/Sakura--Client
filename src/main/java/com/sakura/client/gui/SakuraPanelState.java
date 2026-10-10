package com.sakura.client.gui;

import com.sakura.client.Category;
import com.sakura.client.SubCategory;

import java.util.EnumMap;
import java.util.Map;

public final class SakuraPanelState {
    private static final Map<Category, SakuraPanelState> PANELS = new EnumMap<>(Category.class);

    static {
        for (Category category : Category.values()) {
            PANELS.put(category, new SakuraPanelState(category));
        }
    }

    private final Category category;
    private final Map<SubCategory, SectionState> sections = new EnumMap<>(SubCategory.class);
    private int x = 20;
    private int y = 20;
    private boolean positioned;

    private SakuraPanelState(Category category) {
        this.category = category;
    }

    public static SakuraPanelState forCategory(Category category) {
        return PANELS.get(category);
    }

    public static Map<Category, SakuraPanelState> allPanels() {
        return Map.copyOf(PANELS);
    }

    public Category getCategory() {
        return category;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        positioned = true;
    }

    public boolean hasPosition() {
        return positioned;
    }

    public float getScale() {
        return category == Category.HUD ? 0.75f : 1.0f;
    }

    public SectionState section(SubCategory subCategory) {
        return sections.computeIfAbsent(subCategory, ignored -> new SectionState());
    }

    public Map<SubCategory, SectionState> getSections() {
        return Map.copyOf(sections);
    }

    public void setSectionExpanded(SubCategory subCategory, boolean expanded) {
        section(subCategory).setExpanded(expanded);
    }

    public static final class SectionState {
        private boolean expanded;
        private double animationProgress;
        private int x;
        private int y;

        public boolean isExpanded() {
            return expanded;
        }

        public double getAnimationProgress() {
            return animationProgress;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void toggle() {
            expanded = !expanded;
        }

        public void advanceAnimation() {
            double target = expanded ? 1.0 : 0.0;
            animationProgress += (target - animationProgress) * 0.2;
            if (Math.abs(target - animationProgress) < 0.001) {
                animationProgress = target;
            }
        }

        private void setExpanded(boolean expanded) {
            this.expanded = expanded;
            animationProgress = expanded ? 1.0 : 0.0;
        }
    }
}
