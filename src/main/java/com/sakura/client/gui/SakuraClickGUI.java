package com.sakura.client.gui;

import com.sakura.client.Category;
import com.sakura.client.Config;
import com.sakura.client.Module;
import com.sakura.client.ModuleManager;
import com.sakura.client.SubCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Sakura ClickGUI - pantalla principal del cliente.
 * Se abre/cierra con Right Shift (registrado en SakuraClient).
 */
public class SakuraClickGUI extends Screen {

    private final ModuleManager moduleManager;
    private final PetalRenderer petalRenderer;

    private Category draggingCategory = null;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    private static final int PANEL_WIDTH = Theme.PANEL_WIDTH;
    private static final int HEADER_HEIGHT = Theme.HEADER_HEIGHT;
    private static final int SUBCATEGORY_HEIGHT = Theme.MODULE_HEIGHT;
    private static final int MODULE_HEIGHT = Theme.MODULE_HEIGHT;
    private static final int PADDING = Theme.PADDING;

    public SakuraClickGUI(ModuleManager moduleManager) {
        super(Text.literal("Sakura Client"));
        this.moduleManager = moduleManager;
        this.petalRenderer = new PetalRenderer(40);
    }

    @Override
    protected void init() {
        super.init();
        petalRenderer.setScreenSize(this.width, this.height);

        int x = 20;
        int y = 20;

        for (Category category : Category.values()) {
            SakuraPanelState state = SakuraPanelState.forCategory(category);
            int panelHeight = HEADER_HEIGHT + PADDING * 2;
            for (SubCategory subCategory : getVisibleSubCategories(category)) {
                panelHeight += SUBCATEGORY_HEIGHT + moduleManager.getBySubCategory(subCategory).size() * MODULE_HEIGHT;
            }

            if (!state.hasPosition()) {
                state.setPosition(x, y);
            }

            y += panelHeight + Theme.PANEL_SPACING;

            if (y + 100 > this.height) {
                x += PANEL_WIDTH + Theme.PANEL_SPACING;
                y = 20;
            }
        }
    }

    private List<SubCategory> getVisibleSubCategories(Category category) {
        return moduleManager.getSubCategoriesFor(category).stream()
                .filter(subCategory -> !moduleManager.getBySubCategory(subCategory).isEmpty())
                .toList();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        petalRenderer.update();
        renderPetals(context);

        for (Category category : Category.values()) {
            renderCategoryPanel(context, category, mouseX, mouseY);
        }

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("🌸 Sakura Client v1.0 🌸"),
                this.width / 2,
                8,
                Theme.SAKURA_PINK
        );

        context.fill(this.width - 22, 4, this.width - 4, 22, Theme.BACKGROUND);
        context.drawTextWithShadow(this.textRenderer, "X", this.width - 16, 8, Theme.SAKURA_WHITE);
        context.drawText(
                this.textRenderer,
                "Sakura Client v1.0 | " + moduleManager.getModules().size() + " modules",
                4,
                this.height - 12,
                Theme.SAKURA_WHITE,
                false
        );

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderPetals(DrawContext context) {
        for (PetalRenderer.Petal petal : petalRenderer.getPetals()) {
            int px = (int) petal.x;
            int py = (int) petal.y;
            int s = petal.size;
            context.fill(px, py, px + s, py + s, petal.color);
        }
    }

    private void renderCategoryPanel(DrawContext context, Category category, int mouseX, int mouseY) {
        SakuraPanelState panel = SakuraPanelState.forCategory(category);
        float scale = panel.getScale();
        double localMouseX = (mouseX - panel.getX()) / scale;
        double localMouseY = (mouseY - panel.getY()) / scale;
        int panelHeight = HEADER_HEIGHT + PADDING * 2;
        List<SubCategory> subCategories = getVisibleSubCategories(category);

        for (SubCategory subCategory : subCategories) {
            SakuraPanelState.SectionState section = panel.section(subCategory);
            section.advanceAnimation();
            int maxHeight = moduleManager.getBySubCategory(subCategory).size() * MODULE_HEIGHT;
            panelHeight += SUBCATEGORY_HEIGHT + (int) Math.ceil(section.getAnimationProgress() * maxHeight);
        }

        var matrices = context.getMatrices();
        matrices.push();
        matrices.translate(panel.getX(), panel.getY(), 0);
        matrices.scale(scale, scale, 1.0f);

        context.fill(0, 0, PANEL_WIDTH, panelHeight, Theme.PANEL);
        context.drawBorder(0, 0, PANEL_WIDTH, panelHeight, Theme.BORDER);

        int headerColor = draggingCategory == category ? Theme.SAKURA_PINK : Theme.BACKGROUND;
        context.fill(0, 0, PANEL_WIDTH, HEADER_HEIGHT, headerColor);
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal(category.getDisplay()),
                PANEL_WIDTH / 2,
                (HEADER_HEIGHT - 8) / 2,
                Theme.SAKURA_WHITE
        );

