const Calendar = Packages.java.util.Calendar
const ThreadLocalRandom = Packages.java.util.concurrent.ThreadLocalRandom

const Bukkit = Packages.org.bukkit.Bukkit
const ChatColor = Packages.org.bukkit.ChatColor
const EquipmentSlot = Packages.org.bukkit.inventory.EquipmentSlot
const GameMode = Packages.org.bukkit.GameMode
const Material = Packages.org.bukkit.Material
const ItemStack = Packages.org.bukkit.inventory.ItemStack

const ActionUtils = Packages.pers.neige.neigeitems.utils.ActionUtils
const ConfigUtils = Packages.pers.neige.neigeitems.utils.ConfigUtils
const FileUtils = Packages.pers.neige.neigeitems.utils.FileUtils
const ItemUtils = Packages.pers.neige.neigeitems.utils.ItemUtils
const JsonUtils = Packages.pers.neige.neigeitems.utils.JsonUtils
const LangUtils = Packages.pers.neige.neigeitems.utils.LangUtils
const PlayerUtils = Packages.pers.neige.neigeitems.utils.PlayerUtils
const SamplingUtils = Packages.pers.neige.neigeitems.utils.SamplingUtils
const ScriptUtils = Packages.pers.neige.neigeitems.utils.ScriptUtils
const SectionUtils = Packages.pers.neige.neigeitems.utils.SectionUtils
const StringUtils = Packages.pers.neige.neigeitems.utils.StringUtils

const NbtUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils
const ComponentUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.ComponentUtils
const DamageEventUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.DamageEventUtils
const EnchantmentUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EnchantmentUtils
const EntityUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityUtils
const EntityItemUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityItemUtils
const EntityPlayerUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils
const InventoryUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.InventoryUtils
const SpigotInventoryUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.SpigotInventoryUtils
const PaperInventoryUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.PaperInventoryUtils
const PacketUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.PacketUtils
const ServerUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.ServerUtils
const TranslationUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.TranslationUtils
const WorldUtils = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.WorldUtils
const CommandUtils = Packages.pers.neige.neigeitems.utils.CommandUtils
const ListenerUtils = Packages.pers.neige.neigeitems.utils.ListenerUtils
const ListUtils = Packages.pers.neige.neigeitems.utils.ListUtils
const SchedulerUtils = Packages.pers.neige.neigeitems.utils.SchedulerUtils
const UUIDUtils = Packages.pers.neige.neigeitems.utils.UUIDUtils

const ActionContext = Packages.pers.neige.neigeitems.action.ActionContext
const AnimationType = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.animation.AnimationType
const EnumHand = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.EnumHand
const NbtItemStack = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtItemStack
const SpawnerBuilder = Packages.pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.spawner.SpawnerBuilder
const SuccessResult = Packages.pers.neige.neigeitems.action.result.SuccessResult
const StopResult = Packages.pers.neige.neigeitems.action.result.StopResult
const DelayResult = Packages.pers.neige.neigeitems.action.result.DelayResult
const Results = Packages.pers.neige.neigeitems.action.result.Results

const ActionManager = Packages.pers.neige.neigeitems.manager.ActionManager.INSTANCE
const ConfigManager = Packages.pers.neige.neigeitems.manager.ConfigManager.INSTANCE
const ConfigSectionManager = Packages.pers.neige.neigeitems.manager.ConfigSectionManager
const HookerManager = Packages.pers.neige.neigeitems.manager.HookerManager
const ItemEditorManager = Packages.pers.neige.neigeitems.manager.ItemEditorManager.INSTANCE
const ItemManager = Packages.pers.neige.neigeitems.manager.ItemManager.INSTANCE
const ItemPackManager = Packages.pers.neige.neigeitems.manager.ItemPackManager.INSTANCE

const NeigeItems = Packages.pers.neige.neigeitems.NeigeItems

const bukkitServer = Bukkit.getServer()
const consoleSender = bukkitServer.getConsoleSender()
const pluginManager = Bukkit.getPluginManager()
const scheduler = Bukkit.getScheduler()
const plugin = pluginManager.getPlugin("NeigeItems")

/**
 * 判断玩家是否拥有某个权限节点
 *
 * @param perm String 权限节点
 * @return Boolean 玩家是否拥有该权限
 */
const perm = function (perm) {
    return player.hasPermission(perm)
}

/**
 * 替换文本中的颜色代码
 *
 * @param text String 待替换文本
 * @return String 替换后文本
 */
const color = function (text) {
    return ChatColor.translateAlternateColorCodes('&', text)
}

