package pers.neige.neigeitems.ref.damagesource;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.entity.RefEntityLiving;
import pers.neige.neigeitems.ref.registry.RefHolder;
import pers.neige.neigeitems.ref.tags.RefTagKey;
import pers.neige.neigeitems.ref.world.RefVec3;

import javax.annotation.Nullable;

@HandleBy(reference = "net/minecraft/world/damagesource/DamageSource", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefDamageSource {
    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;<init>(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefDamageSource(RefHolder<RefDamageType> type, @Nullable RefEntity source, @Nullable RefEntity attacker, @Nullable RefVec3 position) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;<init>(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefDamageSource(RefHolder<RefDamageType> type, @Nullable RefEntity source, @Nullable RefEntity attacker) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;<init>(Lnet/minecraft/core/Holder;Lnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefDamageSource(RefHolder<RefDamageType> type, RefVec3 position) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;<init>(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/Entity;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefDamageSource(RefHolder<RefDamageType> type, RefEntity attacker) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;<init>(Lnet/minecraft/core/Holder;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefDamageSource(RefHolder<RefDamageType> type) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;isSweep()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isSweep();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;sweep()Lnet/minecraft/world/damagesource/DamageSource;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefDamageSource sweep();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;isMelting()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isMelting();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;melting()Lnet/minecraft/world/damagesource/DamageSource;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefDamageSource melting();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;isPoison()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isPoison();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;poison()Lnet/minecraft/world/damagesource/DamageSource;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefDamageSource poison();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;getFoodExhaustion()F", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native float getFoodExhaustion();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;isIndirect()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isIndirect();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;getDirectEntity()Lnet/minecraft/world/entity/Entity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntity getDirectEntity();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntity getEntity();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;getLocalizedDeathMessage(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/network/chat/Component;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefComponent getLocalizedDeathMessage(RefEntityLiving killed);

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;getMsgId()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native String getMsgId();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;scalesWithDifficulty()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean scalesWithDifficulty();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;isCreativePlayer()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean isCreativePlayer();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;getSourcePosition()Lnet/minecraft/world/phys/Vec3;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefVec3 getSourcePosition();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;sourcePositionRaw()Lnet/minecraft/world/phys/Vec3;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefVec3 sourcePositionRaw();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native boolean is(RefTagKey<RefDamageType> tag);

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;type()Lnet/minecraft/world/damagesource/DamageType;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefDamageType type();

    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;typeHolder()Lnet/minecraft/core/Holder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefHolder<RefDamageType> typeHolder();

    // critical相关判断为paper独有
//    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;isCritical()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
//    public native boolean isCritical();
//
//    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;critical()Lnet/minecraft/world/damagesource/DamageSource;", predicates = "craftbukkit_version:[v1_17_R1,)")
//    public native RefDamageSource critical();
//
//    @HandleBy(reference = "Lnet/minecraft/world/damagesource/DamageSource;critical(Z)Lnet/minecraft/world/damagesource/DamageSource;", predicates = "craftbukkit_version:[v1_17_R1,)")
//    public native RefDamageSource critical(boolean critical);
}
