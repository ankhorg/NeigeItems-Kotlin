package pers.neige.neigeitems.ref.world.item.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.core.component.RefDataComponentType;

@HandleBy(reference = "net/minecraft/world/item/component/TooltipDisplay", predicates = "craftbukkit_version:[v1_21_R4,)")
public final class RefTooltipDisplay {
    @HandleBy(reference = "Lnet/minecraft/world/item/component/TooltipDisplay;DEFAULT:Lnet/minecraft/world/item/component/TooltipDisplay;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefTooltipDisplay DEFAULT = null;

    @HandleBy(reference = "Lnet/minecraft/world/item/component/TooltipDisplay;withHidden(Lnet/minecraft/core/component/DataComponentType;Z)Lnet/minecraft/world/item/component/TooltipDisplay;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native RefTooltipDisplay withHidden(RefDataComponentType<?> component, boolean hidden);
}
