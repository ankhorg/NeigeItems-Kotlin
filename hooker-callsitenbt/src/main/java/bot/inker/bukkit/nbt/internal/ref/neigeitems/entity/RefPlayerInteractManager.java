package pers.neige.neigeitems.internal.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import pers.neige.neigeitems.internal.ref.block.RefBlockPos;
import pers.neige.neigeitems.internal.ref.world.RefWorld;

@HandleBy(reference = "net/minecraft/server/level/ServerPlayerGameMode", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PlayerInteractManager", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefPlayerInteractManager {
    @HandleBy(reference = "Lnet/minecraft/server/level/ServerPlayerGameMode;useItem(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_16_R1/PlayerInteractManager;a(Lnet/minecraft/server/v1_16_R1/EntityPlayer;Lnet/minecraft/server/v1_16_R1/World;Lnet/minecraft/server/v1_16_R1/ItemStack;Lnet/minecraft/server/v1_16_R1/EnumHand;)Lnet/minecraft/server/v1_16_R1/EnumInteractionResult;", predicates = "craftbukkit_version:[v1_16_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PlayerInteractManager;a(Lnet/minecraft/server/v1_12_R1/EntityHuman;Lnet/minecraft/server/v1_12_R1/World;Lnet/minecraft/server/v1_12_R1/ItemStack;Lnet/minecraft/server/v1_12_R1/EnumHand;)Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native RefEnumInteractionResult useItem(RefEntityHuman entityHuman, RefWorld world, RefNmsItemStack itemStack, RefEnumHand hand);

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerPlayerGameMode;destroyBlock(Lnet/minecraft/core/BlockPos;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PlayerInteractManager;breakBlock(Lnet/minecraft/server/v1_12_R1/BlockPosition;)Z", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native boolean breakBlock(RefBlockPos pos);
}