/**
 * 发送一段消息
 *
 * @param text String 待发送消息
 */
const tell = function (text) {
    player.sendMessage(text)
}

/**
 * 执行一段指令
 *
 * @param cmd String 待执行指令
 */
const command = function (cmd) {
    bukkitServer.dispatchCommand(player, cmd)
}

/**
 * 后台执行一段指令
 *
 * @param cmd String 待执行指令
 */
const console = function (cmd) {
    bukkitServer.dispatchCommand(consoleSender, cmd)
}

/**
 * 解析文本中的papi变量
 *
 * @param text String 待解析文本
 * @return String 解析后文本
 */
const papi = function (text) {
    return HookerManager.papi(player, text)
}

/**
 * 解析文本中的即时声明节点
 *
 * @param text String 待解析文本
 * @return String 解析后文本
 */
const parse = function (text) {
    const c = typeof cache === "undefined" ? null : cache
    const s = typeof sections === "undefined" ? null : sections
    return SectionUtils.parseSection(text, c, player, s)
}

/**
 * 解析文本中的物品节点
 *
 * @param text String 待解析文本
 * @return String 解析后文本
 */
const parseItem = function (text) {
    if (typeof itemTag != "undefined") {
        return SectionUtils.parseItemSection(text, itemStack, itemTag, data, player)
    } else {
        return "未传入物品"
    }
}

/**
 * 获取物品NBT
 *
 * @param key String NBT键
 * @return String NBT值转文本
 */
const getNBT = function (key) {
    const result = itemTag.getDeep(key)
    if (result != null) {
        return ItemUtils.toValue(result).toString()
    }
    return null
}

/**
 * 获取物品NBT
 *
 * @param key String NBT键
 * @return ItemTag NBT值
 */
const getNBTTag = function (key) {
    return itemTag.getDeep(key)
}

/**
 * 生成一个随机数(默认0-1)
 *
 * @param min Double 随机数最小值
 * @param max Double 随机数最大值
 * @return Double 随机数
 */
const random = function (min, max) {
    return ThreadLocalRandom.current().nextDouble(min || 0, max || 1)
}

/**
 * 概率返回true(默认概率0-1)
 * 例:
 * value = 0.5; limit = null, 50%返回true
 * value = 50; limit = 100, 50%返回true
 *
 * @param value Double 返回true的概率
 * @param limit Double 概率上限
 * @return Boolean 随机数
 */
const chance = function (value, limit) {
    return value > ThreadLocalRandom.current().nextDouble(0, limit || 1)
}

/**
 * 检测连击情况
 *
 * @param group String 连击组
 * @param types [] 连击类型
 * @return Boolean 是否达成连击
 */
const combo = function (group, types) {
    if (!PlayerUtils.hasMetadataEZ(player, "Combo-" + group)) {
        PlayerUtils.setMetadataEZ(player, "Combo-" + group, new java.util.ArrayList())
    }
    const comboInfos = PlayerUtils.getMetadataEZ(player, "Combo-" + group, "")
    if (comboInfos.size() < types.length) return false

    const difference = comboInfos.size() - types.length
    let result = true
    for (let index = 0; index < types.length; index++) {
        if (types[index] !== comboInfos[index + difference].type) result = false
    }

    return result
}

/**
 * 检测连击情况
 *
 * @param group String 连击组
 * @return Boolean 是否达成连击
 */
const comboSize = function (group) {
    if (!PlayerUtils.hasMetadataEZ(player, "Combo-" + group)) {
        PlayerUtils.setMetadataEZ(player, "Combo-" + group, new java.util.ArrayList())
    }
    const comboInfos = PlayerUtils.getMetadataEZ(player, "Combo-" + group, "")

    return comboInfos.size()
}

/**
 * 检测点击事件是否由主手触发
 *
 * @return Boolean 点击事件是否由主手触发
 */
const isMainHand = function () {
    return event.hand === EquipmentSlot.HAND
}

/**
 * 检测点击事件是否由副手触发
 *
 * @return Boolean 点击事件是否由副手触发
 */
const isOffHand = function () {
    return event.hand === EquipmentSlot.OFF_HAND
}

/**
 * 检测点击事件是否由副手触发
 *
 * @return Boolean 点击事件是否由副手触发
 */
const hand = function () {
    return event.hand.toString()
}

/**
 * 获取玩家背包中指定ID的NI物品的数量
 *
 * @param itemId String NI物品ID
 * @return Integer 玩家背包中对应NI物品的数量
 */
