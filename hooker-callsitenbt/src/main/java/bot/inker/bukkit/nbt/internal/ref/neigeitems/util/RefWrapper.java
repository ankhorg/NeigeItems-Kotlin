package bot.inker.bukkit.nbt.internal.ref.neigeitems.util;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/util/random/WeightedEntry$Wrapper")
public class RefWrapper<T> implements RefWeightedEntry {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/WeightedEntry$Wrapper;<init>(Ljava/lang/Object;Lnet/minecraft/util/random/Weight;)V", accessor = true)
    public static native <T> RefWrapper<T> newInstance(T data, RefWeight weight);

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/WeightedEntry$Wrapper;getData()Ljava/lang/Object;")
    public native T getData();

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/util/random/WeightedEntry$Wrapper;getWeight()Lnet/minecraft/util/random/Weight;")
    public native RefWeight getWeight();
}
