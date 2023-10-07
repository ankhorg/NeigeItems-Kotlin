package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EnumHand")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/InteractionHand")
public final class RefEnumHand {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumHand;MAIN_HAND:Lnet/minecraft/server/v1_12_R1/EnumHand;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/InteractionHand;MAIN_HAND:Lnet/minecraft/world/InteractionHand;")
    public static final RefEnumHand MAIN_HAND = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumHand;OFF_HAND:Lnet/minecraft/server/v1_12_R1/EnumHand;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/InteractionHand;OFF_HAND:Lnet/minecraft/world/InteractionHand;")
    public static final RefEnumHand OFF_HAND = null;
}
