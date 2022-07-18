package pers.neige.neigeitems.item

import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.getItemTag

class ItemAction(val id: String, val config: ConfigurationSection) {
    // 物品消耗信息
    val consume: ConfigurationSection? = config.getConfigurationSection("consume")

    // 物品使用冷却
    val cooldown = config.getInt("cooldown", 0)

    // 使用物品左键交互
    val left = config.get("left")

    // 使用物品右键交互
    val right = config.get("right")

    // 使用物品左键或右键交互
    val all = config.get("all")

    // 食用或饮用物品
    val eat = config.get("eat")

    // 丢弃物品
    val drop = config.get("drop")

    // 捡起物品
    val pick = config.get("pick")

    // 使用物品破坏方块
    val destroy = config.get("destroy")

    // 物品损坏
    val broken = config.get("broken")
}