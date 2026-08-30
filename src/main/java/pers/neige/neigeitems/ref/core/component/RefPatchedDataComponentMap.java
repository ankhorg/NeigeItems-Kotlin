package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

@HandleBy(reference = "net/minecraft/core/component/PatchedDataComponentMap", predicates = "craftbukkit_version:[v1_20_R4,)")
public class RefPatchedDataComponentMap implements RefDataComponentMap {
    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;<init>(Lnet/minecraft/core/component/DataComponentMap;)V", predicates = "craftbukkit_version:[v1_20_R4,)")
    public RefPatchedDataComponentMap(RefDataComponentMap prototype) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;fromPatch(Lnet/minecraft/core/component/DataComponentMap;Lnet/minecraft/core/component/DataComponentPatch;)Lnet/minecraft/core/component/PatchedDataComponentMap;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public static native RefPatchedDataComponentMap fromPatch(RefDataComponentMap prototype, RefDataComponentPatch patch);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_20_R4,)")
    @Override
    public native <T> @Nullable T get(RefDataComponentType<? extends T> type);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;hasNonDefault(Lnet/minecraft/core/component/DataComponentType;)Z", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native boolean hasNonDefault(RefDataComponentType<?> type);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> @Nullable T set(RefDataComponentType<? super T> type, @Nullable T value);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;set(Lnet/minecraft/core/component/TypedDataComponent;)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_21_R6,)")
    public native <T> @Nullable T set(RefTypedDataComponent<T> component);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;remove(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> @Nullable T remove(RefDataComponentType<? extends T> type);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;applyPatch(Lnet/minecraft/core/component/DataComponentPatch;)V", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native void applyPatch(RefDataComponentPatch patch);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;restorePatch(Lnet/minecraft/core/component/DataComponentPatch;)V", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native void restorePatch(RefDataComponentPatch patch);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;clearPatch()V", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native void clearPatch();

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;setAll(Lnet/minecraft/core/component/DataComponentMap;)V", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native void setAll(RefDataComponentMap map);

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;asPatch()Lnet/minecraft/core/component/DataComponentPatch;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefDataComponentPatch asPatch();

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;copy()Lnet/minecraft/core/component/PatchedDataComponentMap;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefPatchedDataComponentMap copy();

    @HandleBy(reference = "Lnet/minecraft/core/component/PatchedDataComponentMap;toImmutableMap()Lnet/minecraft/core/component/DataComponentMap;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native RefDataComponentMap toImmutableMap();
}
