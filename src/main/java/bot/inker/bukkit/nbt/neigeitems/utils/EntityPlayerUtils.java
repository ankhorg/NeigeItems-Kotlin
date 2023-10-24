package bot.inker.bukkit.nbt.neigeitems.utils;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.argument.RefAnchor;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.*;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.network.RefPacketPlayInFlying;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.network.RefPacketPlayOutAnimation;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.network.RefPacketPlayOutLookAt;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefVec3;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefWorld;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.EnumHand;

import java.lang.reflect.Constructor;

public class EntityPlayerUtils {
    /**
     * 1.15+ 版本起, EntityLiving 内部 swing 方法可以传入一个布尔量.
     * 该布尔量为 true 时, 向周围生物及该实体本身发送摆动对应手臂的动作数据包.
     * 该布尔量为 false 时, 仅向周围生物发送数据包, 不向该实体本身发送.
     * 以往版本中默认不向该实体本身发送.
     */
    private static final boolean SWING_AND_SEND_SUPPORT = CbVersion.v1_15_R1.isSupport();

    /**
     * 1.16.2+ 版本起, Damageable 接口下添加 getAbsorptionAmount 及 setAbsorptionAmount 方法, 用于操作伤害吸收的黄心数值.
     * 高版本的 getAbsorptionAmount 返回的是 double, 而 NMS 内部返回的实质上是 float, 是 OBC 将 float 强转为 double 并返回.
     * 总觉得有一种小脑发育不完全的美, 所以本工具类的 getAbsorptionAmount 干脆全盘使用 NMS 操作, 不在 1.16.2+ 版本调用 BukkitAPI 了.
     */
    private static final boolean ABSORPTION_SUPPORT = CbVersion.v1_16_R2.isSupport();

    /**
     * 1.15.2+ 版本起, HumanEntity 接口下添加 getAttackCooldown 方法, 用于获取玩家当前攻击冷却.
     * 但是 1.15 - 1.15.2 版本的 CraftBukkit 版本均为 v1_15_R1.
     * 为防止出现问题, 直接看作 v1_16_R1 起步.
     */
    private static final boolean ATTACK_COOLDOWN_SUPPORT = CbVersion.v1_16_R1.isSupport();

    /**
     * 1.14+ 版本起, Entity 接口添加 setRotation 方法, 用于设置实体朝向.
     */
    private static final boolean SET_ROTATION_SUPPORT = CbVersion.v1_14_R1.isSupport();

    /**
     * 1.17+ 版本起, Entity 内部的添加 lookAt 方法, 用于使玩家看向指定位置.
     */
    private static final boolean LOOK_AT_SUPPORT = CbVersion.v1_17_R1.isSupport();

    /**
     * 1.13+ 版本起, 添加 PacketPlayOutLookAt 数据包, 用于使玩家看向指定位置.
     */
    private static final boolean PACKET_LOOK_AT_SUPPORT = CbVersion.v1_13_R1.isSupport();

    /**
     * 1.17+ 版本起, PacketPlayInFlying 的构造器发生变化, 同时内部变量全部添加 final 修饰符.
     */
    private static final boolean NEW_MOVE_PACKET_CONSTRUCTOR = CbVersion.v1_17_R1.isSupport();

    /**
     * 让指定玩家攻击指定实体.
     *
     * @param player 攻击者.
     * @param entity 防御者.
     */
    public static void attack(
            @NotNull Player player,
            @NotNull Entity entity
    ) {
        if ((Object) player instanceof RefCraftPlayer) {
            RefEntityPlayer attacker = ((RefCraftPlayer) (Object) player).getHandle();
            if (entity instanceof RefCraftEntity) {
                RefEntity defender = ((RefCraftEntity) entity).getHandle();
                attacker.attack(defender);
            }
        }
    }

