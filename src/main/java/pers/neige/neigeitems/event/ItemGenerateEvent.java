package pers.neige.neigeitems.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
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
    @NotNull
    private final String id;
    @Nullable
    private final OfflinePlayer player;
    @NotNull
    private final Map<String, String> cache;
    @NotNull
    private final ConfigReader configSection;
    @Nullable
    private final ConfigurationSection sections;
    @NotNull
    private ItemStack itemStack;

    /**
     * @param id            物品ID
     * @param player        用于解析物品的玩家
     * @param itemStack     生成的物品
     * @param cache         节点缓存
     * @param configSection 物品配置(经过了节点解析, 不包含节点配置)
     * @param sections      节点配置
     */
    public ItemGenerateEvent(
            @NotNull String id,
            @Nullable OfflinePlayer player,
            @NotNull ItemStack itemStack,
            @NotNull Map<String, String> cache,
            @NotNull ConfigReader configSection,
            @Nullable ConfigurationSection sections
    ) {
        this.id = id;
        this.player = player;
        this.itemStack = itemStack;
        this.cache = cache;
        this.configSection = configSection;
        this.sections = sections;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取物品ID
     */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * 获取用于解析物品的玩家
     */
    @Nullable
    public OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * 获取生成的物品
     */
    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 设置生成的物品
     *
     * @param itemStack 生成的物品
     */
    public void setItemStack(
            @NotNull ItemStack itemStack
    ) {
        this.itemStack = itemStack;
    }

    /**
     * 获取节点缓存
     */
    @NotNull
    public Map<String, String> getCache() {
        return cache;
    }

    /**
     * 获取物品配置(经过了节点解析, 不包含节点配置)
     */
    @NotNull
    public ConfigReader getConfigSection() {
        return configSection;
    }

    /**
     * 获取节点配置
     */
    @Nullable
    public ConfigurationSection getSections() {
        return sections;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}