package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.manager.HookerManager;
import pers.neige.neigeitems.ref.entity.RefCraftEntity;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.entity.RefEntityItem;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;
import pers.neige.neigeitems.ref.network.*;
import pers.neige.neigeitems.ref.network.syncher.RefSynchedEntityData;
import pers.neige.neigeitems.ref.network.syncher.RefSynchedEntityData$DataItem;
import pers.neige.neigeitems.ref.network.syncher.RefSynchedEntityData$DataValue;
import pers.neige.neigeitems.ref.scores.RefCraftTeam;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class PacketUtils {
    public static final @NonNull String SET_SLOT = RefPacketPlayOutSetSlot.class.getSimpleName();
    public static final @NonNull String WINDOW_ITEMS = RefPacketPlayOutWindowItems.class.getSimpleName();
    public static final @NonNull ThreadSafeLazy<String> CURSOR_ITEM = new ThreadSafeLazy<>(() -> {
        if (CbVersion.v1_21_R2.isSupport()) {
            return RefClientboundSetCursorItemPacket.class.getSimpleName();
        } else {
            return null;
        }
    });
    public static final @NonNull String ENTITY_METADATA = RefPacketPlayOutEntityMetadata.class.getSimpleName();
    public static final @NonNull String SPAWN_ENTITY = RefPacketPlayOutSpawnEntity.class.getSimpleName();
    /**
     * 1.21.3+ 版本起, 添加 ClientboundSetCursorItemPacket 数据包, 代表指针物品.
     */
    public static final boolean ADD_CURSOR_ITEM_PACKET = CbVersion.v1_21_R2.isSupport();
    /**
     * 1.17+ 版本起, PacketPlayOutWindowItems 内部添加一个 carriedItem 字段, 用于存储指针上的物品.
     */
    private static final boolean CARRIED_ITEM_SUPPORT = CbVersion.v1_17_R1.isSupport();
    /**
     * 1.17+ 版本起, PacketPlayOutScoreboardTeam 颜色存储于一个 Optional<Parameter> 之中.
     */
    private static final boolean PARAMETER_TEAM_PACKET = CbVersion.v1_17_R1.isSupport();

    /**
     * 获取 PacketPlayOutEntityMetadata 数据包中的 id 字段, 意为实体 id, 无法获取时返回 -1.
     *
     * @param packetObject 待操作的数据包(nms实例).
     * @return 数据包中的 id 字段, 意为实体 id, 无法获取时返回 -1.
     */
    public static int getEntityIdFromPacketPlayOutEntityMetadata(@NonNull Object packetObject) {
        if (packetObject instanceof RefPacketPlayOutEntityMetadata) {
            RefPacketPlayOutEntityMetadata packet = (RefPacketPlayOutEntityMetadata) packetObject;
            return packet.id;
        }
        return -1;
    }

    /**
     * 获取 PacketPlayOutSpawnEntity 数据包中的 id 字段, 意为实体 id, 无法获取时返回 -1.
     *
     * @param packetObject 待操作的数据包(nms实例).
     * @return 数据包中的 id 字段, 意为实体 id, 无法获取时返回 -1.
     */
    public static int getEntityIdFromPacketPlayOutSpawnEntity(@NonNull Object packetObject) {
        if (packetObject instanceof RefPacketPlayOutSpawnEntity) {
            RefPacketPlayOutSpawnEntity packet = (RefPacketPlayOutSpawnEntity) packetObject;
            return packet.id;
        }
        return -1;
    }

    /**
     * 为实体设置虚拟显示名.
     *
     * @param world        所处世界.
     * @param packetObject 待修改的数据包(nms实例).
     * @param name         显示名.
     */
    public static void setFakeCustomName(
            @NonNull World world,
            @NonNull Object packetObject,
            @NonNull BaseComponent name
    ) {
        if (packetObject instanceof RefPacketPlayOutEntityMetadata) {
            RefPacketPlayOutEntityMetadata packet = (RefPacketPlayOutEntityMetadata) packetObject;

            int entityId = packet.id;
            Entity bukkitEntity = HookerManager.INSTANCE.getNmsHooker().getEntityFromID1(world, entityId);
            if (!(bukkitEntity instanceof RefCraftEntity)) return;
            RefEntity entity = ((RefCraftEntity) bukkitEntity).getHandle();

            RefSynchedEntityData entityData = new RefSynchedEntityData(entity);
            if (EntityPlayerUtils.COMPONENT_NAME_SUPPORT) {
                EntityPlayerUtils.defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_COMPONENT, Optional.of(EntityUtils.toNms(name)));
            } else {
                EntityPlayerUtils.defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_STRING, name.toLegacyText());
            }
            if (EntityPlayerUtils.METADATA_NEED_VALUE_LIST) {
                boolean setCustomVisible = true;
                List<RefSynchedEntityData$DataValue> result = new CopyOnWriteArrayList<>();
                for (RefSynchedEntityData$DataValue next : packet.packedItems1) {
                    if (next.id() == RefEntity.DATA_CUSTOM_NAME_VISIBLE.getId()) {
                        setCustomVisible = false;
                        result.add(next);
                    } else {
                        if (EntityPlayerUtils.COMPONENT_NAME_SUPPORT) {
                            if (next.id() != RefEntity.DATA_CUSTOM_NAME_COMPONENT.getId()) {
                                result.add(next);
                            }
                        } else {
                            if (next.id() != RefEntity.DATA_CUSTOM_NAME_STRING.getId()) {
                                result.add(next);
                            }
                        }
                    }
                }
                if (setCustomVisible) {
                    EntityPlayerUtils.defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_VISIBLE, true);
                }
                List<RefSynchedEntityData$DataValue> dataList = entityData.packDirty();
                if (dataList == null) {
                    dataList = entityData.getNonDefaultValues();
                }
                result.addAll(new RefPacketPlayOutEntityMetadata(entityId, dataList).packedItems1);
                packet.packedItems1 = result;
            } else {
                boolean setCustomVisible = true;
                List<RefSynchedEntityData$DataItem> result = new CopyOnWriteArrayList<>();
                for (RefSynchedEntityData$DataItem next : packet.packedItems0) {
                    if (next.getAccessor() == RefEntity.DATA_CUSTOM_NAME_VISIBLE) {
                        setCustomVisible = false;
                        result.add(next);
                    } else {
                        if (EntityPlayerUtils.COMPONENT_NAME_SUPPORT) {
                            if (next.getAccessor() != RefEntity.DATA_CUSTOM_NAME_COMPONENT) {
                                result.add(next);
                            }
                        } else {
                            if (next.getAccessor() != RefEntity.DATA_CUSTOM_NAME_STRING) {
                                result.add(next);
                            }
                        }
                    }
                }
                if (setCustomVisible) {
                    EntityPlayerUtils.defineAndForceSet(entityData, RefEntity.DATA_CUSTOM_NAME_VISIBLE, true);
                }
                result.addAll(new RefPacketPlayOutEntityMetadata(entityId, entityData, true).packedItems0);
                packet.packedItems0 = result;
            }
        }
    }

    /**
     * 为掉落物实体设置虚拟物品.
     *
     * @param world        所处世界.
     * @param packetObject 待修改的数据包(nms实例).
     * @param itemStack    实体对应的物品.
     */
    public static void setFakeItem(
            @NonNull World world,
            @NonNull Object packetObject,
            @NonNull ItemStack itemStack
    ) {
        RefNmsItemStack nmsItemStack;
        if (itemStack instanceof RefCraftItemStack) {
            nmsItemStack = ((RefCraftItemStack) itemStack).handle;
        } else {
            nmsItemStack = RefCraftItemStack.asNMSCopy(itemStack);
        }
        if (packetObject instanceof RefPacketPlayOutEntityMetadata) {
            RefPacketPlayOutEntityMetadata packet = (RefPacketPlayOutEntityMetadata) packetObject;

            int entityId = packet.id;
            Entity bukkitEntity = HookerManager.INSTANCE.getNmsHooker().getEntityFromID1(world, entityId);
            if (!(bukkitEntity instanceof RefCraftEntity)) return;
            RefEntity entity = ((RefCraftEntity) bukkitEntity).getHandle();
            if (!(entity instanceof RefEntityItem)) return;

            RefSynchedEntityData entityData = new RefSynchedEntityData(entity);
            EntityPlayerUtils.defineAndForceSet(entityData, RefEntityItem.DATA_ITEM, nmsItemStack);
            if (EntityPlayerUtils.METADATA_NEED_VALUE_LIST) {
                List<RefSynchedEntityData$DataValue> result = new CopyOnWriteArrayList<>(packet.packedItems1);
                List<RefSynchedEntityData$DataValue> dataList = entityData.packDirty();
                if (dataList == null) {
                    dataList = entityData.getNonDefaultValues();
                }
                result.addAll(new RefPacketPlayOutEntityMetadata(entityId, dataList).packedItems1);
                packet.packedItems1 = result;
            } else {
                List<RefSynchedEntityData$DataItem> result = new CopyOnWriteArrayList<>(packet.packedItems0);
                result.addAll(new RefPacketPlayOutEntityMetadata(entityId, entityData, true).packedItems0);
                packet.packedItems0 = result;
            }
        }
    }

    /**
     * 获取 PacketPlayOutSetSlot 数据包中的 itemStack 字段, 包装为 CraftItemStack 并返回.
     *
     * @param packetObject 待操作的数据包(nms实例).
     * @return 经过包装的数据包中的 itemStack 字段, 传入的不是 PacketPlayOutSetSlot 数据包时返回 null.
     */
    public static @Nullable ItemStack getItemStackFromPacketPlayOutSetSlot(@NonNull Object packetObject) {
        if (packetObject instanceof RefPacketPlayOutSetSlot) {
            RefPacketPlayOutSetSlot packet = (RefPacketPlayOutSetSlot) packetObject;
            return RefCraftItemStack.asCraftMirror(packet.itemStack);
        }
        return null;
    }

    /**
     * 获取 ClientboundSetCursorItemPacket 数据包中的 contents 字段, 包装为 CraftItemStack 并返回.
     *
     * @param packetObject 待操作的数据包(nms实例).
     * @return 经过包装的数据包中的 contents 字段, 传入的不是 ClientboundSetCursorItemPacket 数据包时返回 null.
     */
    public static @Nullable ItemStack getContentsFromClientboundSetCursorItemPacket(@NonNull Object packetObject) {
        if (packetObject instanceof RefClientboundSetCursorItemPacket) {
            RefClientboundSetCursorItemPacket packet = (RefClientboundSetCursorItemPacket) packetObject;
            return RefCraftItemStack.asCraftMirror(packet.contents());
        }
        return null;
    }

    /**
     * 获取 PacketPlayOutWindowItems 数据包中的 items 字段, 将列表内的对象逐个包装为 CraftItemStack 后返回.
     * 不要尝试修改这个 List, 因为这个 List 并非数据包内的 List.
     *
     * @param packetObject 待操作的数据包(nms实例).
     * @return 经过包装的数据包中的 items 字段, 传入的不是 PacketPlayOutSetSlot 数据包时返回 null.
     */
    public static @Nullable List<ItemStack> getItemsFromPacketPlayOutWindowItems(@NonNull Object packetObject) {
        if (packetObject instanceof RefPacketPlayOutWindowItems) {
            RefPacketPlayOutWindowItems packet = (RefPacketPlayOutWindowItems) packetObject;
            return packet.items.stream().map(RefCraftItemStack::asCraftMirror).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取 PacketPlayOutWindowItems 数据包中的 carriedItem 字段, 包装为 CraftItemStack 并返回.
     *
     * @param packetObject 待操作的数据包(nms实例).
     * @return 经过包装的数据包中的 carriedItem 字段, 传入的不是 PacketPlayOutWindowItems 数据包或版本不足 1.17 时返回 null.
     */
    public static @Nullable ItemStack getCarriedItemFromPacketPlayOutWindowItems(@NonNull Object packetObject) {
        if (CARRIED_ITEM_SUPPORT && packetObject instanceof RefPacketPlayOutWindowItems) {
            RefPacketPlayOutWindowItems packet = (RefPacketPlayOutWindowItems) packetObject;
            return RefCraftItemStack.asCraftMirror(packet.carriedItem);
        }
        return null;
    }

    public static @NonNull Object newScoreboardTeamPacket(@NonNull Team team, UUID item) {
        RefPacketPlayOutScoreboardTeam packet;
        if (PARAMETER_TEAM_PACKET) {
            packet = RefPacketPlayOutScoreboardTeam.createAddOrModifyPacket(((RefCraftTeam) team).team, false);
            packet.method = RefPacketPlayOutScoreboardTeam.METHOD_JOIN;
            packet.entities = Collections.singletonList(item.toString());
        } else {
            packet = new RefPacketPlayOutScoreboardTeam();
            packet.name = team.getName();
            packet.method = RefPacketPlayOutScoreboardTeam.METHOD_JOIN;
            packet.entities.add(item.toString());
        }
        return packet;
    }
}
