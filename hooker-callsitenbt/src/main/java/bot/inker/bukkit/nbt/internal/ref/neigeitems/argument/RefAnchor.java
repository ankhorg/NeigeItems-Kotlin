package bot.inker.bukkit.nbt.internal.ref.neigeitems.argument;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_13_R1, reference = "net/minecraft/server/v1_13_R1/ArgumentAnchor$Anchor")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/commands/arguments/EntityAnchorArgument$Anchor")
public final class RefAnchor {
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/ArgumentAnchor$Anchor;FEET:Lnet/minecraft/server/v1_13_R1/ArgumentAnchor$Anchor;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;FEET:Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;")
    public static final RefAnchor FEET = null;

    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/ArgumentAnchor$Anchor;EYES:Lnet/minecraft/server/v1_13_R1/ArgumentAnchor$Anchor;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;EYES:Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;")
    public static final RefAnchor EYES = null;
}
