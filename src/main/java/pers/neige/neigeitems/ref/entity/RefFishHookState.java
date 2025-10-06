package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/entity/projectile/FishingHook$FishHookState", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityFishingHook$HookState", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefFishHookState {
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook$FishHookState;FLYING:Lnet/minecraft/world/entity/projectile/FishingHook$FishHookState;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook$HookState;FLYING:Lnet/minecraft/server/v1_12_R1/EntityFishingHook$HookState;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static final RefFishHookState FLYING = null;
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook$FishHookState;HOOKED_IN_ENTITY:Lnet/minecraft/world/entity/projectile/FishingHook$FishHookState;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook$HookState;HOOKED_IN_ENTITY:Lnet/minecraft/server/v1_12_R1/EntityFishingHook$HookState;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static final RefFishHookState HOOKED_IN_ENTITY = null;
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook$FishHookState;BOBBING:Lnet/minecraft/world/entity/projectile/FishingHook$FishHookState;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook$HookState;BOBBING:Lnet/minecraft/server/v1_12_R1/EntityFishingHook$HookState;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public static final RefFishHookState BOBBING = null;
}
