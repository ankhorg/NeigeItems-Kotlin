package bot.inker.bukkit.nbt.internal.ref.neigeitems.world;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/Vec3D")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/phys/Vec3")
public class RefVec3 {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Vec3D;x:D")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/phys/Vec3;x:D")
    public final double x;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Vec3D;y:D")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/phys/Vec3;y:D")
    public final double y;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Vec3D;z:D")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/phys/Vec3;z:D")
    public final double z;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Vec3D;<init>(DDD)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V")
    public RefVec3(double x, double y, double z) {
        throw new UnsupportedOperationException();
    }
}
