package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.function.Predicate;

@HandleBy(reference = "net/minecraft/core/component/DataComponentPredicate", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
public final class RefDataComponentPredicate implements Predicate<RefDataComponentMap> {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate;EMPTY:Lnet/minecraft/core/component/DataComponentPredicate;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public static final RefDataComponentPredicate EMPTY = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate;builder()Lnet/minecraft/core/component/DataComponentPredicate$Builder;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public static native RefDataComponentPredicate$Builder builder();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate;allOf(Lnet/minecraft/core/component/DataComponentMap;)Lnet/minecraft/core/component/DataComponentPredicate;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public static native RefDataComponentPredicate allOf(RefDataComponentMap components);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate;someOf(Lnet/minecraft/core/component/DataComponentMap;[Lnet/minecraft/core/component/DataComponentType;)Lnet/minecraft/core/component/DataComponentPredicate;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public static native RefDataComponentPredicate someOf(RefDataComponentMap components, RefDataComponentType<?>... types);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate;test(Lnet/minecraft/core/component/DataComponentMap;)Z", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    @Override
    public native boolean test(RefDataComponentMap componentMap);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate;test(Lnet/minecraft/core/component/DataComponentHolder;)Z", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native boolean test(RefDataComponentHolder holder);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate;alwaysMatches()Z", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native boolean alwaysMatches();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate;asPatch()Lnet/minecraft/core/component/DataComponentPatch;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native RefDataComponentPatch asPatch();
}
