package pers.neige.neigeitems.ref.damagesource;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.sounds.RefSoundEvent;

@HandleBy(reference = "net/minecraft/world/damagesource/DamageEffects", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefDamageEffects {
    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageEffects;HURT:Lnet/minecraft/world/damagesource/DamageEffects;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDamageEffects HURT = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageEffects;THORNS:Lnet/minecraft/world/damagesource/DamageEffects;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDamageEffects THORNS = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageEffects;DROWNING:Lnet/minecraft/world/damagesource/DamageEffects;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDamageEffects DROWNING = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageEffects;BURNING:Lnet/minecraft/world/damagesource/DamageEffects;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDamageEffects BURNING = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageEffects;POKING:Lnet/minecraft/world/damagesource/DamageEffects;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDamageEffects POKING = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageEffects;FREEZING:Lnet/minecraft/world/damagesource/DamageEffects;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefDamageEffects FREEZING = null;

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageEffects;getSerializedName()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native String getSerializedName();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageEffects;sound()Lnet/minecraft/sounds/SoundEvent;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefSoundEvent sound();
}
