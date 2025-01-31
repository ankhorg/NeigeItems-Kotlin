package pers.neige.neigeitems.ref;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "Lnet/minecraft/resources/ResourceLocation", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/MinecraftKey", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefMinecraftKey {
    @HandleBy(reference = "Lnet/minecraft/resources/ResourceLocation;parse(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;", predicates = "craftbukkit_version:[v1_21_R1,)")
    public static native RefMinecraftKey parse(String id);
}
