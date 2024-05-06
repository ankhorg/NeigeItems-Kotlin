package pers.neige.neigeitems.ref.entity.ai.control;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/ControllerJump", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public class RefJumpControl {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ControllerJump;a()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native void tick();
}
