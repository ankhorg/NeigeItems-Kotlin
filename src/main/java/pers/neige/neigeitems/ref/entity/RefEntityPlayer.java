package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.argument.RefAnchor;
import pers.neige.neigeitems.ref.block.sign.RefSignBlockEntity;
import pers.neige.neigeitems.ref.network.RefPlayerConnection;
import pers.neige.neigeitems.ref.world.RefVec3;

@HandleBy(reference = "net/minecraft/server/level/ServerPlayer", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityPlayer", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefEntityPlayer extends RefEntityHuman {
    @HandleBy(reference = "Lnet/minecraft/server/level/ServerPlayer;gameMode:Lnet/minecraft/server/level/ServerPlayerGameMode;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityPlayer;playerInteractManager:Lnet/minecraft/server/v1_12_R1/PlayerInteractManager;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final RefPlayerInteractManager playerInteractManager = null;

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerPlayer;connection:Lnet/minecraft/server/network/ServerGamePacketListenerImpl;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityPlayer;playerConnection:Lnet/minecraft/server/v1_12_R1/PlayerConnection;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefPlayerConnection playerConnection = null;

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerPlayer;lookAt(Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native void lookAt(RefAnchor anchorPoint, RefVec3 target);

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerPlayer;openTextEdit(Lnet/minecraft/world/level/block/entity/SignBlockEntity;)V", predicates = "craftbukkit_version:[v1_17_R1,v1_20_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityPlayer;openSign(Lnet/minecraft/server/v1_12_R1/TileEntitySign;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void openSign(RefSignBlockEntity sign);

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerPlayer;openTextEdit(Lnet/minecraft/world/level/block/entity/SignBlockEntity;Z)V", predicates = "craftbukkit_version:[v1_20_R1,)")
    public native void openSign(RefSignBlockEntity sign, boolean front);
}
