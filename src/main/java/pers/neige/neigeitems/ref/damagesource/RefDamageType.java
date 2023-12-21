package pers.neige.neigeitems.ref.damagesource;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/damagesource/DamageType", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefDamageType {
    private RefDamageType() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageType;msgId()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native String msgId();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageType;scaling()Lnet/minecraft/world/damagesource/DamageScaling;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefDamageScaling scaling();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageType;exhaustion()F", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native float exhaustion();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageType;effects()Lnet/minecraft/world/damagesource/DamageEffects;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefDamageEffects effects();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageType;deathMessageType()Lnet/minecraft/world/damagesource/DeathMessageType;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefDeathMessageType deathMessageType();
}