const niItemAmount = function (itemId) {
    const contents = player.inventory.contents
    let amount = 0
    for (let index = 0; index < contents.length; index++) {
        const itemStack = contents[index]
        const currentItemId = ItemUtils.getItemId(itemStack)
        if (currentItemId === itemId) {
            amount += itemStack.amount
        }
    }
    return amount
}

/**
 * 检查玩家背包中指定ID的NI物品是否达到指定数量
 *
 * @param itemId String NI物品ID
 * @param amount Integer 预期达到的物品数量
 * @return Boolean 玩家背包中是否包含足够数量的对应NI物品
 */
const checkNiItemAmount = function (itemId, amount) {
    const contents = player.inventory.contents
    for (let index = 0; index < contents.length; index++) {
        const itemStack = contents[index]
        const currentItemId = ItemUtils.getItemId(itemStack)
        if (currentItemId === itemId) {
            if (amount > itemStack.amount) {
                amount -= itemStack.amount
            } else {
                amount = 0
                break
            }
        }
    }
    return amount === 0
}

/**
 * 获取玩家背包中扣除指定数量的NI物品
 *
 * @param itemId String NI物品ID
 * @param amount Integer 需要扣除的数量
 * @return String 玩家背包中的物品数量是否大于等于需要扣除的数量
 */
const takeNiItem = function (itemId, amount) {
    const contents = player.inventory.contents
    for (let index = 0; index < contents.length; index++) {
        const itemStack = contents[index]
        const currentItemId = ItemUtils.getItemId(itemStack)
        if (currentItemId === itemId) {
            if (amount > itemStack.amount) {
                amount -= itemStack.amount
                itemStack.amount = 0
            } else {
                itemStack.amount -= amount
                amount = 0
                break
            }
        }
    }
    return amount === 0
}

/**
 * 同步执行一个函数
 *
 * @param func 待执行函数
 */
const sync = SchedulerUtils.sync

/**
 * 异步执行一个函数
 *
 * @param func 待执行函数
 */
const async = SchedulerUtils.async

/**
 * 获取玩家IP
 *
 * @return String 玩家IP地址
 */
const address = function () {
    return player.getAddress().getHostString()
}

/**
 * 判断/修改玩家是否允许飞行
 *
 * @param flight Boolean 是否允许飞行
 * @return Boolean 玩家是否允许飞行
 */
const allowFlight = function (flight) {
    if (flight == null) {
        return player.getAllowFlight()
    } else {
        player.setAllowFlight(flight)
        return flight
    }
}

/**
 * 获取玩家攻击冷却
 *
 * @return float 当前攻击冷却
 */
const attackCooldown = function () {
    return player.getAttackCooldown()
}

/**
 * 获取玩家重生点
 *
 * @return Location 玩家重生点
 */
const bedSpawn = function () {
    return player.getBedSpawnLocation()
}

/**
 * 获取玩家重生点X轴坐标
 *
 * @return double 玩家重生点X轴坐标
 */
const bedSpawnX = function () {
    return player.getBedSpawnLocation().getX()
}

/**
 * 获取玩家重生点Y轴坐标
 *
 * @return double 玩家重生点Y轴坐标
 */
const bedSpawnY = function () {
    return player.getBedSpawnLocation().getY()
}

/**
 * 获取玩家重生点Z轴坐标
 *
 * @return double 玩家重生点Z轴坐标
 */
const bedSpawnZ = function () {
    return player.getBedSpawnLocation().getZ()
}

/**
 * 获取玩家是否正在格挡
 *
 * @return Boolean 玩家是否正在格挡
 */
const blocking = function () {
    return player.isBlocking()
}

/**
 * 获取/修改玩家指南针目标
 *
 * @param loc Location 指南针目标
 * @return Location 指南针目标
 */
const compassTarget = function (loc) {
    if (loc == null) {
        return player.getCompassTarget()
    } else {
        player.setCompassTarget(loc)
        return loc
    }
}

/**
 * 获取玩家指南针目标X轴坐标
 *
 * @return double 玩家指南针目标X轴坐标
 */
const compassTargetX = function () {
    return player.getCompassTarget().getX()
}

/**
 * 获取玩家指南针目标Y轴坐标
 *
 * @return double 玩家指南针目标Y轴坐标
 */
const compassTargetY = function () {
    return player.getCompassTarget().getY()
}

/**
 * 获取玩家指南针目标Z轴坐标
 *
 * @return double 玩家指南针目标Z轴坐标
 */
const compassTargetZ = function () {
    return player.getCompassTarget().getZ()
}

