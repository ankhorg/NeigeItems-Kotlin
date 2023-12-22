package pers.neige.neigeitems.internal.ref.entity.ai.navigation;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.internal.ref.entity.RefEntity;

@HandleBy(reference = "net/minecraft/world/entity/ai/navigation/PathNavigation", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NavigationAbstract", predicates = "craftbukkit_version:[v1_17_R1,)")
public abstract class RefPathNavigation {
    @HandleBy(reference = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;moveTo(DDDD)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NavigationAbstract;a(DDDD)Z", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native boolean moveTo(double x, double y, double z, double speed);

    @HandleBy(reference = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;moveTo(Lnet/minecraft/world/entity/Entity;D)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NavigationAbstract;a(Lnet/minecraft/server/v1_12_R1/Entity;D)Z", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native boolean moveTo(RefEntity entity, double speed);
}
