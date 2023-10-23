package bot.inker.bukkit.nbt.internal.ref.neigeitems.util;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/util/random/Weight")
public class RefWeight {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/Weight;of(I)Lnet/minecraft/util/random/Weight;")
    public static native RefWeight of(int weight);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/Weight;asInt()I")
    public native int asInt();
}
