package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.function.Predicate;

@HandleBy(reference = "net/minecraft/core/component/DataComponentExactPredicate", predicates = "craftbukkit_version:[v1_21_R4,)")
public final class RefDataComponentExactPredicate implements Predicate<RefDataComponentGetter> {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate;EMPTY:Lnet/minecraft/core/component/DataComponentExactPredicate;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static final RefDataComponentExactPredicate EMPTY = null;

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate;builder()Lnet/minecraft/core/component/DataComponentExactPredicate$Builder;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static native RefDataComponentExactPredicate$Builder builder();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate;expect(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/DataComponentExactPredicate;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static native <T> RefDataComponentExactPredicate expect(RefDataComponentType<T> component, T value);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate;allOf(Lnet/minecraft/core/component/DataComponentMap;)Lnet/minecraft/core/component/DataComponentExactPredicate;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static native RefDataComponentExactPredicate allOf(RefDataComponentMap map);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate;someOf(Lnet/minecraft/core/component/DataComponentMap;[Lnet/minecraft/core/component/DataComponentType;)Lnet/minecraft/core/component/DataComponentExactPredicate;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public static native RefDataComponentExactPredicate someOf(RefDataComponentMap map, RefDataComponentType<?>... types);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate;isEmpty()Z", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native boolean isEmpty();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate;test(Lnet/minecraft/core/component/DataComponentGetter;)Z", predicates = "craftbukkit_version:[v1_21_R4,)")
    @Override
    public native boolean test(RefDataComponentGetter componentGetter);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate;alwaysMatches()Z", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native boolean alwaysMatches();

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate;asPatch()Lnet/minecraft/core/component/DataComponentPatch;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native RefDataComponentPatch asPatch();
}
