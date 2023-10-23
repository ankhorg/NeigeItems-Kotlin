package bot.inker.bukkit.nbt.internal.ref.neigeitems.util;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/util/random/SimpleWeightedRandomList$Builder")
public final class RefBuilder<E> {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;<init>()V")
    public RefBuilder() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;add(Ljava/lang/Object;I)Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;")
    public native RefBuilder<E> add(E object, int weight);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/SimpleWeightedRandomList$Builder;build()Lnet/minecraft/util/random/SimpleWeightedRandomList;")
    public native RefSimpleWeightedRandomList<E> build();
}
