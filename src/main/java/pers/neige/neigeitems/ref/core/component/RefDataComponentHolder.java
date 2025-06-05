package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

@HandleBy(reference = "net/minecraft/core/component/DataComponentHolder", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public class RefDataComponentHolder {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentHolder;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    public native @Nullable <T> T get(RefDataComponentType<? extends T> type);
}
