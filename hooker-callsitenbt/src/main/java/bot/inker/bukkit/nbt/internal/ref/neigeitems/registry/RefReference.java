package bot.inker.bukkit.nbt.internal.ref.neigeitems.registry;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/core/Holder$Reference")
public final class RefReference<T> {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/core/Holder$Reference;bindValue(Ljava/lang/Object;)V", accessor = true)
    public native void bindValue(T value);
}
