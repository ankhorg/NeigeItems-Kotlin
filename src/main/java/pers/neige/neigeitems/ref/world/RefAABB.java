package pers.neige.neigeitems.ref.world;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/phys/AABB", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/AxisAlignedBB", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefAABB {
    @HandleBy(reference = "Lnet/minecraft/world/phys/AABB;<init>(DDDDDD)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;<init>(DDDDDD)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/phys/AABB;intersects(Lnet/minecraft/world/phys/AABB;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;c(Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;)Z", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native boolean intersects(RefAABB box);

    @HandleBy(reference = "Lnet/minecraft/world/phys/AABB;intersects(DDDDDD)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/AxisAlignedBB;a(DDDDDD)Z", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ);
}
