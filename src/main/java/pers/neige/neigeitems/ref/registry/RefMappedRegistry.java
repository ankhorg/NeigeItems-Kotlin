package pers.neige.neigeitems.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.ref.RefMinecraftKey;

import java.util.Map;

@HandleBy(reference = "net/minecraft/core/MappedRegistry", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefMappedRegistry<T> implements RefWritableRegistry<T> {
    @HandleBy(reference = "Lnet/minecraft/core/MappedRegistry;byValue:Ljava/util/Map;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public final Map<T, RefReference<T>> byValue = null;
    @HandleBy(reference = "Lnet/minecraft/core/MappedRegistry;frozen:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public boolean frozen;

    @HandleBy(reference = "Lnet/minecraft/core/MappedRegistry;unregisteredIntrusiveHolders:Ljava/util/Map;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public Map<T, RefReference<T>> unregisteredIntrusiveHolders;

    @HandleBy(reference = "Lnet/minecraft/core/MappedRegistry;asLookup()Lnet/minecraft/core/HolderLookup$RegistryLookup;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefRegistryLookup<T> asLookup();

    @Override
    public native RefMinecraftKey getKey(T value);

    @Override
    public native T getValue(@Nullable RefMinecraftKey id);
}
