package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/InteractionResult", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EnumInteractionResult", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefEnumInteractionResult {
    @HandleBy(reference = "Lnet/minecraft/world/InteractionResult;SUCCESS:Lnet/minecraft/world/InteractionResult;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;SUCCESS:Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static final RefEnumInteractionResult SUCCESS = null;

    @HandleBy(reference = "Lnet/minecraft/world/InteractionResult;CONSUME:Lnet/minecraft/world/InteractionResult;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_15_R1/EnumInteractionResult;CONSUME:Lnet/minecraft/server/v1_15_R1/EnumInteractionResult;", predicates = "craftbukkit_version:[v1_15_R1,v1_17_R1)")
    public static final RefEnumInteractionResult CONSUME = null;

    @HandleBy(reference = "Lnet/minecraft/world/InteractionResult;CONSUME_PARTIAL:Lnet/minecraft/world/InteractionResult;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public static final RefEnumInteractionResult CONSUME_PARTIAL = null;

    @HandleBy(reference = "Lnet/minecraft/world/InteractionResult;PASS:Lnet/minecraft/world/InteractionResult;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;PASS:Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static final RefEnumInteractionResult PASS = null;

    @HandleBy(reference = "Lnet/minecraft/world/InteractionResult;FAIL:Lnet/minecraft/world/InteractionResult;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;FAIL:Lnet/minecraft/server/v1_12_R1/EnumInteractionResult;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static final RefEnumInteractionResult FAIL = null;
}
