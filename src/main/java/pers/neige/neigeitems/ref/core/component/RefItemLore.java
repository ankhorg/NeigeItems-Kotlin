package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.item.RefItem$TooltipContext;
import pers.neige.neigeitems.ref.item.RefTooltipFlag;

import java.util.List;
import java.util.function.Consumer;

@HandleBy(reference = "net/minecraft/world/item/component/ItemLore", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefItemLore {
    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;EMPTY:Lnet/minecraft/world/item/component/ItemLore;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefItemLore EMPTY = null;

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;MAX_LINES:I", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final int MAX_LINES = 0;

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;<init>(Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_20_R4,)")
    public RefItemLore(List<RefComponent> lines) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;<init>(Ljava/util/List;Ljava/util/List;)V", predicates = "craftbukkit_version:[v1_20_R4,)")
    public RefItemLore(List<RefComponent> lines, List<RefComponent> styledLines) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;lines()Ljava/util/List;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native List<RefComponent> lines();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;styledLines()Ljava/util/List;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native List<RefComponent> styledLines();

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;withLineAdded(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/world/item/component/ItemLore;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefItemLore withLineAdded(RefComponent line);

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;addToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native void addToTooltip(RefItem$TooltipContext context, Consumer<RefComponent> tooltipAdder, RefTooltipFlag flag);

    @HandleBy(reference = "Lnet/minecraft/world/item/component/ItemLore;addToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;Lnet/minecraft/core/component/DataComponentGetter;)V", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native void addToTooltip(RefItem$TooltipContext context, Consumer<RefComponent> tooltipAdder, RefTooltipFlag flag, RefDataComponentGetter componentGetter);
}
