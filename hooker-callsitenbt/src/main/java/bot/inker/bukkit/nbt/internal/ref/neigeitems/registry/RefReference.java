package pers.neige.neigeitems.internal.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/core/Holder$Reference", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefReference<T> {
    @HandleBy(reference = "Lnet/minecraft/core/Holder$Reference;bindValue(Ljava/lang/Object;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void bindValue(T value);
}
