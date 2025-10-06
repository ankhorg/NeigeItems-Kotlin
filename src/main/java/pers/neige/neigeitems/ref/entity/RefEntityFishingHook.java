package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/entity/projectile/FishingHook", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityFishingHook", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefEntityFishingHook extends RefEntity {
    /**
     * 鱼钩是否勾在了地上.
     */
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;isInGround:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public boolean isInGround;
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook;biting:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public boolean biting;
    /**
     * 鱼钩挂地时间
     */
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook;life:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;d:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int life;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;owner:Lnet/minecraft/server/EntityHuman;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefEntityHuman owner;
    /**
     * 在天上飞的时间, 单位是tick(1.17+版本中, 入水后这个时间不会清零, 而是会随时间抵消)
     */
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook;outOfWaterTime:I", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;f:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int outOfWaterTime;
    /**
     * 咬钩多久脱钩.
     */
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook;nibble:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;g:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int nibble;
    /**
     * 等待多久后有鱼游过来.
     */
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook;timeUntilLured:I", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;h:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int timeUntilLured;
    /**
     * 游多久咬钩.
     */
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook;timeUntilHooked:I", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;at:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int timeUntilHooked;
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook;fishAngle:F", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;au:F", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public float fishAngle;
    /**
     * 被鱼钩勾住的实体
     */
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook;hookedIn:Lnet/minecraft/world/entity/Entity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;hooked:Lnet/minecraft/server/v1_12_R1/Entity;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefEntity hooked;
    /**
     * 当前鱼钩状态
     */
    @HandleBy(reference = "Lnet/minecraft/world/entity/projectile/FishingHook;currentState:Lnet/minecraft/world/entity/projectile/FishingHook$FishHookState;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityFishingHook;av:Lnet/minecraft/server/v1_12_R1/EntityFishingHook$HookState;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefFishHookState currentState;
}
