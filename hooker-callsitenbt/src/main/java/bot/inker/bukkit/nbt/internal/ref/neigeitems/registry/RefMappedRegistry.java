package bot.inker.bukkit.nbt.internal.ref.neigeitems.registry;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

import java.util.Map;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/core/MappedRegistry")
public final class RefMappedRegistry<T> implements RefWritableRegistry<T> {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/core/MappedRegistry;byValue:Ljava/util/Map;", accessor = true)
    public final Map<T, RefReference<T>> byValue = null;
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/core/MappedRegistry;frozen:Z", accessor = true)
    public boolean frozen;
}
