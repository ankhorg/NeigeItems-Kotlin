package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.world.RefCraftServer;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftPlayer", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefCraftPlayer extends RefCraftHumanEntity {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftPlayer;<init>(Lorg/bukkit/craftbukkit/v1_17_R1/CraftServer;Lnet/minecraft/server/level/ServerPlayer;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftPlayer;<init>(Lorg/bukkit/craftbukkit/v1_12_R1/CraftServer;Lnet/minecraft/server/v1_12_R1/EntityPlayer;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefCraftPlayer(RefCraftServer server, RefEntityPlayer entity) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftPlayer;getHandle()Lnet/minecraft/server/level/ServerPlayer;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftPlayer;getHandle()Lnet/minecraft/server/v1_12_R1/EntityPlayer;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefEntityPlayer getHandle();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftPlayer;isOp()Z", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native boolean isOp();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftPlayer;setOp(Z)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setOp(boolean value);
}
