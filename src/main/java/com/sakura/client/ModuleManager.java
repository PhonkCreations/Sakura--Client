package com.sakura.client;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    /** Registra un módulo en el sistema. */
    public void register(Module module) {
        modules.add(module);
    }

    /** Devuelve la lista completa de módulos (para la GUI). */
    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getBySubCategory(SubCategory subCategory) {
        return modules.stream()
                .filter(module -> module.getSubCategory() == subCategory)
                .toList();
    }

    public List<SubCategory> getSubCategoriesFor(Category category) {
        return java.util.Arrays.stream(SubCategory.values())
                .filter(subCategory -> subCategory.getParent() == category)
                .toList();
    }

    /** Busca un módulo por nombre (case-insensitive). */
    public Module get(String name) {
        return modules.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /** Llamado cada tick del cliente. Actualiza los módulos activos. */
    public void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }
}