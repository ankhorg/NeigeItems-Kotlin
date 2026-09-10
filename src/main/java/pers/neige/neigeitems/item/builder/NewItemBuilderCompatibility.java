package pers.neige.neigeitems.item.builder;

import org.bukkit.inventory.ItemFlag;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;

final class NewItemBuilderCompatibility {
    private NewItemBuilderCompatibility() {
    }

    static boolean hasHideFlag(int hideFlag, String flagName) {
        try {
            return (hideFlag & (1 << ItemFlag.valueOf(flagName).ordinal())) != 0;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    static void applyCustomModelData(RefNmsItemStack itemStack, int customModelData) {
        if (CbVersion.v1_21_R3.isSupport()) {
            NewItemBuilderCustomModelDataModern.apply(itemStack, customModelData);
        } else {
            NewItemBuilderCustomModelDataLegacy.apply(itemStack, customModelData);
        }
    }

    static void applyHideFlags(RefNmsItemStack itemStack, int hideFlag) {
        if (CbVersion.v1_21_R4.isSupport()) {
            NewItemBuilderHideFlagsModern.apply(itemStack, hideFlag);
        } else {
            NewItemBuilderHideFlagsLegacy.apply(itemStack, hideFlag);
        }
    }
}

final class NewItemBuilderCustomModelDataLegacy {
    private NewItemBuilderCustomModelDataLegacy() {
    }

    static void apply(RefNmsItemStack itemStack, int customModelData) {
        itemStack.set(
            pers.neige.neigeitems.ref.core.component.RefDataComponents.CUSTOM_MODEL_DATA,
            new pers.neige.neigeitems.ref.core.component.RefCustomModelData(customModelData)
        );
    }
}

final class NewItemBuilderCustomModelDataModern {
    private NewItemBuilderCustomModelDataModern() {
    }

    static void apply(RefNmsItemStack itemStack, int customModelData) {
        itemStack.set(
            pers.neige.neigeitems.ref.core.component.RefDataComponents.CUSTOM_MODEL_DATA,
            new pers.neige.neigeitems.ref.core.component.RefCustomModelData(
                java.util.Collections.singletonList((float) customModelData),
                java.util.Collections.<Boolean>emptyList(),
                java.util.Collections.<String>emptyList(),
                java.util.Collections.<Integer>emptyList()
            )
        );
    }
}

final class NewItemBuilderHideFlagsLegacy {
    private NewItemBuilderHideFlagsLegacy() {
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object getComponent(
        RefNmsItemStack itemStack,
        pers.neige.neigeitems.ref.core.component.RefDataComponentType<?> type
    ) {
        return itemStack.components.get((pers.neige.neigeitems.ref.core.component.RefDataComponentType) type);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setComponent(
        RefNmsItemStack itemStack,
        pers.neige.neigeitems.ref.core.component.RefDataComponentType<?> type,
        Object value
    ) {
        itemStack.set((pers.neige.neigeitems.ref.core.component.RefDataComponentType) type, value);
    }

    static void apply(RefNmsItemStack itemStack, int hideFlag) {
        if (hideFlag == 0 || itemStack.components == null) return;

        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_ATTRIBUTES")) {
            pers.neige.neigeitems.ref.world.item.component.RefItemAttributeModifiers value =
                (pers.neige.neigeitems.ref.world.item.component.RefItemAttributeModifiers) getComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.ATTRIBUTE_MODIFIERS
                );
            if (value != null) {
                setComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.ATTRIBUTE_MODIFIERS,
                    value.withTooltip(false)
                );
            }
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_ENCHANTS")) {
            pers.neige.neigeitems.ref.world.item.enchantment.RefItemEnchantments value =
                (pers.neige.neigeitems.ref.world.item.enchantment.RefItemEnchantments) getComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.ENCHANTMENTS
                );
            if (value != null) {
                setComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.ENCHANTMENTS,
                    value.withTooltip(false)
                );
            }
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_STORED_ENCHANTS")) {
            pers.neige.neigeitems.ref.world.item.enchantment.RefItemEnchantments value =
                (pers.neige.neigeitems.ref.world.item.enchantment.RefItemEnchantments) getComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.STORED_ENCHANTMENTS
                );
            if (value != null) {
                setComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.STORED_ENCHANTMENTS,
                    value.withTooltip(false)
                );
            }
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_UNBREAKABLE")) {
            pers.neige.neigeitems.ref.world.item.component.RefUnbreakable value =
                (pers.neige.neigeitems.ref.world.item.component.RefUnbreakable) getComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.UNBREAKABLE
                );
            if (value != null) {
                setComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.UNBREAKABLE,
                    value.withTooltip(false)
                );
            }
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_DYE")) {
            pers.neige.neigeitems.ref.world.item.component.RefDyedItemColor value =
                (pers.neige.neigeitems.ref.world.item.component.RefDyedItemColor) getComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.DYED_COLOR
                );
            if (value != null) {
                setComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.DYED_COLOR,
                    value.withTooltip(false)
                );
            }
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_ARMOR_TRIM")) {
            pers.neige.neigeitems.ref.world.item.equipment.trim.RefArmorTrim value =
                (pers.neige.neigeitems.ref.world.item.equipment.trim.RefArmorTrim) getComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.TRIM
                );
            if (value != null) {
                setComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.TRIM,
                    value.withTooltip(false)
                );
            }
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_PLACED_ON")) {
            pers.neige.neigeitems.ref.world.item.RefAdventureModePredicate value =
                (pers.neige.neigeitems.ref.world.item.RefAdventureModePredicate) getComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.CAN_PLACE_ON
                );
            if (value != null) {
                setComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.CAN_PLACE_ON,
                    value.withTooltip(false)
                );
            }
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_DESTROYS")) {
            pers.neige.neigeitems.ref.world.item.RefAdventureModePredicate value =
                (pers.neige.neigeitems.ref.world.item.RefAdventureModePredicate) getComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.CAN_BREAK
                );
            if (value != null) {
                setComponent(
                    itemStack,
                    pers.neige.neigeitems.ref.core.component.RefDataComponents.CAN_BREAK,
                    value.withTooltip(false)
                );
            }
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_ADDITIONAL_TOOLTIP")) {
            setComponent(
                itemStack,
                pers.neige.neigeitems.ref.core.component.RefDataComponents.HIDE_ADDITIONAL_TOOLTIP,
                pers.neige.neigeitems.ref.util.RefUnit.INSTANCE
            );
        }
    }
}

