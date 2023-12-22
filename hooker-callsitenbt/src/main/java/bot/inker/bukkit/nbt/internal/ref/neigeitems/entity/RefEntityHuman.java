package pers.neige.neigeitems.internal.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/entity/player/Player", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityHuman", predicates = "craftbukkit_version:[v1_12_R1,)")
public abstract class RefEntityHuman extends RefEntityLiving {
    @HandleBy(reference = "Lnet/minecraft/world/entity/player/Player;getAbsorptionAmount()F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityHuman;getAbsorptionHearts()F", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native float getAbsorptionAmount();

    @HandleBy(reference = "Lnet/minecraft/world/entity/player/Player;setAbsorptionAmount(F)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityHuman;setAbsorptionHearts(F)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setAbsorptionAmount(float amount);

    @HandleBy(reference = "Lnet/minecraft/world/entity/player/Player;attack(Lnet/minecraft/world/entity/Entity;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityHuman;attack(Lnet/minecraft/server/v1_12_R1/Entity;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void attack(RefEntity target);

    @HandleBy(reference = "Lnet/minecraft/world/entity/player/Player;jumpFromGround()V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityHuman;jump()V", predicates = "craftbukkit_version:[v1_14_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityHuman;cH()V", predicates = "craftbukkit_version:[v1_13_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityHuman;cu()V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void jumpFromGround();
}