    /**
     * 获取指定实体的攻击冷却.
     *
     * @param entity 待获取实体.
     */
    public static float getAttackCooldown(
            @NotNull HumanEntity entity
    ) {
        if (ATTACK_COOLDOWN_SUPPORT) {
            return entity.getAttackCooldown();
        } else {
            return getAttackCooldown((LivingEntity) entity);
        }
    }

    /**
     * 获取指定实体的攻击冷却.
     *
     * @param entity 待获取实体.
     */
    public static float getAttackCooldown(
            @NotNull LivingEntity entity
    ) {
        if (entity instanceof RefCraftLivingEntity) {
            RefEntityLiving livingEntity = ((RefCraftLivingEntity) entity).getHandle();
            int attackStrengthTicker = livingEntity.attackStrengthTicker;
            AttributeInstance attackSpeedAttribute = entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            double attackSpeed;
            if (attackSpeedAttribute != null) {
                attackSpeed = attackSpeedAttribute.getValue();
            } else {
                attackSpeed = 4;
            }
            float value = (float) ((attackStrengthTicker + 0.5) / (1.0 / attackSpeed * 20.0));
            return value < 0 ? 0 : Math.min(value, 1);
        }
        return 0;
    }

    /**
     * 获取指定实体距上次攻击行为有多久(tick).
     *
     * @param entity 待获取实体.
     */
    public static int getAttackStrengthTicker(
            @NotNull LivingEntity entity
    ) {
        if (entity instanceof RefCraftLivingEntity) {
            RefEntityLiving livingEntity = ((RefCraftLivingEntity) entity).getHandle();
            return livingEntity.attackStrengthTicker;
        }
        return 0;
    }

    /**
     * 设置指定实体距上次攻击行为有多久(tick).
     *
     * @param entity 待获取实体.
     * @param attackStrengthTicker 实体距上次攻击行为的时间(tick).
     */
    public static void setAttackStrengthTicker(
            @NotNull LivingEntity entity,
            int attackStrengthTicker
    ) {
        if (entity instanceof RefCraftLivingEntity) {
            RefEntityLiving livingEntity = ((RefCraftLivingEntity) entity).getHandle();
            livingEntity.attackStrengthTicker = attackStrengthTicker;
        }
    }

    /**
     * 让指定玩家使用对应手持物品.
     *
     * @param player 待操作玩家.
     * @param hand 待使用物品槽位.
     */
    public static void useItem(
            @NotNull Player player,
            @NotNull EnumHand hand
    ) {
        if ((Object) player instanceof RefCraftPlayer) {
            RefEntityPlayer entityPlayer = ((RefCraftPlayer) (Object) player).getHandle();
            RefEnumHand enumHand = toRefEnumHand(hand);
            RefWorld world = entityPlayer.world;
            RefNmsItemStack itemStack = entityPlayer.getItemInHand(enumHand);
            entityPlayer.playerInteractManager.useItem(entityPlayer, world, itemStack, enumHand);
        }
    }

    /**
     * 摆动指定玩家的指定手臂.
     *
     * @param player 待操作玩家.
     * @param hand 待摆动手臂.
     */
    public static void swing(
            @NotNull Player player,
            @NotNull EnumHand hand
    ) {
        swing(player, hand, true);
    }

    /**
     * 摆动指定玩家的指定手臂.
     *
     * @param player 待操作玩家.
     * @param hand 待摆动手臂.
     * @param fromServerPlayer 本人是否可见摆动动作.
     */
    public static void swing(
            @NotNull Player player,
            @NotNull EnumHand hand,
            boolean fromServerPlayer
    ) {
        if ((Object) player instanceof RefCraftPlayer) {
            RefEntityPlayer entityPlayer = ((RefCraftPlayer) (Object) player).getHandle();
            RefEnumHand enumHand = toRefEnumHand(hand);
            swing(entityPlayer, enumHand, fromServerPlayer);
        }
    }

