package bot.inker.bukkit.nbt.internal.ref.neigeitems.util;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

import java.util.List;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/util/random/WeightedRandomList")
public class RefWeightedRandomList<E extends RefWeightedEntry> {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/WeightedRandomList;create(Ljava/util/List;)Lnet/minecraft/util/random/WeightedRandomList;")
    public static native <E extends RefWeightedEntry> RefWeightedRandomList<E> create(List<E> entries);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/WeightedRandomList;unwrap()Ljava/util/List;")
    public native List<E> unwrap();
}
