package pers.neige.neigeitems.ref.core.component;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/core/component/DataComponentPredicate$Builder", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
public final class RefDataComponentPredicate$Builder {
    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate$Builder;expect(Lnet/minecraft/core/component/TypedDataComponent;)Lnet/minecraft/core/component/DataComponentPredicate$Builder;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native <T> RefDataComponentPredicate$Builder expect(RefTypedDataComponent<T> component);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate$Builder;expect(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/DataComponentPredicate$Builder;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native <T> RefDataComponentPredicate$Builder expect(RefDataComponentType<? super T> type, T value);

    @HandleBy(reference = "Lnet/minecraft/core/component/DataComponentPredicate$Builder;build()Lnet/minecraft/core/component/DataComponentPredicate;", predicates = "craftbukkit_version:[v1_20_R4,v1_21_R4)")
    public native RefDataComponentPredicate build();
}
