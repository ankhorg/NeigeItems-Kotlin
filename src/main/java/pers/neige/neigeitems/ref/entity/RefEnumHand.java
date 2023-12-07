package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/InteractionHand", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EnumHand", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefEnumHand {
    @HandleBy(reference = "Lnet/minecraft/world/InteractionHand;MAIN_HAND:Lnet/minecraft/world/InteractionHand;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EnumHand;MAIN_HAND:Lnet/minecraft/server/v1_12_R1/EnumHand;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static final RefEnumHand MAIN_HAND = null;

    @HandleBy(reference = "Lnet/minecraft/world/InteractionHand;OFF_HAND:Lnet/minecraft/world/InteractionHand;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EnumHand;OFF_HAND:Lnet/minecraft/server/v1_12_R1/EnumHand;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static final RefEnumHand OFF_HAND = null;
}
