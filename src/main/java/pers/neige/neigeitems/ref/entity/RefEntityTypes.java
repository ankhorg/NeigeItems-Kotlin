package pers.neige.neigeitems.ref.entity;

import pers.neige.neigeitems.ref.RefMinecraftKey;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityTypes", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public final class RefEntityTypes {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityTypes;a(Lnet/minecraft/server/v1_12_R1/MinecraftKey;)Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static native String getTranslationKey(RefMinecraftKey key);

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityTypes;a(ILjava/lang/String;Ljava/lang/Class;Ljava/lang/String;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static native void registerEntity(int id, String name, Class<? extends RefEntity> entityClass, String entityName);
}
