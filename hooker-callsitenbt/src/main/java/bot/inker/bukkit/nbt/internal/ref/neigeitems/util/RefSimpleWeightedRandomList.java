package bot.inker.bukkit.nbt.internal.ref.neigeitems.util;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

import java.util.List;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/util/random/SimpleWeightedRandomList")
public final class RefSimpleWeightedRandomList<E> extends RefWeightedRandomList<RefWrapper<E>> {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList;<init>(Ljava/util/List;)V", accessor = true)
    public static native <E> RefSimpleWeightedRandomList<E> newInstance(List<? extends RefWrapper<E>> entries);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList;builder()Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;")
    public static native <E> RefBuilder<E> builder();
}
