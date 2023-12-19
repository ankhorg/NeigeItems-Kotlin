package pers.neige.neigeitems.ref.entity;

import pers.neige.neigeitems.ref.entity.ai.navigation.RefPathNavigation;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/entity/Mob", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityInsentient", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public abstract class RefMob extends RefEntityLiving {
    @HandleBy(reference = "Lnet/minecraft/world/entity/Mob;getNavigation()Lnet/minecraft/world/entity/ai/navigation/PathNavigation;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityInsentient;getNavigation()Lnet/minecraft/server/v1_12_R1/NavigationAbstract;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefPathNavigation getNavigation();
}
