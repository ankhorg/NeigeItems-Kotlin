package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.ai.navigation;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEntity;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/v1_12_R1/NavigationAbstract")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/ai/navigation/PathNavigation")
public abstract class RefPathNavigation {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/NavigationAbstract;a(DDDD)Z")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;moveTo(DDDD)Z")
    public native boolean moveTo(double x, double y, double z, double speed);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/NavigationAbstract;a(Lnet/minecraft/server/v1_12_R1/Entity;D)Z")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;moveTo(Lnet/minecraft/world/entity/Entity;D)Z")
    public native boolean moveTo(RefEntity entity, double speed);
}
