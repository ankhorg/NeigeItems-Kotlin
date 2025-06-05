package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@HandleBy(reference = "net/minecraft/core/component/DataComponentPatch", predicates = "craftbukkit_version:[v1_20_R4,)")
public class RefDataComponentPatch {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/util/Optional;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> @Nullable Optional<? extends T> get(RefDataComponentType<? extends T> type);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch;entrySet()Ljava/util/Set;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native Set<Map.Entry<RefDataComponentType<?>, Optional<?>>> entrySet();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch;size()I", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native int size();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch;forget(Ljava/util/function/Predicate;)Lnet/minecraft/core/component/DataComponentPatch;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefDataComponentPatch forget(Predicate<RefDataComponentType<?>> removedTypePredicate);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPatch;isEmpty()Z", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native boolean isEmpty();
}
