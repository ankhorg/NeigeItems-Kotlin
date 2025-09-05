package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NeigeItemsUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.argument.RefAnchor;
import pers.neige.neigeitems.ref.chat.RefChatSerializer;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.entity.*;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import pers.neige.neigeitems.ref.server.level.RefTrackedEntity;
import pers.neige.neigeitems.ref.world.RefVec3;

public class EntityUtils {
    /**
     * 1.14+ 版本起, Entity#move 方法需传入一个 Vec3 实例.
     * 在此前的版本, Entity#move 方法需传入三个 double, 用于确定方向.
     */
    private static final boolean VEC3_MOVE_SUPPORT = CbVersion.v1_14_R1.isSupport();

    /**
     * 1.17+ 版本起, Entity 内部的 yaw/pitch 字段设置为私有, 需要通过方法进行操作.
     */
    private static final boolean GET_YAW_SUPPORT = CbVersion.v1_17_R1.isSupport();

    /**
     * 1.14+ 版本起, Entity 接口添加 setRotation 方法, 用于设置实体朝向.
     */
    private static final boolean SET_ROTATION_SUPPORT = CbVersion.v1_14_R1.isSupport();

    /**
     * 1.17+ 版本起, Entity 内部的添加 lookAt 方法, 用于使实体看向指定位置.
     */
    private static final boolean LOOK_AT_SUPPORT = CbVersion.v1_17_R1.isSupport();
    /**
     * 1.13+ 版本起, Entity 类 setCustomName 方法由接收 String 改为接收 IChatBaseComponent.
     */
    private static final boolean COMPONENT_NAME_SUPPORT = CbVersion.v1_13_R1.isSupport();

    /**
     * 将实体信息保存至 NBT.
     *
     * @param entity 待保存实体.
     * @return 包含实体信息的 NBT.
     */
    public static @Nullable NbtCompound save(
            @NonNull Entity entity
    ) {
        if (entity instanceof RefCraftEntity) {
            RefNbtTagCompound nbt = new RefNbtTagCompound();
            ((RefCraftEntity) entity).getHandle().save(nbt);
            return NeigeItemsUtils.fromNms(nbt);
        }
        return null;
    }

    /**
     * 令实体加载 NBT 信息.
     *
     * @param entity 待操作实体.
     * @param nbt    待加载 NBT.
     */
    public static void load(
            @NonNull Entity entity,
            @NonNull NbtCompound nbt
    ) {
        if (entity instanceof RefCraftEntity) {
            ((RefCraftEntity) entity).getHandle().load(NeigeItemsUtils.toNms(nbt));
        }
    }

    /**
     * 让实体尝试寻路并移动到对应位置.
     *
     * @param entity 待操作实体.
     * @param x      目标点 x 轴坐标.
     * @param y      目标点 y 轴坐标.
     * @param z      目标点 z 轴坐标.
     * @param speed  移动速度.
     * @return 寻路是否成功.
     */
    public static boolean tryToMoveTo(
            @NonNull LivingEntity entity,
            double x,
            double y,
            double z,
            double speed
    ) {
        if (entity instanceof RefCraftLivingEntity) {
            RefEntityLiving living = ((RefCraftLivingEntity) entity).getHandle();
            if (living instanceof RefMob) {
                return ((RefMob) living).getNavigation().moveTo(x, y, z, speed);
            }
        }
        return false;
    }

    /**
     * 让实体尝试寻路并移动到对应实体位置.
     *
     * @param entity 待操作实体.
     * @param target 目标实体.
     * @param speed  移动速度.
     * @return 寻路是否成功.
     */
    public static boolean tryToMoveTo(
            @NonNull LivingEntity entity,
            Entity target,
            double speed
    ) {
        if (entity instanceof RefCraftLivingEntity && target instanceof RefCraftEntity) {
            RefEntityLiving living = ((RefCraftLivingEntity) entity).getHandle();
            if (living instanceof RefMob) {
                return ((RefMob) living).getNavigation().moveTo(((RefCraftEntity) target).getHandle(), speed);
            }
        }
        return false;
    }

