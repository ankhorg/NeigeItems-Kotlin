package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EnumMoveType")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/MoverType")
public final class RefMoverType {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumMoveType;SELF:Lnet/minecraft/server/v1_12_R1/EnumMoveType;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/MoverType;SELF:Lnet/minecraft/world/entity/MoverType;")
    public static final RefMoverType SELF = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumMoveType;PLAYER:Lnet/minecraft/server/v1_12_R1/EnumMoveType;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/MoverType;PLAYER:Lnet/minecraft/world/entity/MoverType;")
    public static final RefMoverType PLAYER = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumMoveType;PISTON:Lnet/minecraft/server/v1_12_R1/EnumMoveType;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/MoverType;PISTON:Lnet/minecraft/world/entity/MoverType;")
    public static final RefMoverType PISTON = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumMoveType;SHULKER_BOX:Lnet/minecraft/server/v1_12_R1/EnumMoveType;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/MoverType;SHULKER_BOX:Lnet/minecraft/world/entity/MoverType;")
    public static final RefMoverType SHULKER_BOX = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EnumMoveType;SHULKER:Lnet/minecraft/server/v1_12_R1/EnumMoveType;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/MoverType;SHULKER:Lnet/minecraft/world/entity/MoverType;")
    public static final RefMoverType SHULKER = null;
}
