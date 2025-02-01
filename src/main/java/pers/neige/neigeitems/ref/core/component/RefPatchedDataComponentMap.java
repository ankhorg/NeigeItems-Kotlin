package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

import javax.annotation.Nullable;

@HandleBy(reference = "net/minecraft/core/component/PatchedDataComponentMap", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public class RefPatchedDataComponentMap implements RefDataComponentMap {
    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_20_R4,)")
    @Nullable
    @Override
    public native <T> T get(RefDataComponentType<? extends T> type);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_20_R4,)")
    @Nullable
    public native <T> T set(RefDataComponentType<? super T> type, @Nullable T value);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;remove(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_20_R4,)")
    @Nullable
    public native <T> T remove(RefDataComponentType<? extends T> type);
}
