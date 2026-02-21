package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.spawner;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.block.RefBlockPos;
import pers.neige.neigeitems.ref.block.spawner.RefCraftCreatureSpawner;
import pers.neige.neigeitems.ref.block.spawner.RefSpawnerBlockEntity;
import pers.neige.neigeitems.utils.ItemUtils;

import java.util.Objects;

public class SpawnerBuilder {
    /**
     * 1.17+ 版本起, CraftCreatureSpawner 构造函数发生变化.
     */
    private static final boolean NEW_CONSTRUCTOR = CbVersion.v1_17_R1.isSupport();
    /**
     * 刷怪笼对应的 Material
     * 1.12.2, Material.MOB_SPAWNER.
     * 1.13+, Material.SPAWNER.
     */
    private static final @NonNull Material MATERIAL_SPAWNER;
//    private static Field snapshot = null;

    static {
        // 确定刷怪笼对应的 Material
        if (CbVersion.v1_13_R1.isSupport()) {
            MATERIAL_SPAWNER = Material.valueOf("SPAWNER");
        } else {
            MATERIAL_SPAWNER = Material.valueOf("MOB_SPAWNER");
        }
//        if (NEW_CONSTRUCTOR) {
//            try {
//                snapshot = RefCraftBlockEntityState.class.getDeclaredField("snapshot");
//                snapshot.setAccessible(true);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 构建刷怪笼的 ItemStack.
     */
    private final ItemStack itemStack;
    /**
     * ItemStack 内部的 ItemMeta 原本.
     */
    private final @NonNull BlockStateMeta itemMeta;
    /**
     * 用于构建 ItemMeta 的 CreatureSpawner.
     */
    private final @NonNull CreatureSpawner blockState;

    public SpawnerBuilder() {
        itemStack = new ItemStack(MATERIAL_SPAWNER);
        itemMeta = (BlockStateMeta) Objects.requireNonNull(itemStack.getItemMeta());
        // 构建对应的 CreatureSpawner
        if (NEW_CONSTRUCTOR) {
            blockState = (CreatureSpawner) (Object) new RefCraftCreatureSpawner((World) null, new RefSpawnerBlockEntity(RefBlockPos.ZERO, null));
        } else {
            blockState = (CreatureSpawner) (Object) new RefCraftCreatureSpawner(MATERIAL_SPAWNER, new RefSpawnerBlockEntity());
        }
    }

    public SpawnerBuilder(ItemStack spawner) {
        if (spawner.getType() != MATERIAL_SPAWNER) {
            throw new IllegalArgumentException("itemStack type must be " + MATERIAL_SPAWNER);
        }
        itemStack = ItemUtils.copy(spawner);
        itemMeta = (BlockStateMeta) Objects.requireNonNull(itemStack.getItemMeta());
        // 构建对应的 CreatureSpawner
        if (itemMeta.hasBlockState()) {
            blockState = (CreatureSpawner) itemMeta.getBlockState();
        } else {
            if (NEW_CONSTRUCTOR) {
                blockState = (CreatureSpawner) (Object) new RefCraftCreatureSpawner((World) null, new RefSpawnerBlockEntity(RefBlockPos.ZERO, null));
            } else {
                blockState = (CreatureSpawner) (Object) new RefCraftCreatureSpawner(MATERIAL_SPAWNER, new RefSpawnerBlockEntity());
            }
        }
    }

    /**
     * 获取刷怪笼生成实体类型.
     *
     * @return 刷怪笼生成实体类型.
     */
    public @NonNull EntityType getSpawnedType() {
        return blockState.getSpawnedType();
    }

    /**
     * 设置刷怪笼生成实体类型.
     *
     * @param creatureType 刷怪笼生成实体类型.
     * @return SpawnerBuilder 本身.
     */
    public SpawnerBuilder setSpawnedType(
        @NonNull EntityType creatureType
    ) {
        blockState.setSpawnedType(creatureType);
        return this;
    }

    /**
     * 通过 minecraft 实体ID 设置刷怪笼生成实体类型.
     *
     * @param creatureType minecraft 实体ID.
     * @return SpawnerBuilder 本身.
     */
    public SpawnerBuilder setCreatureTypeByName(
        @NonNull String creatureType
    ) {
        blockState.setCreatureTypeByName(creatureType);
        return this;
    }

    /**
     * 获取 minecraft 实体ID 形式的刷怪笼生成实体类型.
     *
     * @return minecraft 实体ID 形式的刷怪笼生成实体类型.
     */
    public @NonNull String getCreatureTypeName() {
        return blockState.getCreatureTypeName();
    }

    /**
     * 获取最小生成间隔(tick), 该数值必须大于等于0, 默认为 200 tick.
     *
     * @return 最小生成间隔(tick).
     */
    public int getMinSpawnDelay() {
        return blockState.getMinSpawnDelay();
    }

    /**
     * 设置最小生成间隔(tick), 该数值必须大于等于0, 默认为 200 tick.
     *
     * @param delay 最小生成间隔(tick).
     * @return SpawnerBuilder 本身.
     */
    public SpawnerBuilder setMinSpawnDelay(
        int delay
    ) {
        blockState.setMinSpawnDelay(Math.max(delay, 0));
        return this;
    }

    /**
     * 获取最大生成间隔(tick), 该数值必须大于等于0, 同时大于等于最小生成间隔, 默认为 800 tick.
     *
     * @return 最大生成间隔(tick).
     */
    public int getMaxSpawnDelay() {
        return blockState.getMaxSpawnDelay();
    }

    /**
     * 设置最大生成间隔(tick), 该数值必须大于等于0, 同时大于等于最小生成间隔, 默认为 800 tick.
     *
     * @param delay 最大生成间隔(tick).
     * @return SpawnerBuilder 本身.
     */
    public SpawnerBuilder setMaxSpawnDelay(
        int delay
    ) {
        blockState.setMaxSpawnDelay(Math.max(delay, blockState.getMinSpawnDelay()));
        return this;
    }

    /**
     * 获取每次尝试生成的最大怪物数量(个), 默认为 4 个.
     *
     * @return 每次尝试生成的最大怪物数量(个).
     */
    public int getSpawnCount() {
        return blockState.getSpawnCount();
    }

    /**
     * 设置每次尝试生成的最大怪物数量(个), 默认为 4 个.
     *
     * @param spawnCount 每次尝试生成的最大怪物数量(个).
     * @return SpawnerBuilder 本身.
     */
    public SpawnerBuilder setSpawnCount(
        int spawnCount
    ) {
        blockState.setSpawnCount(spawnCount);
        return this;
    }

    /**
     * 获取周围存在的同类实体上限(个), 同类实体数量超限则不进行刷怪活动, 默认为 6 个.
     *
     * @return 周围存在的同类实体上限(个).
     */
    public int getMaxNearbyEntities() {
        return blockState.getMaxNearbyEntities();
    }

    /**
     * 设置周围存在的同类实体上限(个), 同类实体数量超限则不进行刷怪活动, 默认为 6 个.
     *
     * @param maxNearbyEntities 周围存在的同类实体上限(个).
     * @return SpawnerBuilder 本身.
     */
    public SpawnerBuilder setMaxNearbyEntities(
        int maxNearbyEntities
    ) {
        blockState.setMaxNearbyEntities(maxNearbyEntities);
        return this;
    }

    /**
     * 获取玩家最远距离(格), 距离内没有玩家则不进行刷怪活动, 默认为 16 格.
     * 数值小于等于 0 时, 刷怪笼将持续刷怪.
     *
     * @return 玩家最远距离(格).
     */
    public int getRequiredPlayerRange() {
        return blockState.getRequiredPlayerRange();
    }

    /**
     * 玩家最远距离(格), 距离内没有玩家则不进行刷怪活动, 默认为 16 格.
     * 数值小于等于 0 时, 刷怪笼将持续刷怪.
     *
     * @param requiredPlayerRange 玩家最远距离(格).
     * @return SpawnerBuilder 本身.
     */
    public SpawnerBuilder setRequiredPlayerRange(
        int requiredPlayerRange
    ) {
        blockState.setRequiredPlayerRange(requiredPlayerRange);
        return this;
    }

    /**
     * 获取刷怪笼刷怪半径(格), 默认为 4 格.
     * 可刷怪的 Y 轴高度恒定为 3, 包括: 刷怪笼本身的高度, 向上一格, 以及向下一格.
     *
     * @return 刷怪笼刷怪半径(格).
     */
    public int getSpawnRange() {
        return blockState.getSpawnRange();
    }

    /**
     * 设置刷怪笼刷怪半径(格), 默认为 4 格.
     * 可刷怪的 Y 轴高度恒定为 3, 包括: 刷怪笼本身的高度, 向上一格, 以及向下一格.
     *
     * @param spawnRange 刷怪笼刷怪半径(格).
     * @return SpawnerBuilder 本身.
     */
    public SpawnerBuilder setSpawnRange(
        int spawnRange
    ) {
        blockState.setSpawnRange(spawnRange);
        return this;
    }

//    /**
//     * 添加待生成生物.
//     *
//     * @param entity 待生成生物.
//     * @param weight 生成权重.
//     * @return SpawnerBuilder 本身.
//     */
//    public SpawnerBuilder addSpawnData(
//            @NonNull Entity entity,
//            int weight
//    ) {
//        addSpawnData(EntityUtils.save(entity), weight);
//        return this;
//    }
//
//    /**
//     * 添加待生成生物.
//     *
//     * @param nbt 待生成生物的 NBT.
//     * @param weight 生成权重.
//     * @return SpawnerBuilder 本身.
//     */
//    public SpawnerBuilder addSpawnData(
//            @NonNull NbtCompound nbt,
//            int weight
//    ) {
//        if (NEW_CONSTRUCTOR) {
////            try {
////                RefBaseSpawner baseSpawner = ((RefSpawnerBlockEntity) snapshot.get(blockState)).getSpawner();
////                RefWeightedRandomList<RefWrapper<RefSpawnData>> spawnPotentials = baseSpawner.spawnPotentials;
////                ImmutableList.Builder<RefWrapper<RefSpawnData>> builder = ImmutableList.builder();
////                spawnPotentials.unwrap().forEach((wrapper) -> {
////                    builder.add(RefWrapper.newInstance(wrapper.getData(), wrapper.getWeight()));
////                });
////                builder.add(RefWrapper.newInstance(new RefSpawnData(NeigeItemsUtils.toNms(nbt), Optional.empty()), RefWeight.of(weight)));
//////                builder.add(new RefWrapper<>(new RefSpawnData(NeigeItemsUtils.toNms(nbt), Optional.empty()), RefWeight.of(weight)));
////                baseSpawner.spawnPotentials = RefSimpleWeightedRandomList.newInstance(builder.build());
////            } catch (IllegalAccessException e) {
////                e.printStackTrace();
////            }
//        } else {
////            RefBaseSpawner baseSpawner = ((RefCraftCreatureSpawner) (Object) blockState).snapshot.getSpawner();
////            List<RefSpawnData> mobs = baseSpawner.mobs;
////            mobs.add(new RefSpawnData(weight, NeigeItemsUtils.toNms(nbt)));
//        }
//        return this;
//    }

    /**
     * 构建生成刷怪笼物品.
     *
     * @return 刷怪笼物品.
     */
    public ItemStack build() {
        itemMeta.setBlockState(blockState);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