final class NewItemBuilderHideFlagsModern {
    private static final pers.neige.neigeitems.ref.core.component.RefDataComponentType<?>[] ADDITIONAL_TOOLTIP_TYPES = {
        pers.neige.neigeitems.ref.core.component.RefDataComponents.BANNER_PATTERNS,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.BEES,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.BLOCK_ENTITY_DATA,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.BLOCK_STATE,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.BUNDLE_CONTENTS,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.CHARGED_PROJECTILES,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.CONTAINER,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.CONTAINER_LOOT,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.FIREWORK_EXPLOSION,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.FIREWORKS,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.INSTRUMENT,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.JUKEBOX_PLAYABLE,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.MAP_ID,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.PAINTING_VARIANT,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.POT_DECORATIONS,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.POTION_CONTENTS,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.TROPICAL_FISH_PATTERN,
        pers.neige.neigeitems.ref.core.component.RefDataComponents.WRITTEN_BOOK_CONTENT
    };

    private NewItemBuilderHideFlagsModern() {
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setComponent(
        pers.neige.neigeitems.ref.nbt.RefNmsItemStack itemStack,
        pers.neige.neigeitems.ref.core.component.RefDataComponentType<?> type,
        Object value
    ) {
        itemStack.set((pers.neige.neigeitems.ref.core.component.RefDataComponentType) type, value);
    }

    static void apply(pers.neige.neigeitems.ref.nbt.RefNmsItemStack itemStack, int hideFlag) {
        if (hideFlag == 0 || itemStack.components == null) return;

        pers.neige.neigeitems.ref.world.item.component.RefTooltipDisplay tooltipDisplay =
            (pers.neige.neigeitems.ref.world.item.component.RefTooltipDisplay) itemStack.components.get(
                pers.neige.neigeitems.ref.core.component.RefDataComponents.TOOLTIP_DISPLAY
            );
        if (tooltipDisplay == null) {
            tooltipDisplay = pers.neige.neigeitems.ref.world.item.component.RefTooltipDisplay.DEFAULT;
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_ATTRIBUTES")) {
            tooltipDisplay = tooltipDisplay.withHidden(pers.neige.neigeitems.ref.core.component.RefDataComponents.ATTRIBUTE_MODIFIERS, true);
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_ENCHANTS")) {
            tooltipDisplay = tooltipDisplay.withHidden(pers.neige.neigeitems.ref.core.component.RefDataComponents.ENCHANTMENTS, true);
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_STORED_ENCHANTS")) {
            tooltipDisplay = tooltipDisplay.withHidden(pers.neige.neigeitems.ref.core.component.RefDataComponents.STORED_ENCHANTMENTS, true);
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_UNBREAKABLE")) {
            tooltipDisplay = tooltipDisplay.withHidden(pers.neige.neigeitems.ref.core.component.RefDataComponents.UNBREAKABLE, true);
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_DYE")) {
            tooltipDisplay = tooltipDisplay.withHidden(pers.neige.neigeitems.ref.core.component.RefDataComponents.DYED_COLOR, true);
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_ARMOR_TRIM")) {
            tooltipDisplay = tooltipDisplay.withHidden(pers.neige.neigeitems.ref.core.component.RefDataComponents.TRIM, true);
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_PLACED_ON")) {
            tooltipDisplay = tooltipDisplay.withHidden(pers.neige.neigeitems.ref.core.component.RefDataComponents.CAN_PLACE_ON, true);
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_DESTROYS")) {
            tooltipDisplay = tooltipDisplay.withHidden(pers.neige.neigeitems.ref.core.component.RefDataComponents.CAN_BREAK, true);
        }
        if (NewItemBuilderCompatibility.hasHideFlag(hideFlag, "HIDE_ADDITIONAL_TOOLTIP")) {
            for (pers.neige.neigeitems.ref.core.component.RefDataComponentType<?> type : ADDITIONAL_TOOLTIP_TYPES) {
                tooltipDisplay = tooltipDisplay.withHidden(type, true);
            }
        }
        setComponent(itemStack, pers.neige.neigeitems.ref.core.component.RefDataComponents.TOOLTIP_DISPLAY, tooltipDisplay);
    }
}
