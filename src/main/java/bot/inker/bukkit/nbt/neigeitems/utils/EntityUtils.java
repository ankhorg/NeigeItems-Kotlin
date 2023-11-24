package bot.inker.bukkit.nbt.neigeitems.utils;

import bot.inker.bukkit.nbt.NbtCompound;
import bot.inker.bukkit.nbt.NeigeItemsUtils;
import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.ref.RefNbtTagCompound;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.argument.RefAnchor;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.chat.RefChatSerializer;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.chat.RefComponent;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.*;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefVec3;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    public static NbtCompound save(
            @NotNull Entity entity
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
            @NotNull Entity entity,
            @NotNull NbtCompound nbt
    ) {
        if (entity instanceof RefCraftEntity) {
            ((RefCraftEntity) entity).getHandle().load(NeigeItemsUtils.toNms(nbt));
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
            @NotNull Entity entity,
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
            @NotNull Entity entity
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
            @NotNull Entity entity
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
            @NotNull Entity entity,
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
            @NotNull Entity entity
    ) {
        if (entity instanceof RefCraftEntity) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            return nmsEntity.getHeadRotation();
        }
        return 0;
    }

    public static void setHeadRotation(
            @NotNull Entity entity,
            float headRotation
    ) {
        if (entity instanceof RefCraftEntity) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            nmsEntity.setHeadRotation(headRotation);
        }
    }

    public static void absMoveTo(
            @NotNull Entity entity,
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
            @NotNull Entity entity,
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
            @NotNull Entity entity,
            @NotNull Location target
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
            @NotNull Entity entity,
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
            @NotNull Entity entity,
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
            @NotNull LivingEntity entity,
            @NotNull Location target
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
            @NotNull LivingEntity entity,
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
            @NotNull Entity entity,
            @NotNull BaseComponent name
    ) {
        if (COMPONENT_NAME_SUPPORT && entity instanceof RefCraftEntity) {
            ((RefCraftEntity) entity).getHandle().setCustomName(toNms(name));
        }
    }

    protected static RefComponent toNms(BaseComponent component) {
        return RefChatSerializer.fromJson(ComponentSerializer.toString(component));
    }

    protected static void lookAtByNms(
            @NotNull LivingEntity entity,
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
            @NotNull RefEntity entity
    ) {
        if (GET_YAW_SUPPORT) {
            return entity.getPitch();
        } else {
            return entity.pitch;
        }
    }

    private static float getYawByNms(
            @NotNull RefEntity entity
    ) {
        if (GET_YAW_SUPPORT) {
            return entity.getYaw();
        } else {
            return entity.yaw;
        }
    }
}
