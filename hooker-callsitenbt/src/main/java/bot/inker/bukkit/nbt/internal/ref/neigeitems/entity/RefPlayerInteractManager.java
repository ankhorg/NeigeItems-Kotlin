package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefWorld;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PlayerInteractManager")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/level/ServerPlayerGameMode")
public final class RefPlayerInteractManager {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerInteractManager;a(Lnet/minecraft/server/v1_12_R1/EntityHuman;Lnet/minecraft/server/v1_12_R1/World;Lnet/minecraft/server/v1_12_R1/ItemStack;Lnet/minecraft/server/v1_12_R1/EnumHand;)Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;")
    @HandleBy(version = CbVersion.v1_16_R1, reference = "Lnet/minecraft/server/v1_16_R1/PlayerInteractManager;a(Lnet/minecraft/server/v1_16_R1/EntityPlayer;Lnet/minecraft/server/v1_16_R1/World;Lnet/minecraft/server/v1_16_R1/ItemStack;Lnet/minecraft/server/v1_16_R1/EnumHand;)Lnet/minecraft/server/v1_16_R1/EnumInteractionResult;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/level/ServerPlayerGameMode;useItem(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;")
    public native RefEnumInteractionResult useItem(RefEntityHuman entityHuman, RefWorld world, RefNmsItemStack itemStack, RefEnumHand hand);
}
