package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/core/component/DataComponentExactPredicate$Builder", predicates = "craftbukkit_version:[v1_21_R4,)")
public final class RefDataComponentExactPredicate$Builder {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate$Builder;expect(Lnet/minecraft/core/component/TypedDataComponent;)Lnet/minecraft/core/component/DataComponentExactPredicate$Builder;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native <T> RefDataComponentExactPredicate$Builder expect(RefTypedDataComponent<T> component);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate$Builder;expect(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/DataComponentExactPredicate$Builder;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native <T> RefDataComponentExactPredicate$Builder expect(RefDataComponentType<? super T> component, T value);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentExactPredicate$Builder;build()Lnet/minecraft/core/component/DataComponentExactPredicate;", predicates = "craftbukkit_version:[v1_21_R4,)")
    public native RefDataComponentExactPredicate build();
}