/**
 * 获取本月日期
 *
 * @return int 日期
 */
const day = function () {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
}

/**
 * 获取本月日期
 *
 * @return int 日期
 */
const dayOfMonth = function () {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
}

/**
 * 获取今天是星期几(1-7)
 *
 * @return int 星期几
 */
const dayOfWeek = function () {
    return (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) || 7
}

/**
 * 获取今天是一年中的第几天(1-365)
 *
 * @return int 天数
 */
const dayOfYear = function () {
    return Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
}

/**
 * 获取现在是几月(1-12)
 *
 * @return int 月份
 */
const month = function () {
    return Calendar.getInstance().get(Calendar.MONTH) + 1
}

/**
 * 获取当前年份
 *
 * @return int 年份
 */
const year = function () {
    return Calendar.getInstance().get(Calendar.YEAR)
}

/**
 * 获取现在是几点钟
 *
 * @return int 小时
 */
const hour = function () {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
}

/**
 * 获取现在是几分钟
 *
 * @return int 分钟
 */
const minute = function () {
    return Calendar.getInstance().get(Calendar.MINUTE)
}

/**
 * 获取现在是几秒钟
 *
 * @return int 秒钟
 */
const second = function () {
    return Calendar.getInstance().get(Calendar.SECOND)
}

/**
 * 获取现在是本月第几周
 *
 * @return int 周数
 */
const weekOfMonth = function () {
    return Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)
}

/**
 * 获取现在是本年第几周
 *
 * @return int 周数
 */
const weekOfYear = function () {
    return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
}

/**
 * 获取现在是上午还是下午(0/1)
 *
 * @return int 上午/下午
 */
const amOrPm = function () {
    return Calendar.getInstance().get(Calendar.AM_PM)
}

/**
 * 获取当前时间戳
 *
 * @return int 时间戳
 */
const time = function () {
    return new Date().getTime()
}

/**
 * 获取玩家是否死亡
 *
 * @return Boolean 玩家当前是否死亡
 */
const dead = function () {
    return player.isDead()
}

/**
 * 获取/修改玩家疲劳度
 *
 * @param value float 玩家疲劳度
 * @return float 玩家疲劳度
 */
const exhaustion = function (value) {
    if (value == null) {
        return player.getExhaustion()
    } else {
        player.setExhaustion(value)
        return value
    }
}

/**
 * 获取/修改玩家经验值
 *
 * @param value int 经验值
 * @return int 经验值
 */
const exp = function (value) {
    if (value == null) {
        return player.getTotalExperience()
    } else {
        player.setTotalExperience(value)
        return value
    }
}

/**
 * 给予玩家经验值
 *
 * @param value int 经验值
 */
const addExp = function (value) {
    player.giveExp(value)
}

/**
 * 扣除玩家经验值
 *
 * @param value int 经验值
 */
const takeExp = function (value) {
    player.giveExp(-value)
}

/**
 * 获取/修改玩家等级
 *
 * @param value int 等级
 * @return int 等级
 */
const level = function (value) {
    if (value == null) {
        return player.getLevel()
    } else {
        player.setLevel(value)
        return value
    }
}

/**
 * 给予玩家等级
 *
 * @param value int 等级
 */
const addLevel = function (value) {
    player.giveExpLevels(value)
}

/**
 * 扣除玩家等级
 *
 * @param value int 等级
 */
const takeLevel = function (value) {
    player.giveExpLevels(-value)
}

/**
 * 玩家是否首次登录
 *
 * @return Boolean 是否首次登录
 */
const firstPlay = function () {
    return !player.hasPlayedBefore()
}

/**
 * 获取/修改玩家飞行状态
 *
 * @param value Boolean 玩家飞行状态
 * @return Boolean 玩家飞行状态
 */
const fly = function (value) {
    if (value == null) {
        return player.isFlying()
    } else {
        player.setFlying(value)
        return value
    }
}

/**
 * 获取/修改玩家飞行速度
 *
 * @param value float 玩家飞行速度
 * @return float 玩家飞行速度
 */
const flySpeed = function (value) {
    if (value == null) {
        return player.getFlySpeed()
    } else {
        player.setFlySpeed(value)
        return value
    }
}

/**
 * 获取/修改玩家行走速度
 *
 * @param value float 玩家行走速度
 * @return float 玩家行走速度
 */
const walkSpeed = function (value) {
    if (value == null) {
        return player.getWalkSpeed()
    } else {
        player.setWalkSpeed(value)
        return value
    }
}

/**
 * 获取/修改玩家饥饿度
 *
 * @param value int 饥饿度
 * @return int 饥饿度
 */
