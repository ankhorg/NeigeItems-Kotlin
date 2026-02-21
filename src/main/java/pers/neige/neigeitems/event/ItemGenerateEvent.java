package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.config.ConfigReader;

import java.util.Map;

/**
 * 物品生成事件.
 *
 * @property id 物品ID
 * @property player 用于解析物品的玩家
 * @property itemStack 生成的物品
 * @property cache 节点缓存
 * @property configSection 物品配置(经过了节点解析, 不包含节点配置)
 * @property sections 节点配置
 */
public final class ItemGenerateEvent extends BasicEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NonNull String id;
    private final @Nullable OfflinePlayer player;
    private final @NonNull Map<String, String> cache;
    private final @NonNull ConfigReader configSection;
    private final @Nullable ConfigurationSection sections;
    private @NonNull ItemStack itemStack;

    /**
     * @param id            物品ID
     * @param player        用于解析物品的玩家
     * @param itemStack     生成的物品
     * @param cache         节点缓存
     * @param configSection 物品配置(经过了节点解析, 不包含节点配置)
     * @param sections      节点配置
     */
    public ItemGenerateEvent(
        @NonNull String id,
        @Nullable OfflinePlayer player,
        @NonNull ItemStack itemStack,
        @NonNull Map<String, String> cache,
        @NonNull ConfigReader configSection,
        @Nullable ConfigurationSection sections
    ) {
        this.id = id;
        this.player = player;
        this.itemStack = itemStack;
        this.cache = cache;
        this.configSection = configSection;
        this.sections = sections;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取物品ID
     */
    public @NonNull String getId() {
        return id;
    }

    /**
     * 获取用于解析物品的玩家
     */
    public @Nullable OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * 获取生成的物品
     */
    public @NonNull ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 设置生成的物品
     *
     * @param itemStack 生成的物品
     */
    public void setItemStack(
        @NonNull ItemStack itemStack
    ) {
        this.itemStack = itemStack;
    }

    /**
     * 获取节点缓存
     */
    public @NonNull Map<String, String> getCache() {
        return cache;
    }

    /**
     * 获取物品配置(经过了节点解析, 不包含节点配置)
     */
    public @NonNull ConfigReader getConfigSection() {
        return configSection;
    }

    /**
     * 获取节点配置
     */
    public @Nullable ConfigurationSection getSections() {
        return sections;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}