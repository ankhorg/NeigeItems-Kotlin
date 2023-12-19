package pers.neige.neigeitems.ref.world;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/phys/Vec3", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/Vec3D", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefVec3 {
    @HandleBy(reference = "Lnet/minecraft/world/phys/Vec3;x:D", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Vec3D;x:D", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final double x;
    @HandleBy(reference = "Lnet/minecraft/world/phys/Vec3;y:D", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Vec3D;y:D", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final double y;
    @HandleBy(reference = "Lnet/minecraft/world/phys/Vec3;z:D", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Vec3D;z:D", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final double z;

    @HandleBy(reference = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Vec3D;<init>(DDD)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefVec3(double x, double y, double z) {
        throw new UnsupportedOperationException();
    }
}