const food = function (value) {
    if (value == null) {
        return player.getFoodLevel()
    } else {
        player.setFoodLevel(value)
        return value
    }
}

/**
 * 给予玩家饥饿度
 *
 * @param value int 饥饿度
 */
const addFood = function (value) {
    player.setFoodLevel(Math.min(Math.max(player.getFoodLevel() + value, 0), 20))
}

/**
 * 扣除玩家饥饿度
 *
 * @param value int 饥饿度
 */
const takeFood = function (value) {
    player.setFoodLevel(Math.min(Math.max(player.getFoodLevel() - value, 0), 20))
}

/**
 * 获取/修改玩家游戏模式(ADVENTURE/CREATIVE/SPECTATOR/SURVIVAL)
 *
 * @param value String 游戏模式
 * @return String 游戏模式
 */
const gamemode = function (value) {
    if (value == null) {
        return player.getGameMode().toString()
    } else {
        player.setGameMode(GameMode.valueOf(value.toUpperCase()))
        return value
    }
}

/**
 * 获取/修改玩家滑翔状态
 *
 * @param value Boolean 滑翔状态
 * @return Boolean 滑翔状态
 */
const guilding = function (value) {
    if (value == null) {
        return player.isGliding()
    } else {
        player.setGliding(value)
        return value
    }
}

/**
 * 获取/修改玩家发光状态
 *
 * @param value Boolean 发光状态
 * @return Boolean 发光状态
 */
const glowing = function (value) {
    if (value == null) {
        return player.isGlowing()
    } else {
        player.setGlowing(value)
        return value
    }
}

/**
 * 获取/修改玩家是否拥有重力
 *
 * @param value Boolean 重力状态
 * @return Boolean 重力状态
 */
const gravity = function (value) {
    if (value == null) {
        return player.hasGravity()
    } else {
        player.setGravity(value)
        return value
    }
}

/**
 * 获取玩家生命
 *
 * @return double 玩家生命
 */
const health = function () {
    return player.getHealth()
}

/**
 * 获取玩家最大生命
 *
 * @return double 玩家生命
 */
const maxHealth = function () {
    return player.getMaxHealth()
}

/**
 * 获取玩家名称
 *
 * @return String 玩家名称
 */
const name = function () {
    return player.getName()
}

/**
 * 获取/修改玩家剩余氧气
 *
 * @param value int 剩余氧气
 * @return int 剩余氧气
 */
const remainingAir = function (value) {
    if (value == null) {
        return player.getRemainingAir()
    } else {
        player.setRemainingAir(value)
        return value
    }
}

/**
 * 判断玩家是否正在睡觉
 *
 * @return Boolean 睡觉状态
 */
const sleeping = function () {
    return player.isSleeping()
}

/**
 * 获取/设置玩家潜行状态
 *
 * @return Boolean 潜行状态
 */
const sneaking = function (value) {
    if (value == null) {
        return player.isSneaking()
    } else {
        player.setSneaking(value)
        return value
    }
}

/**
 * 获取/设置玩家疾跑状态
 *
 * @return Boolean 疾跑状态
 */
const sprinting = function (value) {
    if (value == null) {
        return player.isSprinting()
    } else {
        player.setSprinting(value)
        return value
    }
}

/**
 * 获取/设置玩家游泳状态
 *
 * @return Boolean 游泳状态
 */
const swimming = function (value) {
    if (value == null) {
        return player.isSwimming()
    } else {
        player.setSwimming(value)
        return value
    }
}

/**
 * 获取玩家所处世界名称
 *
 * @return String 世界名称
 */
const world = function () {
    return player.getWorld().getName()
}

/**
 * 获取玩家主手物品
 */
const mainHandItem = function () {
    return player.getInventory().getItemInMainHand()
}

/**
 * 获取玩家副手物品
 */
const offHandItem = function () {
    return player.getInventory().getItemInOffHand()
}

/**
 * 在damage,block,kill相关动作中获取MM怪物ID, 不是MM怪物则返回null
 */
const getAttackerMobId = function () {
    const hooker = HookerManager.INSTANCE.mythicMobsHooker
    if (hooker == null) return null
    return hooker.getMythicId(event.getDamager())
}

/**
 * 在damage,block,kill相关动作中获取MM怪物ID, 不是MM怪物则返回null
 */
const getDefenderMobId = function () {
    const hooker = HookerManager.INSTANCE.mythicMobsHooker
    if (hooker == null) return null
    return hooker.getMythicId(event.getEntity())
}
