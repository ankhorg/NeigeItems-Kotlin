package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.world.item.component.RefCustomData;

@HandleBy(reference = "net/minecraft/core/component/DataComponents", predicates = "craftbukkit_version:[v1_20_R4,)")
public final class RefDataComponents {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CUSTOM_DATA:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<RefCustomData> CUSTOM_DATA = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponents;CUSTOM_NAME:Lnet/minecraft/core/component/DataComponentType;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static final RefDataComponentType<RefComponent> CUSTOM_NAME = null;
}
