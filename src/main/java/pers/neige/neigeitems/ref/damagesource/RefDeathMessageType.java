package pers.neige.neigeitems.ref.damagesource;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/damagesource/DamageScaling", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefDeathMessageType {
    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DeathMessageType;DEFAULT:Lnet/minecraft/world/damagesource/DeathMessageType;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDeathMessageType DEFAULT = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DeathMessageType;FALL_VARIANTS:Lnet/minecraft/world/damagesource/DeathMessageType;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDeathMessageType FALL_VARIANTS = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DeathMessageType;INTENTIONAL_GAME_DESIGN:Lnet/minecraft/world/damagesource/DeathMessageType;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDeathMessageType INTENTIONAL_GAME_DESIGN = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DeathMessageType;getSerializedName()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native String getSerializedName();
}
