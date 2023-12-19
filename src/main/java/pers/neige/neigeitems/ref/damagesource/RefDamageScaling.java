package pers.neige.neigeitems.ref.damagesource;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/damagesource/DamageScaling", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefDamageScaling {
    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageScaling;NEVER:Lnet/minecraft/world/damagesource/DamageScaling;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDamageScaling NEVER = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageScaling;WHEN_CAUSED_BY_LIVING_NON_PLAYER:Lnet/minecraft/world/damagesource/DamageScaling;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDamageScaling WHEN_CAUSED_BY_LIVING_NON_PLAYER = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageScaling;ALWAYS:Lnet/minecraft/world/damagesource/DamageScaling;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDamageScaling ALWAYS = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageScaling;getSerializedName()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native String getSerializedName();
}
