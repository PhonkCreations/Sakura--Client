package com.sakura.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.sakura.client.gui.SakuraPanelState;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = MinecraftClient.getInstance().runDirectory.toPath()
            .resolve("config")
            .resolve("sakuraclient.json");
    private static volatile boolean loading;
    private static GlobalSettings globalSettings = new GlobalSettings();

    private Config() {
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            return;
        }

        loading = true;
        try {
            Configuration configuration = GSON.fromJson(Files.readString(CONFIG_PATH), Configuration.class);
            if (configuration == null) {
                return;
            }

            if (configuration.modules != null) {
                for (ModuleConfiguration moduleConfiguration : configuration.modules) {
                    if (moduleConfiguration == null || moduleConfiguration.name == null) {
                        continue;
                    }
                    Module module = SakuraClient.MODULES.get(moduleConfiguration.name);
                    if (module != null) {
                        module.setKeybind(moduleConfiguration.keybind);
                        module.setEnabled(moduleConfiguration.enabled);
                    }
                }
            }

            if (configuration.panels != null) {
                for (PanelConfiguration panelConfiguration : configuration.panels) {
                    if (panelConfiguration == null || panelConfiguration.category == null) {
                        continue;
                    }
                    try {
                        SakuraPanelState.forCategory(Category.valueOf(panelConfiguration.category))
                                .setPosition(panelConfiguration.x, panelConfiguration.y);
                    } catch (IllegalArgumentException exception) {
                        SakuraClient.LOGGER.warn("[SakuraClient] Ignoring unknown panel category: {}", panelConfiguration.category);
                    }
                }
            }

            if (configuration.subCategories != null) {
                for (SubCategoryConfiguration subCategoryConfiguration : configuration.subCategories) {
                    if (subCategoryConfiguration == null || subCategoryConfiguration.subCategory == null) {
                        continue;
                    }
                    try {
                        SubCategory subCategory = SubCategory.valueOf(subCategoryConfiguration.subCategory);
                        SakuraPanelState.forCategory(subCategory.getParent())
                                .setSectionExpanded(subCategory, subCategoryConfiguration.expanded);
                    } catch (IllegalArgumentException exception) {
                        SakuraClient.LOGGER.warn("[SakuraClient] Ignoring unknown subcategory: {}", subCategoryConfiguration.subCategory);
                    }
                }
            }

            if (configuration.globalSettings != null) {
                globalSettings = configuration.globalSettings;
            }
        } catch (IOException | JsonParseException exception) {
            SakuraClient.LOGGER.error("[SakuraClient] Failed to load config from {}", CONFIG_PATH, exception);
        } finally {
            loading = false;
        }
    }

    public static synchronized void save() {
        if (loading) {
            return;
        }

        Configuration configuration = new Configuration();
        for (Module module : SakuraClient.MODULES.getModules()) {
            ModuleConfiguration moduleConfiguration = new ModuleConfiguration();
            moduleConfiguration.name = module.getName();
            moduleConfiguration.enabled = module.isEnabled();
            moduleConfiguration.keybind = module.getKeybind();
            configuration.modules.add(moduleConfiguration);
        }

        for (SakuraPanelState panel : SakuraPanelState.allPanels().values()) {
            PanelConfiguration panelConfiguration = new PanelConfiguration();
            panelConfiguration.category = panel.getCategory().name();
            panelConfiguration.x = panel.getX();
            panelConfiguration.y = panel.getY();
            configuration.panels.add(panelConfiguration);

            for (SubCategory subCategory : SakuraClient.MODULES.getSubCategoriesFor(panel.getCategory())) {
                SubCategoryConfiguration subCategoryConfiguration = new SubCategoryConfiguration();
                subCategoryConfiguration.subCategory = subCategory.name();
                subCategoryConfiguration.expanded = panel.section(subCategory).isExpanded();
                configuration.subCategories.add(subCategoryConfiguration);
            }
        }
        configuration.globalSettings = globalSettings;

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(configuration));
        } catch (IOException exception) {
            SakuraClient.LOGGER.error("[SakuraClient] Failed to save config to {}", CONFIG_PATH, exception);
        }
    }

    public static boolean isWatermarkVisible() {
        return globalSettings.watermarkVisible;
    }

    public static void setWatermarkVisible(boolean visible) {
        globalSettings.watermarkVisible = visible;
        save();
    }

    private static final class Configuration {
        private List<ModuleConfiguration> modules = new ArrayList<>();
        private List<PanelConfiguration> panels = new ArrayList<>();
        private List<SubCategoryConfiguration> subCategories = new ArrayList<>();
        private GlobalSettings globalSettings = new GlobalSettings();
    }

    private static final class ModuleConfiguration {
        private String name;
        private boolean enabled;
        private int keybind = -1;
    }

    private static final class PanelConfiguration {
        private String category;
        private int x;
        private int y;
    }

    private static final class SubCategoryConfiguration {
        private String subCategory;
        private boolean expanded;
    }

    private static final class GlobalSettings {
        private boolean watermarkVisible = true;
    }
}