        Text hoveredTooltip = null;
        int sectionY = HEADER_HEIGHT;
        for (SubCategory subCategory : subCategories) {
            SakuraPanelState.SectionState section = panel.section(subCategory);
            section.setPosition(0, sectionY);
            List<Module> modules = moduleManager.getBySubCategory(subCategory);
            double progress = section.getAnimationProgress();
            int visibleHeight = (int) Math.ceil(progress * modules.size() * MODULE_HEIGHT);

            context.fill(2, sectionY, PANEL_WIDTH - 2, sectionY + SUBCATEGORY_HEIGHT, 0xAA100914);
            String arrow = section.isExpanded() ? "▼ " : "▶ ";
            context.drawTextWithShadow(
                    this.textRenderer,
                    Text.literal(arrow + subCategory.getDisplayName()),
                    PADDING,
                    sectionY + (SUBCATEGORY_HEIGHT - 8) / 2,
                    Theme.SAKURA_LIGHT
            );

            int modulesY = sectionY + SUBCATEGORY_HEIGHT;
            for (int index = 0; index < modules.size(); index++) {
                int rowY = modulesY + index * MODULE_HEIGHT;
                int rowVisibleHeight = Math.min(MODULE_HEIGHT, visibleHeight - index * MODULE_HEIGHT);
                if (rowVisibleHeight <= 0) {
                    continue;
                }

                Module module = modules.get(index);
                boolean hovered = localMouseX >= 0 && localMouseX <= PANEL_WIDTH
                        && localMouseY >= rowY && localMouseY <= rowY + rowVisibleHeight;
                context.fill(2, rowY, PANEL_WIDTH - 2, rowY + rowVisibleHeight,
                        withAlpha(hovered ? 0x66FFFFFF : 0x22000000, progress));

                int textColor = module.isEnabled() ? Theme.ENABLED : Theme.DISABLED;
                context.drawTextWithShadow(
                        this.textRenderer,
                        Text.literal(module.getName()),
                        PADDING,
                        rowY + (MODULE_HEIGHT - 8) / 2,
                        withAlpha(textColor, progress)
                );

                String status = module.isEnabled() ? "ON" : "OFF";
                int statusWidth = this.textRenderer.getWidth(status);
                context.drawTextWithShadow(
                        this.textRenderer,
                        Text.literal(status),
                        PANEL_WIDTH - PADDING - statusWidth,
                        rowY + (MODULE_HEIGHT - 8) / 2,
                        withAlpha(textColor, progress)
                );

                if (hovered) {
                    hoveredTooltip = Text.literal(module.getDescription());
                }
            }
            sectionY += SUBCATEGORY_HEIGHT + visibleHeight;
        }
        matrices.pop();
        if (hoveredTooltip != null) {
            context.drawTooltip(this.textRenderer, hoveredTooltip, mouseX, mouseY);
        }
    }

    private int withAlpha(int color, double opacity) {
        int alpha = (int) (((color >>> 24) & 0xFF) * opacity);
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (mouseX >= this.width - 22 && mouseX <= this.width - 4
                    && mouseY >= 4 && mouseY <= 22) {
                close();
                return true;
            }

            for (Category category : Category.values()) {
                SakuraPanelState panel = SakuraPanelState.forCategory(category);
                float scale = panel.getScale();
                double localX = (mouseX - panel.getX()) / scale;
                double localY = (mouseY - panel.getY()) / scale;

                if (localX >= 0 && localX <= PANEL_WIDTH && localY >= 0 && localY <= HEADER_HEIGHT) {
                    draggingCategory = category;
                    dragOffsetX = mouseX - panel.getX();
                    dragOffsetY = mouseY - panel.getY();
                    return true;
                }
            }

            for (Category category : Category.values()) {
                SakuraPanelState panel = SakuraPanelState.forCategory(category);
                float scale = panel.getScale();
                double localX = (mouseX - panel.getX()) / scale;
                double localY = (mouseY - panel.getY()) / scale;
                if (localX < 0 || localX > PANEL_WIDTH) {
                    continue;
                }

                int sectionY = HEADER_HEIGHT;
                for (SubCategory subCategory : getVisibleSubCategories(category)) {
                    SakuraPanelState.SectionState section = panel.section(subCategory);
                    List<Module> modules = moduleManager.getBySubCategory(subCategory);
                    double progress = section.getAnimationProgress();
                    if (localY >= sectionY && localY <= sectionY + SUBCATEGORY_HEIGHT) {
                        section.toggle();
                        Config.save();
                        return true;
                    }

                    int visibleHeight = (int) Math.ceil(progress * modules.size() * MODULE_HEIGHT);
                    int modulesY = sectionY + SUBCATEGORY_HEIGHT;
                    if (localY >= modulesY && localY < modulesY + visibleHeight) {
                        int moduleIndex = (int) ((localY - modulesY) / MODULE_HEIGHT);
                        if (moduleIndex >= 0 && moduleIndex < modules.size()) {
                            modules.get(moduleIndex).toggle();
                            return true;
                        }
                    }
                    sectionY += SUBCATEGORY_HEIGHT + visibleHeight;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingCategory != null && button == 0) {
            SakuraPanelState.forCategory(draggingCategory).setPosition(
                    (int) (mouseX - dragOffsetX),
                    (int) (mouseY - dragOffsetY)
            );
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingCategory != null && button == 0) {
            draggingCategory = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void removed() {
        Config.save();
        super.removed();
    }
}