    /**
     * 尝试tick实体.
     *
     * @param entity 待操作实体.
     */
    public static void tick(@NonNull Entity entity) {
        if (entity instanceof RefCraftEntity) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            nmsEntity.tick();
        }
    }

    /**
     * 尝试tick实体寻路.
     *
     * @param entity 待操作实体.
     */
    public static void tickNavigation(@NonNull LivingEntity entity) {
        if (entity instanceof RefCraftLivingEntity) {
            RefEntityLiving living = ((RefCraftLivingEntity) entity).getHandle();
            if (living instanceof RefMob) {
                RefMob mob = (RefMob) living;
                mob.getNavigation().tick();
                mob.getLookControl().tick();
                mob.getMoveControl().tick();
                mob.getJumpControl().tick();
                living.travel(living.xxa, living.yya, living.zza);

                double d0 = living.locX - living.lastX;
                double d1 = living.locZ - living.lastZ;
                float f = (float) ((d0 * d0) + (d1 * d1));
                float f1 = living.yBodyRot;
                float f2 = 0.0f;
                living.oRun = living.run;
                float f3 = 0.0f;
                if (f > 0.0025000002f) {
                    f3 = 1.0f;
                    f2 = ((float) Math.sqrt(f)) * 3.0f;
                    float f4 = (((float) MathUtils.c(d1, d0)) * 57.295776f) - 90.0f;
                    float f5 = MathUtils.e(MathUtils.g(living.yaw) - f4);
                    if (95.0f < f5 && f5 < 265.0f) {
                        f1 = f4 - 180.0f;
                    } else {
                        f1 = f4;
                    }
                }
                if (living.attackAnim > 0.0f) {
                    f1 = living.yaw;
                }
                if (!entity.isOnGround()) {
                    f3 = 0.0f;
                }
                living.run += (f3 - living.run) * 0.3f;
                float f22 = living.tickHeadTurn(f1, f2);
                while (living.yaw - living.lastYaw < -180.0f) {
                    living.lastYaw -= 360.0f;
                }
                while (living.yaw - living.lastYaw >= 180.0f) {
                    living.lastYaw += 360.0f;
                }
                while (living.yBodyRot - living.yBodyRotO < -180.0f) {
                    living.yBodyRotO -= 360.0f;
                }
                while (living.yBodyRot - living.yBodyRotO >= 180.0f) {
                    living.yBodyRotO += 360.0f;
                }
                while (living.pitch - living.lastPitch < -180.0f) {
                    living.lastPitch -= 360.0f;
                }
                while (living.pitch - living.lastPitch >= 180.0f) {
                    living.lastPitch += 360.0f;
                }
                while (living.yHeadRot - living.yHeadRotO < -180.0f) {
                    living.yHeadRotO -= 360.0f;
                }
                while (living.yHeadRot - living.yHeadRotO >= 180.0f) {
                    living.yHeadRotO += 360.0f;
                }
                living.animStep += f22;
                if (living.isFallFlying()) {
                    living.fallFlyTicks++;
                } else {
                    living.fallFlyTicks = 0;
                }
            }
        }
    }

    /**
     * 强制实体移动.
     *
     * @param entity 待操作实体.
     * @param x      移动方向 x 轴分量.
     * @param y      移动方向 y 轴分量.
     * @param z      移动方向 z 轴分量.
     */
    public static void move(
            @NonNull Entity entity,
            double x,
            double y,
            double z
    ) {
        if (entity instanceof RefCraftEntity) {
            if (VEC3_MOVE_SUPPORT) {
                ((RefCraftEntity) entity).getHandle().move(RefMoverType.PLAYER, new RefVec3(x, y, z));
            } else {
                ((RefCraftEntity) entity).getHandle().move(RefMoverType.PLAYER, x, y, z);
            }
        }
    }

    /**
     * 获取实体俯仰角.
     *
     * @param entity 待检测实体.
     * @return 实体俯仰角.
     */
    public static float getPitch(
            @NonNull Entity entity
    ) {
        if (entity instanceof RefCraftEntity) {
            return getPitchByNms(((RefCraftEntity) entity).getHandle());
        }
        return 0;
    }

    /**
     * 获取实体偏航角.
     *
     * @param entity 待检测实体.
     * @return 实体偏航角.
     */
    public static float getYaw(
            @NonNull Entity entity
    ) {
        if (entity instanceof RefCraftEntity) {
            return getYawByNms(((RefCraftEntity) entity).getHandle());
        }
        return 0;
    }

    /**
     * 设置非玩家实体朝向.
     *
     * @param entity 待设置实体.
     * @param yaw    实体偏航角.
     * @param pitch  实体俯仰角.
     */
    public static void setRotation(
            @NonNull Entity entity,
            float yaw,
            float pitch
    ) {
        if (SET_ROTATION_SUPPORT) {
            entity.setRotation(yaw, pitch);
        } else {
            if (entity instanceof RefCraftEntity) {
                RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
                NumberConversions.checkFinite(yaw, "yaw not finite");
                NumberConversions.checkFinite(pitch, "pitch not finite");
                yaw = normalizeYawByNms(yaw);
                pitch = normalizePitchByNms(pitch);
                nmsEntity.yaw = yaw;
                nmsEntity.pitch = pitch;
                nmsEntity.lastYaw = yaw;
                nmsEntity.lastPitch = pitch;
                nmsEntity.setHeadRotation(yaw);
            }
        }
    }

    public static float getHeadRotation(
            @NonNull Entity entity
    ) {
        if (entity instanceof RefCraftEntity) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            return nmsEntity.getHeadRotation();
        }
        return 0;
    }

    public static void setHeadRotation(
            @NonNull Entity entity,
            float headRotation
    ) {
        if (entity instanceof RefCraftEntity) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            nmsEntity.setHeadRotation(headRotation);
        }
    }

    public static void absMoveTo(
            @NonNull Entity entity,
            double x,
            double y,
            double z,
            float yaw,
            float pitch
    ) {
        if (entity instanceof RefCraftEntity) {
            ((RefCraftEntity) entity).getHandle().absMoveTo(x, y, z, yaw, pitch);
        }
    }

    public static void moveTo(
            @NonNull Entity entity,
            double x,
            double y,
            double z,
            float yaw,
            float pitch
    ) {
        if (entity instanceof RefCraftEntity) {
            ((RefCraftEntity) entity).getHandle().moveTo(x, y, z, yaw, pitch);
        }
    }

    /**
     * 令实体看向指定坐标.
     *
     * @param entity 待操作实体.
     * @param target 目标坐标.
     */
    public static void lookAt(
            @NonNull Entity entity,
            @NonNull Location target
    ) {
        lookAt(entity, target.getX(), target.getY(), target.getZ());
    }

    /**
     * 令实体看向指定坐标.
     *
     * @param entity 待操作实体.
     * @param x      目标 x 坐标.
     * @param y      目标 y 坐标.
     * @param z      目标 z 坐标.
     */
    public static void lookAt(
            @NonNull Entity entity,
            double x,
            double y,
            double z
    ) {
        if (entity instanceof RefCraftEntity) {
            if (LOOK_AT_SUPPORT) {
                ((RefCraftEntity) entity).getHandle().lookAt(RefAnchor.EYES, new RefVec3(x, y, z));
            } else {
                lookAtByNms(entity, x, y, z);
            }
        }
    }

    protected static void lookAtByNms(
            @NonNull Entity entity,
            double x,
            double y,
            double z
    ) {
        RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
        Location location = entity.getLocation();
        double d0 = x - location.getX();
        double d1 = y - location.getY() + nmsEntity.getEyeHeight();
        double d2 = z - location.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        nmsEntity.pitch = wrapDegrees((float) (-(Math.atan2(d1, d3) * 57.2957763671875D)));
        nmsEntity.yaw = wrapDegrees((float) (Math.atan2(d2, d0) * 57.2957763671875D) - 90.0F);
        nmsEntity.setHeadRotation(nmsEntity.yaw);
        nmsEntity.lastPitch = nmsEntity.pitch;
        nmsEntity.lastYaw = nmsEntity.yaw;
    }

    /**
     * 令实体看向指定坐标.
     *
     * @param entity 待操作实体.
     * @param target 目标坐标.
     */
    public static void lookAt(
            @NonNull LivingEntity entity,
            @NonNull Location target
    ) {
        lookAt(entity, target.getX(), target.getY(), target.getZ());
    }

    /**
     * 令实体看向指定坐标.
     *
     * @param entity 待操作实体.
     * @param x      目标 x 坐标.
     * @param y      目标 y 坐标.
     * @param z      目标 z 坐标.
     */
    public static void lookAt(
            @NonNull LivingEntity entity,
            double x,
            double y,
            double z
    ) {
        if (entity instanceof RefCraftLivingEntity) {
            if (LOOK_AT_SUPPORT) {
                ((RefCraftLivingEntity) entity).getHandle().lookAt(RefAnchor.EYES, new RefVec3(x, y, z));
            } else {
                lookAtByNms(entity, x, y, z);
            }
        }
    }

    /**
     * 为实体设置自定义名称.
     *
     * @param entity 待操作实体.
     * @param name   自定义名称.
     */
    public static void setCustomName(
            @NonNull Entity entity,
            @NonNull BaseComponent name
    ) {
        if (COMPONENT_NAME_SUPPORT && entity instanceof RefCraftEntity) {
            ((RefCraftEntity) entity).getHandle().setCustomName(toNms(name));
        }
    }

    /**
     * 获取实体ID.
     *
     * @param entity 待获取实体.
     * @return 实体ID.
     */
    public static int getId(
            @NonNull Entity entity
    ) {
        if (entity instanceof RefCraftEntity) {
            return ((RefCraftEntity) entity).getHandle().getId();
        }
        return 0;
    }

    /**
     * 设置实体隐身状态.
     *
     * @param entity    待操作实体.
     * @param invisible 是否隐身.
     */
    public static void setInvisible(
            @NonNull Entity entity,
            boolean invisible
    ) {
        if (entity instanceof RefCraftEntity) {
            ((RefCraftEntity) entity).getHandle().setInvisible(invisible);
        }
    }

    /**
     * 从 TrackedEntity 中获取 Entity.
     *
     * @param trackedEntity 待操作实体.
     */
    public static @NonNull Entity getEntityFromTrackedEntity(
            @NonNull Object trackedEntity
    ) {
        if (!(trackedEntity instanceof RefTrackedEntity)) {
            throw new IllegalArgumentException("trackedEntity must be of type RefTrackedEntity");
        }
        return ((RefTrackedEntity) trackedEntity).entity.getBukkitEntity();
    }

    protected static RefComponent toNms(BaseComponent component) {
        return RefChatSerializer.fromJson(ComponentSerializer.toString(component));
    }

    protected static void lookAtByNms(
            @NonNull LivingEntity entity,
            double x,
            double y,
            double z
    ) {
        RefEntityLiving nmsEntity = ((RefCraftLivingEntity) entity).getHandle();
        lookAtByNms((Entity) entity, x, y, z);
        nmsEntity.yHeadRotO = nmsEntity.yHeadRot;
        nmsEntity.yBodyRot = nmsEntity.yHeadRot;
        nmsEntity.yBodyRotO = nmsEntity.yBodyRot;
    }

    public static float wrapDegrees(float degrees) {
        float f = degrees % 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

    private static float normalizeYawByNms(float yaw) {
        yaw %= 360.0F;
        if (yaw >= 180.0F) {
            yaw -= 360.0F;
        } else if (yaw < -180.0F) {
            yaw += 360.0F;
        }

        return yaw;
    }

    private static float normalizePitchByNms(float pitch) {
        if (pitch > 90.0F) {
            pitch = 90.0F;
        } else if (pitch < -90.0F) {
            pitch = -90.0F;
        }

        return pitch;
    }

    private static float getPitchByNms(
            @NonNull RefEntity entity
    ) {
        if (GET_YAW_SUPPORT) {
            return entity.getPitch();
        } else {
            return entity.pitch;
        }
    }

    private static float getYawByNms(
            @NonNull RefEntity entity
    ) {
        if (GET_YAW_SUPPORT) {
            return entity.getYaw();
        } else {
            return entity.yaw;
        }
    }
}
