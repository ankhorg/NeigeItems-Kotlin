package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.EnumHand;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.animation.AnimationType;
import pers.neige.neigeitems.ref.argument.RefAnchor;
import pers.neige.neigeitems.ref.block.RefBlockPos;
import pers.neige.neigeitems.ref.block.sign.RefSignBlockEntity;
import pers.neige.neigeitems.ref.chat.RefChatSerializer;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.chat.RefCraftChatMessage;
import pers.neige.neigeitems.ref.chat.RefEnumTitleAction;
import pers.neige.neigeitems.ref.entity.*;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;
import pers.neige.neigeitems.ref.network.*;
import pers.neige.neigeitems.ref.network.syncher.RefEntityDataAccessor;
import pers.neige.neigeitems.ref.network.syncher.RefSynchedEntityData;
import pers.neige.neigeitems.ref.network.syncher.RefSynchedEntityData$DataValue;
import pers.neige.neigeitems.ref.server.level.RefServerEntity;
import pers.neige.neigeitems.ref.server.level.RefWorldServer;
import pers.neige.neigeitems.ref.world.RefCraftWorld;
import pers.neige.neigeitems.ref.world.RefVec3;
import pers.neige.neigeitems.ref.world.inventory.RefCraftContainer;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EntityPlayerUtils {
    /**
     * 1.19.4+ 版本起, EntityMetadata数据包构造函数发生了改变.
     */
    static final boolean METADATA_NEED_VALUE_LIST = CbVersion.v1_19_R3.isSupport();
    /**
     * 1.13+ 版本起, Entity 类 setCustomName 方法由接收 String 改为接收 IChatBaseComponent.
     */
    static final boolean COMPONENT_NAME_SUPPORT = CbVersion.v1_13_R1.isSupport();
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
     * 1.17+ 版本起, SignBlockEntity 构造函数发生变化.
     */
    private static final boolean NEW_SIGN_ENTITY_CONSTRUCTOR = CbVersion.v1_17_R1.isSupport();
    /**
     * 1.20+ 版本起, 告示牌开始出现正反面区别.
     */
    private static final boolean SIGN_SIDE_SUPPORT = CbVersion.v1_20_R1.isSupport();
    /**
     * 1.17+ 版本起, Title 相关数据包发生变化.
     */
    private static final boolean TITLE_PACKET_CHANGED = CbVersion.v1_17_R1.isSupport();
    /**
     * 1.14+ 版本起, Entity 类添加 addEntityPacket 方法, 返回对应的添加实体数据包.
     */
    private static final boolean ADD_ENTITY_PACKET_SUPPORT = CbVersion.v1_14_R1.isSupport();
    /**
     * 1.19.4+ 版本起, SynchedEntityData 下 set 方法添加 boolean 类型 force 参数, 用于强制设置.
     */
    private static final boolean FORCE_DATA_SET = CbVersion.v1_19_R3.isSupport();
    /**
     * 1.14+ 版本起, WorldServer 类移除 tracker 字段.
     */
    private static final boolean REMOVED_TRACKER = CbVersion.v1_14_R1.isSupport();
    /**
     * 1.14+ 版本起, PacketPlayOutOpenWindow 数据包中的容器类型由 String 变更为 Containers(MenuType).
     */
    private static final boolean FUCKING_STRING_MENU_TYPE = CbVersion.v1_14_R1.isSupport();

    /**
     * 让指定玩家攻击指定实体.
     *
     * @param player 攻击者.
     * @param entity 防御者.
     */
    public static void attack(
            @NonNull Player player,
            @NonNull Entity entity
    ) {
        if (player instanceof RefCraftPlayer) {
            RefEntityPlayer attacker = ((RefCraftPlayer) player).getHandle();
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
            @NonNull HumanEntity entity
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
            @NonNull LivingEntity entity
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
     * 设置指定实体的攻击冷却.
     *
     * @param entity         待获取实体.
     * @param attackCooldown 攻击冷却.
     */
    public static void setAttackCooldown(
            @NonNull LivingEntity entity,
            float attackCooldown
    ) {
        if (entity instanceof RefCraftLivingEntity) {
            RefEntityLiving livingEntity = ((RefCraftLivingEntity) entity).getHandle();
            AttributeInstance attackSpeedAttribute = entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            double attackSpeed;
            if (attackSpeedAttribute != null) {
                attackSpeed = attackSpeedAttribute.getValue();
            } else {
                attackSpeed = 4;
            }
            livingEntity.attackStrengthTicker = Math.max(0, (int) (attackCooldown * (1.0 / attackSpeed * 20.0) - 0.5));
        }
    }

    /**
     * 获取指定实体距上次攻击行为有多久(tick).
     *
     * @param entity 待获取实体.
     */
    public static int getAttackStrengthTicker(
            @NonNull LivingEntity entity
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
     * @param entity               待获取实体.
     * @param attackStrengthTicker 实体距上次攻击行为的时间(tick).
     */
    public static void setAttackStrengthTicker(
            @NonNull LivingEntity entity,
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
     * @param hand   待使用物品槽位.
     */
    public static void useItem(
            @NonNull Player player,
            @NonNull EnumHand hand
    ) {
        if (player instanceof RefCraftPlayer) {
            RefEntityPlayer entityPlayer = ((RefCraftPlayer) player).getHandle();
            RefEnumHand enumHand = toRefEnumHand(hand);
            RefWorldServer world = ((RefCraftWorld) (Object) player.getWorld()).getHandle();
            RefNmsItemStack itemStack = entityPlayer.getItemInHand(enumHand);
            entityPlayer.playerInteractManager.useItem(entityPlayer, world, itemStack, enumHand);
        }
    }

    /**
     * 摆动指定玩家的指定手臂.
     *
     * @param player 待操作玩家.
     * @param hand   待摆动手臂.
     */
    public static void swing(
            @NonNull Player player,
            @NonNull EnumHand hand
    ) {
        swing(player, hand, true);
    }

    /**
     * 摆动指定玩家的指定手臂.
     *
     * @param player           待操作玩家.
     * @param hand             待摆动手臂.
     * @param fromServerPlayer 本人是否可见摆动动作.
     */
    public static void swing(
            @NonNull Player player,
            @NonNull EnumHand hand,
            boolean fromServerPlayer
    ) {
        if (player instanceof RefCraftPlayer) {
            RefEntityPlayer entityPlayer = ((RefCraftPlayer) player).getHandle();
            RefEnumHand enumHand = toRefEnumHand(hand);
            swingByNms(entityPlayer, enumHand, fromServerPlayer);
        }
    }

    /**
     * 给玩家播放指定动画(仅玩家本人可见).
     *
     * @param player 待操作玩家.
     * @param type   动画类型.
     */
    public static void sendAnimationPacket(
            @NonNull Player player,
            @NonNull AnimationType type
    ) {
        if (player instanceof RefCraftPlayer) {
            RefEntityPlayer entityPlayer = ((RefCraftPlayer) player).getHandle();
            entityPlayer.playerConnection.sendPacket(new RefPacketPlayOutAnimation(entityPlayer, type.getValue()));
        }
    }

    /**
     * 让指定玩家破坏对应位置的方块.
     *
     * @param player 待操作玩家.
     * @param block  待破坏方块.
     * @return 是否破坏成功
     */
    public static boolean breakBlock(
            @NonNull Player player,
            Block block
    ) {
        Preconditions.checkArgument(block != null, "Block cannot be null");
        Preconditions.checkArgument(block.getWorld().equals(player.getWorld()), "Cannot break blocks across worlds");
        return breakBlock(player, block.getX(), block.getY(), block.getZ());
    }

    /**
     * 让指定玩家破坏对应位置的方块.
     *
     * @param player 待操作玩家.
     * @param x      目标方块 x 坐标.
     * @param y      目标方块 y 坐标.
     * @param z      目标方块 z 坐标.
     * @return 是否破坏成功
     */
    public static boolean breakBlock(
            @NonNull Player player,
            int x,
            int y,
            int z
    ) {
        if (player instanceof RefCraftPlayer) {
            RefEntityPlayer entityPlayer = ((RefCraftPlayer) player).getHandle();
            return entityPlayer.playerInteractManager.breakBlock(new RefBlockPos(x, y, z));
        }
        return false;
    }

    /**
     * 获取玩家的伤害吸收数值.
     *
     * @param player 待获取玩家.
     */
    public static float getAbsorptionAmount(
            @NonNull Player player
    ) {
        if (player instanceof RefCraftPlayer) {
            return ((RefCraftPlayer) player).getHandle().getAbsorptionAmount();
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
            @NonNull Player player,
            float amount
    ) {
        if (player instanceof RefCraftPlayer) {
            ((RefCraftPlayer) player).getHandle().setAbsorptionAmount(amount);
        }
    }

    /**
     * 强制实体从地上跳起(浮空时不生效).
     *
     * @param humanEntity 待操作实体.
     */
    public static void jumpFromGround(
            @NonNull HumanEntity humanEntity
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
            @NonNull LivingEntity entity
    ) {
        if (entity.isOnGround() && entity instanceof RefCraftLivingEntity) {
            ((RefCraftLivingEntity) entity).getHandle().jumpFromGround();
        }
    }

    /**
     * 设置玩家朝向(仅适用于1.13+).
     *
     * @param player 待设置玩家.
     * @param yaw    实体偏航角.
     * @param pitch  实体俯仰角.
     */
    public static void setRotation(
            @NonNull Player player,
            float yaw,
            float pitch
    ) {
        if (SET_ROTATION_SUPPORT) {
            player.setRotation(yaw, pitch);
        } else {
            if (player instanceof RefCraftPlayer) {
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
            @NonNull Player player,
            @NonNull Location target
    ) {
        lookAt(player, target.getX(), target.getY(), target.getZ());
    }

    /**
     * 令玩家看向指定坐标(仅适用于1.13+).
     *
     * @param player 待操作玩家.
     * @param x      目标 x 坐标.
     * @param y      目标 y 坐标.
     * @param z      目标 z 坐标.
     */
    public static void lookAt(
            @NonNull Player player,
            double x,
            double y,
            double z
    ) {
        if (player instanceof RefCraftPlayer) {
            if (LOOK_AT_SUPPORT) {
                ((RefCraftPlayer) player).getHandle().lookAt(RefAnchor.EYES, new RefVec3(x, y, z));
            } else {
                lookAtByNms(player, x, y, z);
            }
        }
    }

    /**
     * 获取玩家对应的 Channel.
     *
     * @param player 待操作玩家.
     * @return 玩家对应的 Channel.
     */
    public static @Nullable Channel getChannel(
            @NonNull Player player
    ) {
        if (player instanceof RefCraftPlayer) {
            return ((RefCraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        }
        return null;
    }

    /**
     * 为玩家打开一个虚拟告示牌.
     *
     * @param player 待操作玩家.
     */
    public static void openSign(
            @NonNull Player player
    ) {
        if (player instanceof RefCraftPlayer) {
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();
            RefSignBlockEntity sign;
            if (NEW_SIGN_ENTITY_CONSTRUCTOR) {
                Location location = player.getLocation();
                sign = new RefSignBlockEntity(new RefBlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), null);
            } else {
                sign = new RefSignBlockEntity();
            }
            if (SIGN_SIDE_SUPPORT) {
                nmsPlayer.openSign(sign, true);
            } else {
                nmsPlayer.openSign(sign);
            }
        }
    }

    /**
     * 向玩家发送 BaseComponent 形式的 Title.
     *
     * @param player   待接收玩家.
     * @param title    Title.
     * @param subtitle Subtitle.
     */
    public static void sendTitle(
            @NonNull Player player,
            @Nullable BaseComponent title,
            @Nullable BaseComponent subtitle
    ) {
        sendTitle(player, title, subtitle, 10, 70, 20);
    }

    /**
     * 向玩家发送 BaseComponent 形式的 Title.
     *
     * @param player   待接收玩家.
     * @param title    Title.
     * @param subtitle Subtitle.
     * @param fadeIn   渐入时间(tick).
     * @param stay     停留时间(tick).
     * @param fadeOut  渐出时间(tick).
     */
    public static void sendTitle(
            @NonNull Player player,
            @Nullable BaseComponent title,
            @Nullable BaseComponent subtitle,
            int fadeIn,
            int stay,
            int fadeOut
    ) {
        sendTitle(player, ComponentSerializer.toString(title), ComponentSerializer.toString(subtitle), fadeIn, stay, fadeOut);
    }

    /**
     * 向玩家发送 BaseComponent 形式的 Title.
     *
     * @param player       待接收玩家.
     * @param jsonTitle    Title.
     * @param jsonSubtitle Subtitle.
     */
    public static void sendTitle(
            @NonNull Player player,
            @Nullable String jsonTitle,
            @Nullable String jsonSubtitle
    ) {
        sendTitle(player, jsonTitle, jsonSubtitle, 10, 70, 20);
    }

    /**
     * 向玩家发送 BaseComponent 形式的 Title.
     *
     * @param player       待接收玩家.
     * @param jsonTitle    Title.
     * @param jsonSubtitle Subtitle.
     * @param fadeIn       渐入时间(tick).
     * @param stay         停留时间(tick).
     * @param fadeOut      渐出时间(tick).
     */
    public static void sendTitle(
            @NonNull Player player,
            @Nullable String jsonTitle,
            @Nullable String jsonSubtitle,
            int fadeIn,
            int stay,
            int fadeOut
    ) {
        RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();
        if (TITLE_PACKET_CHANGED) {
            RefClientboundSetTitlesAnimationPacket times = new RefClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
            nmsPlayer.playerConnection.sendPacket(times);
            if (jsonTitle != null) {
                RefClientboundSetTitleTextPacket packetTitle = new RefClientboundSetTitleTextPacket(RefChatSerializer.fromJson(jsonTitle));
                nmsPlayer.playerConnection.sendPacket(packetTitle);
            }
            if (jsonSubtitle != null) {
                RefClientboundSetSubtitleTextPacket packetSubtitle = new RefClientboundSetSubtitleTextPacket(RefChatSerializer.fromJson(jsonSubtitle));
                nmsPlayer.playerConnection.sendPacket(packetSubtitle);
            }
        } else {
            RefPacketPlayOutTitle times = new RefPacketPlayOutTitle(fadeIn, stay, fadeOut);
            nmsPlayer.playerConnection.sendPacket(times);
            RefPacketPlayOutTitle packetSubtitle;
            if (jsonTitle != null) {
                packetSubtitle = new RefPacketPlayOutTitle(RefEnumTitleAction.TITLE, RefChatSerializer.fromJson(jsonTitle));
                nmsPlayer.playerConnection.sendPacket(packetSubtitle);
            }
            if (jsonSubtitle != null) {
                packetSubtitle = new RefPacketPlayOutTitle(RefEnumTitleAction.SUBTITLE, RefChatSerializer.fromJson(jsonSubtitle));
                nmsPlayer.playerConnection.sendPacket(packetSubtitle);
            }
        }
    }

    /**
     * 向玩家发送一个虚拟的实体数据包.
     *
     * @param location 待生成坐标.
     * @param type     实体类型.
     * @param player   待接收玩家.
     */
    public static Entity sendFakeEntity(
            @NonNull Location location,
            @NonNull EntityType type,
            @NonNull Player player
    ) {
        return sendFakeEntity(location, type, true, null, player);
    }

    /**
     * 向玩家发送一个虚拟的实体数据包.
     *
     * @param location      待生成坐标.
     * @param type          实体类型.
     * @param randomizeData 是否随机实体数据(仅在1.17+版本生效).
     * @param player        待接收玩家.
     */
    public static Entity sendFakeEntity(
            @NonNull Location location,
            @NonNull EntityType type,
            boolean randomizeData,
            @NonNull Player player
    ) {
        return sendFakeEntity(location, type, randomizeData, null, player);
    }

    /**
     * 向玩家发送一个虚拟的实体数据包.
     *
     * @param location      待生成坐标.
     * @param type          实体类型.
     * @param randomizeData 是否随机实体数据(仅在1.17+版本生效).
     * @param function      对实体执行的操作.
     * @param player        待接收玩家.
     * @return 生成的实体.
     */
    public static @Nullable Entity sendFakeEntity(
            @NonNull Location location,
            @NonNull EntityType type,
            boolean randomizeData,
            @Nullable Consumer<Entity> function,
            @NonNull Player player
    ) {
        RefEntity entity = WorldUtils.createNmsEntity(location, type, randomizeData);
        if (entity == null) return null;
        Entity bukkitEntity = entity.getBukkitEntity();

        if (player instanceof RefCraftPlayer) {
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            if (ADD_ENTITY_PACKET_SUPPORT) {
                nmsPlayer.playerConnection.sendPacket(entity.getAddEntityPacket());
            } else {
                RefServerEntity serverEntity = new RefServerEntity(entity, 0, 0, 0, false);
                nmsPlayer.playerConnection.sendPacket(serverEntity.createPacket());
            }
            if (function != null) {
                function.accept(bukkitEntity);
                refreshFakeEntity(bukkitEntity, player);
            }
        }
        return bukkitEntity;
    }

    /**
     * 向玩家发送一个虚拟的实体数据包.
     *
     * @param entity 待发送实体.
     * @param player 待接收玩家.
     * @return 生成的实体.
     */
    public static @NonNull Entity sendFakeEntity(
            @NonNull Entity entity,
            @NonNull Player player
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            if (ADD_ENTITY_PACKET_SUPPORT) {
                nmsPlayer.playerConnection.sendPacket(nmsEntity.getAddEntityPacket());
            } else {
                RefServerEntity serverEntity = new RefServerEntity(nmsEntity, 0, 0, 0, false);
                nmsPlayer.playerConnection.sendPacket(serverEntity.createPacket());
            }
        }
        return entity;
    }

    /**
     * 刷新虚拟实体数据.
     *
     * @param entity 待刷新实体.
     * @param player 待接收玩家.
     */
    public static void refreshFakeEntity(
            @NonNull Entity entity,
            @NonNull Player player
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntityMetadata packet;
            if (METADATA_NEED_VALUE_LIST) {
                RefSynchedEntityData entityData = nmsEntity.getEntityData();
                List<RefSynchedEntityData$DataValue> dataList = entityData.packDirty();
                if (dataList == null) {
                    dataList = entityData.getNonDefaultValues();
                }
                packet = new RefPacketPlayOutEntityMetadata(nmsEntity.getId(), dataList);
            } else {
                packet = new RefPacketPlayOutEntityMetadata(nmsEntity.getId(), nmsEntity.getEntityData(), true);
            }
            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 移除虚拟实体.
     *
     * @param entity 待移除实体.
     * @param player 待接收玩家.
     */
    public static void removeFakeEntity(
            @NonNull Entity entity,
            @NonNull Player player
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntityDestroy packet = new RefPacketPlayOutEntityDestroy(nmsEntity.getId());
            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 发送实体视角移动数据包.
     *
     * @param entity   待操作实体.
     * @param player   待接收玩家.
     * @param yaw      偏航角.
     * @param pitch    俯仰角.
     * @param onGround 实体在不在地上
     */
    public static void sendFakeEntityLookRaw(
            @NonNull Entity entity,
            @NonNull Player player,
            float yaw,
            float pitch,
            boolean onGround
    ) {
        byte yRot = (byte) ((yaw * 256.0f) / 360.0f);
        byte xRot = (byte) ((pitch * 256.0f) / 360.0f);
        sendFakeEntityLook(entity, player, yRot, xRot, onGround);
    }

    /**
     * 发送实体视角移动数据包.
     *
     * @param entity 待操作实体.
     * @param player 待接收玩家.
     * @param yRot   (byte) ((yaw*256.0)/360.0).
     * @param xRot   (byte) ((pitch*256.0)/360.0).
     */
    public static void sendFakeEntityLook(
            @NonNull Entity entity,
            @NonNull Player player,
            byte yRot,
            byte xRot,
            boolean onGround
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntity.RefPacketPlayOutEntityLook packet = new RefPacketPlayOutEntity.RefPacketPlayOutEntityLook(nmsEntity.getId(), yRot, xRot, onGround);
            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 发送实体位置相对移动数据包.
     *
     * @param entity   待操作实体.
     * @param player   待接收玩家.
     * @param relX     x轴相对移动量.
     * @param relY     y轴相对移动量.
     * @param relZ     z轴相对移动量.
     * @param onGround 实体在不在地上
     */
    public static void sendFakeEntityRelMoveRaw(
            @NonNull Entity entity,
            @NonNull Player player,
            double relX,
            double relY,
            double relZ,
            boolean onGround
    ) {
        double tempX = relX * 4096D;
        long intTempX = (int) tempX;
        long xa = tempX < ((double) intTempX) ? intTempX - 1 : intTempX;
        double tempY = relY * 4096D;
        long intTempY = (int) tempY;
        long ya = tempY < ((double) intTempY) ? intTempY - 1 : intTempY;
        double tempZ = relZ * 4096D;
        long intTempZ = (int) tempZ;
        long za = tempZ < ((double) intTempZ) ? intTempZ - 1 : intTempZ;
        sendFakeEntityRelMove(entity, player, xa, ya, za, onGround);
    }

    /**
     * 发送实体位置相对移动数据包.
     *
     * @param entity   待操作实体.
     * @param player   待接收玩家.
     * @param xa       (x方向移动量*4096)向下取整.
     * @param ya       (y方向移动量*4096)向下取整.
     * @param za       (z方向移动量*4096)向下取整.
     * @param onGround 实体在不在地上
     */
    public static void sendFakeEntityRelMove(
            @NonNull Entity entity,
            @NonNull Player player,
            long xa,
            long ya,
            long za,
            boolean onGround
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntity.RefPacketPlayOutRelEntityMove packet = new RefPacketPlayOutEntity.RefPacketPlayOutRelEntityMove(nmsEntity.getId(), xa, ya, za, onGround);
            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 发送实体位置相对移动及视角移动数据包.
     *
     * @param entity   待操作实体.
     * @param player   待接收玩家.
     * @param relX     x轴相对移动量.
     * @param relY     y轴相对移动量.
     * @param relZ     z轴相对移动量.
     * @param yaw      偏航角.
     * @param pitch    俯仰角.
     * @param onGround 实体在不在地上
     */
    public static void sendFakeEntityRelMoveLookRaw(
            @NonNull Entity entity,
            @NonNull Player player,
            double relX,
            double relY,
            double relZ,
            float yaw,
            float pitch,
            boolean onGround
    ) {
        double tempX = relX * 4096D;
        long intTempX = (int) tempX;
        long xa = tempX < ((double) intTempX) ? intTempX - 1 : intTempX;
        double tempY = relY * 4096D;
        long intTempY = (int) tempY;
        long ya = tempY < ((double) intTempY) ? intTempY - 1 : intTempY;
        double tempZ = relZ * 4096D;
        long intTempZ = (int) tempZ;
        long za = tempZ < ((double) intTempZ) ? intTempZ - 1 : intTempZ;
        byte yRot = (byte) ((yaw * 256.0f) / 360.0f);
        byte xRot = (byte) ((pitch * 256.0f) / 360.0f);
        sendFakeEntityRelMoveLook(entity, player, xa, ya, za, yRot, xRot, onGround);
    }

    /**
     * 发送实体位置相对移动及视角移动数据包.
     *
     * @param entity   待操作实体.
     * @param player   待接收玩家.
     * @param xa       (x方向移动量*4096)向下取整.
     * @param ya       (y方向移动量*4096)向下取整.
     * @param za       (z方向移动量*4096)向下取整.
     * @param yRot     (byte) ((yaw*256.0)/360.0).
     * @param xRot     (byte) ((pitch*256.0)/360.0).
     * @param onGround 实体在不在地上
     */
    public static void sendFakeEntityRelMoveLook(
            @NonNull Entity entity,
            @NonNull Player player,
            long xa,
            long ya,
            long za,
            byte yRot,
            byte xRot,
            boolean onGround
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntity.RefPacketPlayOutRelEntityMoveLook packet = new RefPacketPlayOutEntity.RefPacketPlayOutRelEntityMoveLook(nmsEntity.getId(), xa, ya, za, yRot, xRot, onGround);
            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 发送实体传送数据包.
     *
     * @param entity   待操作实体.
     * @param player   待接收玩家.
     * @param x        目标点x轴坐标.
     * @param y        目标点y轴坐标.
     * @param z        目标点z轴坐标.
     * @param yaw      偏航角.
     * @param pitch    俯仰角.
     * @param onGround 实体在不在地上
     */
    public static void sendFakeEntityTeleportRaw(
            @NonNull Entity entity,
            @NonNull Player player,
            double x,
            double y,
            double z,
            float yaw,
            float pitch,
            boolean onGround
    ) {
        byte yRot = (byte) ((yaw * 256.0f) / 360.0f);
        byte xRot = (byte) ((pitch * 256.0f) / 360.0f);
        sendFakeEntityTeleport(entity, player, x, y, z, yRot, xRot, onGround);
    }

    /**
     * 发送实体传送数据包.
     *
     * @param entity   待操作实体.
     * @param player   待接收玩家.
     * @param x        目标点x轴坐标.
     * @param y        目标点y轴坐标.
     * @param z        目标点z轴坐标.
     * @param yRot     (byte) ((yaw*256.0)/360.0).
     * @param xRot     (byte) ((pitch*256.0)/360.0).
     * @param onGround 实体在不在地上
     */
    public static void sendFakeEntityTeleport(
            @NonNull Entity entity,
            @NonNull Player player,
            double x,
            double y,
            double z,
            byte yRot,
            byte xRot,
            boolean onGround
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntityTeleport packet = new RefPacketPlayOutEntityTeleport();
            packet.entityId = nmsEntity.getId();
            packet.x = x;
            packet.y = y;
            packet.z = z;
            packet.yRot = yRot;
            packet.xRot = xRot;
            packet.onGround = onGround;
            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 发送实体头部转动数据包.
     *
     * @param entity 待操作实体.
     * @param player 待接收玩家.
     * @param yaw    偏航角.
     */
    public static void sendFakeEntityHeadRotationRaw(
            @NonNull Entity entity,
            @NonNull Player player,
            float yaw
    ) {
        sendFakeEntityHeadRotation(entity, player, (byte) ((yaw * 256.0) / 360.0));
    }

    /**
     * 发送实体头部转动数据包.
     *
     * @param entity   待操作实体.
     * @param player   待接收玩家.
     * @param yHeadRot (byte) ((yaw*256.0)/360.0).
     */
    public static void sendFakeEntityHeadRotation(
            @NonNull Entity entity,
            @NonNull Player player,
            byte yHeadRot
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntityHeadRotation packet = new RefPacketPlayOutEntityHeadRotation(nmsEntity, yHeadRot);
            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 刷新实体头部转动.
     *
     * @param entity 待操作实体.
     * @param player 待接收玩家.
     */
    public static void refreshFakeEntityHeadRotation(
            @NonNull Entity entity,
            @NonNull Player player
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntityHeadRotation packet = new RefPacketPlayOutEntityHeadRotation(nmsEntity, (byte) Math.floor((nmsEntity.getHeadRotation() * 256.0f) / 360.0f));
            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 移除实体虚拟显示名.
     *
     * @param entity 待设置实体.
     * @param player 待接收玩家.
     */
    public static void removeFakeCustomName(
            @NonNull Entity entity,
            @NonNull Player player
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntityMetadata packet;
            RefSynchedEntityData entityData = new RefSynchedEntityData(nmsEntity);
            defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_VISIBLE, entity.isCustomNameVisible());
            if (COMPONENT_NAME_SUPPORT) {
                defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_COMPONENT, Optional.of(nmsEntity.getCustomName()));
            } else {
                defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_STRING, entity.getCustomName());
            }
            if (METADATA_NEED_VALUE_LIST) {
                List<RefSynchedEntityData$DataValue> dataList = entityData.packDirty();
                if (dataList == null) {
                    dataList = entityData.getNonDefaultValues();
                }
                packet = new RefPacketPlayOutEntityMetadata(nmsEntity.getId(), dataList);
            } else {
                packet = new RefPacketPlayOutEntityMetadata(nmsEntity.getId(), entityData, true);
            }

            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 为实体设置虚拟显示名.
     *
     * @param entity 待设置实体.
     * @param player 待接收玩家.
     * @param name   显示名.
     */
    public static void setFakeCustomName(
            @NonNull Entity entity,
            @NonNull Player player,
            @NonNull String name
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntityMetadata packet;
            RefSynchedEntityData entityData = new RefSynchedEntityData(nmsEntity);
            defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_VISIBLE, true);
            if (COMPONENT_NAME_SUPPORT) {
                defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_COMPONENT, Optional.of(EntityUtils.toNms(new TextComponent(name))));
            } else {
                defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_STRING, name);
            }
            if (METADATA_NEED_VALUE_LIST) {
                List<RefSynchedEntityData$DataValue> dataList = entityData.packDirty();
                if (dataList == null) {
                    dataList = entityData.getNonDefaultValues();
                }
                packet = new RefPacketPlayOutEntityMetadata(nmsEntity.getId(), dataList);
            } else {
                packet = new RefPacketPlayOutEntityMetadata(nmsEntity.getId(), entityData, true);
            }

            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 为实体设置虚拟显示名.
     *
     * @param entity 待设置实体.
     * @param player 待接收玩家.
     * @param name   显示名.
     */
    public static void setFakeCustomName(
            @NonNull Entity entity,
            @NonNull Player player,
            @NonNull BaseComponent name
    ) {
        if (entity instanceof RefCraftEntity && player instanceof RefCraftPlayer) {
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();

            RefPacketPlayOutEntityMetadata packet;
            RefSynchedEntityData entityData = new RefSynchedEntityData(nmsEntity);
            defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_VISIBLE, true);
            if (COMPONENT_NAME_SUPPORT) {
                defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_COMPONENT, Optional.of(EntityUtils.toNms(name)));
            } else {
                defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_STRING, name.toLegacyText());
            }
            if (METADATA_NEED_VALUE_LIST) {
                List<RefSynchedEntityData$DataValue> dataList = entityData.packDirty();
                if (dataList == null) {
                    dataList = entityData.getNonDefaultValues();
                }
                packet = new RefPacketPlayOutEntityMetadata(nmsEntity.getId(), dataList);
            } else {
                packet = new RefPacketPlayOutEntityMetadata(nmsEntity.getId(), entityData, true);
            }

            nmsPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * 向玩家发送 NMS 数据包.
     *
     * @param player       待接收玩家.
     * @param packetObject 待发送的数据包(nms实例).
     */
    public static void sendPacket(
            @NonNull Player player,
            @NonNull Object packetObject
    ) {
        if (player instanceof RefCraftPlayer && packetObject instanceof RefPacket<?>) {
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();
            nmsPlayer.playerConnection.sendPacket((RefPacket<?>) packetObject);
        }
    }

    /**
     * 向玩家发送虚假的容器标题.
     *
     * @param player 待接收玩家.
     * @param title  待发送容器标题.
     */
    public static void sendFakeInventoryTitle(
            @NonNull Player player,
            @NonNull BaseComponent title
    ) {
        sendFakeInventoryTitleByNms(player, EntityUtils.toNms(title));
    }

    /**
     * 向玩家发送虚假的容器标题.
     *
     * @param player 待接收玩家.
     * @param title  待发送容器标题.
     */
    public static void sendFakeInventoryTitle(
            @NonNull Player player,
            @NonNull String title
    ) {
        sendFakeInventoryTitleByNms(player, RefCraftChatMessage.fromString(title)[0]);
    }

    /**
     * 向玩家发送虚假的JSON文本容器标题.
     *
     * @param player 待接收玩家.
     * @param title  JSON格式的待发送容器标题.
     */
    public static void sendFakeInventoryJsonTitle(
            @NonNull Player player,
            @NonNull String title
    ) {
        sendFakeInventoryTitleByNms(player, RefChatSerializer.fromJson(title));
    }

    /**
     * 获取玩家摄像机.
     *
     * @param player 待操作玩家.
     * @return 玩家摄像机
     */
    public static Entity getCamera(
            @NonNull Player player
    ) {
        if (player instanceof RefCraftPlayer) {
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();
            return nmsPlayer.getCamera().getBukkitEntity();
        }
        return player;
    }

    /**
     * 设置玩家摄像机.
     *
     * @param player 待操作玩家.
     * @param entity 待设置摄像机.
     */
    public static void setCamera(
            @NonNull Player player,
            @NonNull Entity entity
    ) {
        if (player instanceof RefCraftPlayer && entity instanceof RefCraftEntity) {
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();
            RefEntity nmsEntity = ((RefCraftEntity) entity).getHandle();
            nmsPlayer.setCamera(nmsEntity);
        }
    }

    /**
     * 获取玩家当前抛出的鱼钩实体.
     *
     * @param player 待操作玩家.
     * @return 玩家摄像机
     */
    public static @Nullable FishHook getFishHook(
            @NonNull Player player
    ) {
        if (!(player instanceof RefCraftPlayer)) {
            throw new IllegalArgumentException("player must be of type CraftPlayer");
        }
        RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();
        RefEntityFishingHook hookedFish = nmsPlayer.hookedFish;
        if (hookedFish == null) return null;
        return (FishHook) hookedFish.getBukkitEntity();
    }

    /**
     * 向玩家发送虚假的容器标题.
     *
     * @param player 待接收玩家.
     * @param title  待发送容器标题.
     */
    private static void sendFakeInventoryTitleByNms(
            @NonNull Player player,
            @NonNull RefComponent title
    ) {
        if (player instanceof RefCraftPlayer) {
            RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();
            RefEntityHuman nmsHuman = nmsPlayer;
            if (nmsHuman.containerMenu != nmsHuman.inventoryMenu) {
                RefPacketPlayOutOpenWindow packet;
                if (FUCKING_STRING_MENU_TYPE) {
                    packet = new RefPacketPlayOutOpenWindow(nmsHuman.containerMenu.containerId, nmsHuman.containerMenu.getType(), title);
                } else {
                    String windowType = RefCraftContainer.getNotchInventoryType0(player.getOpenInventory().getType());
                    int size = nmsHuman.containerMenu.getBukkitView().getTopInventory().getSize();
                    if (windowType.equals("minecraft:crafting_table") || windowType.equals("minecraft:anvil") || windowType.equals("minecraft:enchanting_table")) {
                        size = 0;
                    }
                    packet = new RefPacketPlayOutOpenWindow(nmsHuman.containerMenu.containerId, windowType, title, size);
                }

                nmsPlayer.playerConnection.sendPacket(packet);
                player.updateInventory();
            }
        }
    }

    static <T> void defineAndForceSet(
            @NonNull RefSynchedEntityData entityData,
            @NonNull RefEntityDataAccessor<T> key,
            @NonNull T value
    ) {
        entityData.define(key, value);
        if (FORCE_DATA_SET) {
            entityData.set(key, value, true);
        } else {
            entityData.set(key, value);
        }
    }

    private static void lookAtByNms(
            @NonNull Player player,
            double x,
            double y,
            double z
    ) {
        RefEntityPlayer nmsPlayer = ((RefCraftPlayer) player).getHandle();
        if (PACKET_LOOK_AT_SUPPORT) {
            EntityUtils.lookAtByNms(player, x, y, z);
            nmsPlayer.playerConnection.sendPacket(new RefPacketPlayOutLookAt(RefAnchor.EYES, x, y, z));
        } else {
            player.teleport(player.getLocation().setDirection(player.getEyeLocation().multiply(-1).add(x, y, z).toVector()));
        }
    }

    private static void swingByNms(
            @NonNull RefEntityPlayer player,
            @NonNull RefEnumHand hand
    ) {
        swingByNms(player, hand, true);
    }

    private static void swingByNms(
            @NonNull RefEntityPlayer player,
            @NonNull RefEnumHand hand,
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

    private static @NonNull RefEnumHand toRefEnumHand(
            @NonNull EnumHand hand
    ) {
        if (hand == EnumHand.MAIN_HAND) {
            return RefEnumHand.MAIN_HAND;
        } else {
            return RefEnumHand.OFF_HAND;
        }
    }
}
