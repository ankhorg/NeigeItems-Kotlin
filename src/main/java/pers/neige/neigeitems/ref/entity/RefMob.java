package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.ai.control.RefJumpControl;
import pers.neige.neigeitems.ref.entity.ai.control.RefLookControl;
import pers.neige.neigeitems.ref.entity.ai.control.RefMoveControl;
import pers.neige.neigeitems.ref.entity.ai.navigation.RefPathNavigation;

@HandleBy(reference = "net/minecraft/world/entity/Mob", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityInsentient", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public abstract class RefMob extends RefEntityLiving {
    @HandleBy(reference = "Lnet/minecraft/world/entity/Mob;getNavigation()Lnet/minecraft/world/entity/ai/navigation/PathNavigation;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityInsentient;getNavigation()Lnet/minecraft/server/v1_12_R1/NavigationAbstract;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefPathNavigation getNavigation();

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityInsentient;getControllerLook()Lnet/minecraft/server/v1_12_R1/ControllerLook;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native RefLookControl getLookControl();

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityInsentient;getControllerMove()Lnet/minecraft/server/v1_12_R1/ControllerMove;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native RefMoveControl getMoveControl();

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityInsentient;getControllerJump()Lnet/minecraft/server/v1_12_R1/ControllerJump;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native RefJumpControl getJumpControl();
}
