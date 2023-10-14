package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EnumInteractionResult")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/InteractionResult")
public final class RefEnumInteractionResult {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;SUCCESS:Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/InteractionResult;SUCCESS:Lnet/minecraft/world/InteractionResult;")
    public static final RefEnumInteractionResult SUCCESS = null;

    @HandleBy(version = CbVersion.v1_15_R1, reference = "Lnet/minecraft/server/v1_15_R1/EnumInteractionResult;CONSUME:Lnet/minecraft/server/v1_15_R1/EnumInteractionResult;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/InteractionResult;CONSUME:Lnet/minecraft/world/InteractionResult;")
    public static final RefEnumInteractionResult CONSUME = null;

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/InteractionResult;CONSUME_PARTIAL:Lnet/minecraft/world/InteractionResult;")
    public static final RefEnumInteractionResult CONSUME_PARTIAL = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;PASS:Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/InteractionResult;PASS:Lnet/minecraft/world/InteractionResult;")
    public static final RefEnumInteractionResult PASS = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;FAIL:Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/InteractionResult;FAIL:Lnet/minecraft/world/InteractionResult;")
    public static final RefEnumInteractionResult FAIL = null;
}
