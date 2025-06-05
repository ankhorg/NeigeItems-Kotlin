package pers.neige.neigeitems.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;

@HandleBy(reference = "net/minecraft/core/Holder$c", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefReference<T> implements RefHolder<T> {
    @HandleBy(reference = "Lnet/minecraft/core/Holder$Reference;createIntrusive(Lnet/minecraft/core/HolderOwner;Ljava/lang/Object;)Lnet/minecraft/core/Holder$Reference;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native static <T> RefReference<T> createIntrusive(RefHolderOwner<T> owner, @Nullable T value);

    @HandleBy(reference = "Lnet/minecraft/core/Holder$c;b(Ljava/lang/Object;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void bindValue(T value);
}