    /**
     * 获取玩家的伤害吸收数值.
     *
     * @param player 待获取玩家.
     */
    public static float getAbsorptionAmount(
            @NotNull Player player
    ) {
        if ((Object) player instanceof RefCraftPlayer) {
            return ((RefCraftPlayer) (Object) player).getHandle().getAbsorptionAmount();
        }
        return 0;
    }

    /**
     * 设置玩家的伤害吸收数值.
     *
     * @param player 待操作玩家.
     * @param amount 伤害吸收数值.
     */
    public static void setAbsorptionAmount(
            @NotNull Player player,
            float amount
    ) {
        if ((Object) player instanceof RefCraftPlayer) {
            ((RefCraftPlayer) (Object) player).getHandle().setAbsorptionAmount(amount);
        }
    }

    /**
     * 强制实体从地上跳起(浮空时不生效).
     *
     * @param humanEntity 待操作实体.
     */
    public static void jumpFromGround(
            @NotNull HumanEntity humanEntity
    ) {
        if (humanEntity.isOnGround() && humanEntity instanceof RefCraftHumanEntity) {
            ((RefCraftHumanEntity) humanEntity).getHandle().jumpFromGround();
        }
    }

    /**
     * 强制实体从地上跳起(浮空时不生效).
     *
     * @param entity 待操作实体.
     */
    public static void jumpFromGround(
            @NotNull LivingEntity entity
    ) {
        if (entity.isOnGround() && entity instanceof RefCraftLivingEntity) {
            ((RefCraftLivingEntity) entity).getHandle().jumpFromGround();
        }
    }

