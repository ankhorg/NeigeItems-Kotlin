package pers.neige.neigeitems.ref.entity.ai.control;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/ControllerMove", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public class RefMoveControl {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ControllerMove;a()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native void tick();
}
