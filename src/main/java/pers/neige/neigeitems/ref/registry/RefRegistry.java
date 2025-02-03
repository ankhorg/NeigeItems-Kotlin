package pers.neige.neigeitems.ref.registry;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.RefMinecraftKey;

import javax.annotation.Nullable;

@HandleBy(reference = "net/minecraft/core/Registry", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
public interface RefRegistry<T> {
    @HandleBy(reference = "Lnet/minecraft/core/Registry;getValue(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_21_R2,)")
    @HandleBy(reference = "Lnet/minecraft/core/Registry;get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,v1_21_R2)")
    T getValue(@Nullable RefMinecraftKey id);
}