    /**
     * 设置玩家朝向(仅适用于1.13+).
     *
     * @param player 待设置玩家.
     * @param yaw 实体偏航角.
     * @param pitch 实体俯仰角.
     */
    public static void setRotation(
            @NotNull Player player,
            float yaw,
            float pitch
    ) {
        if (SET_ROTATION_SUPPORT) {
            player.setRotation(yaw, pitch);
        } else {
            if ((Object) player instanceof RefCraftPlayer) {
                Location targetLocation = player.getEyeLocation();
                targetLocation.setYaw(yaw);
                targetLocation.setPitch(pitch);
                Vector direction = targetLocation.getDirection();
                direction.multiply(9999999);
                targetLocation.add(direction);
                lookAtByNms(player, targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
            }
        }
    }

    /**
     * 令玩家看向指定坐标(仅适用于1.13+).
     *
     * @param player 待操作玩家.
     * @param target 目标坐标.
     */
    public static void lookAt(
            @NotNull Player player,
            @NotNull Location target
    ) {
        lookAt(player, target.getX(), target.getY(), target.getZ());
    }

    /**
     * 令玩家看向指定坐标(仅适用于1.13+).
     *
     * @param player 待操作玩家.
     * @param x 目标 x 坐标.
     * @param y 目标 y 坐标.
     * @param z 目标 z 坐标.
     */
    public static void lookAt(
            @NotNull Player player,
            double x,
            double y,
            double z
    ) {
        if ((Object) player instanceof RefCraftPlayer) {
            if (LOOK_AT_SUPPORT) {
                ((RefCraftPlayer) (Object) player).getHandle().lookAt(RefAnchor.EYES, new RefVec3(x, y, z));
            } else {
                lookAtByNms(player, x, y, z);
            }
        }
    }

    public static void receiveMovePacket(
            Player player,
            float yaw,
            float pitch,
            boolean onGround
    ) {
        if ((Object) player instanceof RefCraftPlayer) {
            RefEntityPlayer entityPlayer = ((RefCraftPlayer) (Object) player).getHandle();
            entityPlayer.playerConnection.handleMovePlayer(newPlayerMovePacket(yaw, pitch, onGround));
        }
    }

    public static void receiveMovePacket(
            Player player,
            double x,
            double y,
            double z,
            boolean onGround
    ) {
        if ((Object) player instanceof RefCraftPlayer) {
            RefEntityPlayer entityPlayer = ((RefCraftPlayer) (Object) player).getHandle();
            entityPlayer.playerConnection.handleMovePlayer(newPlayerMovePacket(x, y, z, onGround));
        }
    }

    public static void receiveMovePacket(
            Player player,
            double x,
            double y,
            double z,
            float yaw,
            float pitch,
            boolean onGround
    ) {
        if ((Object) player instanceof RefCraftPlayer) {
            RefEntityPlayer entityPlayer = ((RefCraftPlayer) (Object) player).getHandle();
            entityPlayer.playerConnection.handleMovePlayer(newPlayerMovePacket(x, y, z, yaw, pitch, onGround));
        }
    }

    private static RefPacketPlayInFlying newPlayerMovePacket(
            float yaw,
            float pitch,
            boolean onGround
    ) {
        if (NEW_MOVE_PACKET_CONSTRUCTOR) {
            return new RefPacketPlayInFlying.Rot(yaw, pitch, onGround);
        } else {
            RefPacketPlayInFlying packet = new RefPacketPlayInFlying.Rot();
            packet.yaw = yaw;
            packet.pitch = pitch;
            packet.onGround = onGround;
            packet.changePosition = false;
            return packet;
        }
    }

    private static RefPacketPlayInFlying newPlayerMovePacket(
            double x,
            double y,
            double z,
            boolean onGround
    ) {
        if (NEW_MOVE_PACKET_CONSTRUCTOR) {
            return new RefPacketPlayInFlying.Pos(x, y, z, onGround);
        } else {
            RefPacketPlayInFlying packet = new RefPacketPlayInFlying.Pos();
            packet.x = x;
            packet.y = y;
            packet.z = z;
            packet.onGround = onGround;
            packet.changeLook = false;
            return packet;
        }
    }

    private static RefPacketPlayInFlying newPlayerMovePacket(
            double x,
            double y,
            double z,
            float yaw,
            float pitch,
            boolean onGround
    ) {
        if (NEW_MOVE_PACKET_CONSTRUCTOR) {
            return new RefPacketPlayInFlying.PosRot(x, y, z, yaw, pitch, onGround);
        } else {
            RefPacketPlayInFlying packet = new RefPacketPlayInFlying.PosRot();
            packet.x = x;
            packet.y = y;
            packet.z = z;
            packet.yaw = yaw;
            packet.pitch = pitch;
            packet.onGround = onGround;
            return packet;
        }
    }

    private static void lookAtByNms(
            @NotNull Player player,
            double x,
            double y,
            double z
    ) {
        RefEntityPlayer nmsPlayer = ((RefCraftPlayer) (Object) player).getHandle();
        if (PACKET_LOOK_AT_SUPPORT) {
            EntityUtils.lookAtByNms(player, x, y, z);
            nmsPlayer.playerConnection.sendPacket(new RefPacketPlayOutLookAt(RefAnchor.EYES, x, y, z));
        } else {
            player.teleport(player.getLocation().setDirection(player.getEyeLocation().multiply(-1).add(x, y, z).toVector()));
        }
    }

    private static void swing(
            @NotNull RefEntityPlayer player,
            @NotNull RefEnumHand hand
    ) {
        swing(player, hand, true);
    }

    private static void swing(
            @NotNull RefEntityPlayer player,
            @NotNull RefEnumHand hand,
            boolean fromServerPlayer
    ) {
        if (SWING_AND_SEND_SUPPORT) {
            player.swing(hand, fromServerPlayer);
        } else {
            if (fromServerPlayer) {
                player.playerConnection.sendPacket(new RefPacketPlayOutAnimation(player, hand == RefEnumHand.MAIN_HAND ? 0 : 3));
            }
            player.swing(hand);
        }
    }

    @NotNull
    private static RefEnumHand toRefEnumHand(
            @NotNull EnumHand hand
    ) {
        if (hand == EnumHand.MAIN_HAND) {
            return RefEnumHand.MAIN_HAND;
        } else {
            return RefEnumHand.OFF_HAND;
        }
    }
}
