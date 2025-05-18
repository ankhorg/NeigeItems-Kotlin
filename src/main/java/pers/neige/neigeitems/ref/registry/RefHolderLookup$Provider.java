package pers.neige.neigeitems.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.resources.RefRegistryOps;
import pers.neige.neigeitems.ref.serialization.RefDynamicOps;

@HandleBy(reference = "net/minecraft/core/HolderLookup$Provider", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
public interface RefHolderLookup$Provider {
    @HandleBy(reference = "Lnet/minecraft/core/HolderLookup$Provider;createSerializationContext(Lcom/mojang/serialization/DynamicOps;)Lnet/minecraft/resources/RegistryOps;", isInterface = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    default <V> RefRegistryOps<V> createSerializationContext(RefDynamicOps<V> delegate) {
        return null;
    }
}
