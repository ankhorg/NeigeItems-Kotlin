package pers.neige.neigeitems.internal.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.Map;

@HandleBy(reference = "net/minecraft/core/MappedRegistry", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefMappedRegistry<T> implements RefWritableRegistry<T> {
    @HandleBy(reference = "Lnet/minecraft/core/MappedRegistry;byValue:Ljava/util/Map;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public final Map<T, RefReference<T>> byValue = null;
    @HandleBy(reference = "Lnet/minecraft/core/MappedRegistry;frozen:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public boolean frozen;
}
