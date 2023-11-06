package bot.inker.bukkit.nbt.internal.ref.neigeitems.world;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/AxisAlignedBB")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/phys/AABB")
public class RefAABB {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;<init>(DDDDDD)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/phys/AABB;<init>(DDDDDD)V")
    public RefAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        throw new UnsupportedOperationException();
    }
}
