package pers.neige.neigeitems.hook.mythicmobs

import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.ProxyListener

abstract class MythicMobsHooker {
    // MM怪物生成事件监听器, 一般不用操作, 放着就行
    abstract val spawnListener: ProxyListener

    // MM怪物死亡事件监听器, 一般不用操作, 放着就行
    abstract val deathListener: ProxyListener

    // 获取MM物品
    abstract fun getItemStack(id: String): ItemStack?

    // 同步获取MM物品(在5.1.0左右的版本中, MM物品的获取强制同步)
    // 不一定真的同步获取, 只在必要时同步(指高版本)
    abstract fun getItemStackSync(id: String): ItemStack?

    // 释放MM技能
    abstract fun castSkill(entity: Entity, skill: String)

    // 获取所有MM物品ID
    abstract fun getItemIds(): List<String>
}