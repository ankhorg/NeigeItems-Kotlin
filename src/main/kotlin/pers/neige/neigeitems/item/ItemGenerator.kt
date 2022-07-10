package pers.neige.neigeitems.item

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ConfigUtils.loadFromString
import pers.neige.neigeitems.utils.ConfigUtils.saveToString
import pers.neige.neigeitems.utils.ItemUtils.inherit
import pers.neige.neigeitems.utils.ItemUtils.loadGlobalSections

// 物品ID
class ItemGenerator (private val itemConfig: ItemConfig) {
    // 物品ID
    val id = itemConfig.id
    // 物品所在文件
    val file = itemConfig.file
    // 物品原配置
    val originConfigSection = itemConfig.configSection?.clone() ?: YamlConfiguration() as ConfigurationSection
    // 解析后配置
    var configSection = (YamlConfiguration() as ConfigurationSection).inherit(originConfigSection).loadGlobalSections()
    // 物品配置文本
    val configString = configSection.saveToString()
    // 物品配置文本哈希值
    val hashCode = configString.hashCode()

    fun getItemStack(player: OfflinePlayer?, data: String?): ItemStack {
        var configString = this.configString

        // 进行一次papi解析
        if (player != null) {
            configString = PlaceholderAPI.setPlaceholders(player, configString)
        }
        // 加载回YamlConfiguration
        val configSection = configString.loadFromString(id)

        // 获取随机数, 用于代表当前物品
        val sectionData = {}
        // 加载指向数据
        if (data) dataParse_NI(data, sectionData)

        // 获取私有节点配置
        if (configSection.contains("sections")) var Sections = configSection.getConfigurationSection("sections")
        // 如果当前物品包含预声明节点
        if (Sections != undefined) {
            // 针对每个节点
            Sections.getKeys(false).forEach(function(section) {
                // 节点解析
                globalSectionParse_NI(Sections, section, sectionData, player)
            })
        }
        // 对文本化配置进行全局节点解析
        tempConfigSection = new YamlConfiguration()
        tempConfigSection.set(this.id, configSection)
        configString = getSection_NI(Sections, tempConfigSection.saveToString(), sectionData, player)
        configString = configString.replace(/\\</g, "<").replace(/\\>/g, ">")
        if (player instanceof Player) configString = setPapiWithNoColor_NI(player, configString)
        if (config_NI.Debug) print(configString)
        configSection = new YamlConfiguration()
        configSection.loadFromString(configString)
        configSection = configSection.getConfigurationSection(this.id)
        // 构建物品
        let material
        if (configSection.contains("material") && configSection.getString("material") && (material = Material.matchMaterial(configSection.getString("material").toUpperCase()))) {
            let itemStack = new ItemStack(material)
            // 设置子ID/损伤值
            if (configSection.contains("damage")) {
                itemStack.setDurability(configSection.getInt("damage"))
            }
            // 设置物品附魔
            if (configSection.contains("enchantments")) {
                let enchantSection = configSection.getConfigurationSection("enchantments")
                if (enchantSection instanceof MemorySection) {
                    enchantSection.getKeys(false).forEach(function(enchant) {
                        if (enchant != null) {
                            let level = enchantSection.getInt(enchant)
                            if (level > 0
                                && (enchant = Enchantment.getByName(enchant.toUpperCase()))) {
                                itemStack.addUnsafeEnchantment(enchant, level)
                            }
                        }
                    })
                }
            }
            // 获取ItemMeta
            let itemMeta = itemStack.getItemMeta()
            // 设置CustomModelData
            if (configSection.contains("custommodeldata")) {
                try { itemMeta.setCustomModelData(configSection.getInt("custommodeldata")) } catch (e) {}
            }
            // 设置物品名
            if (configSection.contains("name")) {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', configSection.getString("name")))
            }
            // 设置Lore
            if (configSection.contains("lore")) {
                let originLores = configSection.getStringList("lore")
                originLores.replaceAll(function(lore) {return ChatColor.translateAlternateColorCodes('&', lore)})
                let finalLores = new ArrayList()
                for (let index = 0; index < originLores.length; index++) {
                    const lores = originLores[index].split("\n")
                    for (let index = 0; index < lores.length; index++) {
                    finalLores.add(lores[index])
                }
                }
                itemMeta.setLore(finalLores)
            }
            // 设置是否无法破坏
            if (configSection.contains("unbreakable")) {
                itemMeta.setUnbreakable(configSection.getBoolean("unbreakable"))
            }
            // 设置ItemFlags
            if (configSection.contains("hideflags")) {
                var flags = configSection.getStringList("hideflags")
                if (flags.length) {
                    for (let key in flags) {
                        itemMeta.addItemFlags(ItemFlag.valueOf(flags[key]))
                    }
                }
            }
            // 设置物品颜色
            if (configSection.contains("color")) {
                let color = configSection.get("color")
                if (typeof color == "string") color = parseInt(color, 16)
                try { itemMeta.setColor(Color.fromRGB(color)) } catch (e) {}
            }
            itemStack.setItemMeta(itemMeta)
            // 设置物品NBT
            let itemTag = NMSKt.getItemTag(itemStack)
            itemTag.NeigeItems = new ItemTag()
            itemTag.NeigeItems.id = new ItemTagData(this.id)
            itemTag.NeigeItems.data = new ItemTagData(JSON.stringify(sectionData))
            itemTag.NeigeItems.hashCode = new ItemTagData(this.hashCode)
            // 设置物品使用次数
            if (configSection.contains("options.charge")) {
                itemTag.NeigeItems.charge = new ItemTagData(configSection.get("options.charge"))
                itemTag.NeigeItems.maxCharge = new ItemTagData(configSection.get("options.charge"))
            }
            // 设置掉落物闪光颜色
            if (configSection.contains("options.color")) {
                const colorString = configSection.getString("options.color").toUpperCase()
                // 判断你这颜色保不保熟
                if (ChatColor[colorString] != undefined) {
                    itemTag.NeigeItems.color = new ItemTagData(colorString)
                }
            }
            if (configSection.contains("options.dropskill")) {
                itemTag.NeigeItems.dropSkill = new ItemTagData(configSection.get("options.dropskill"))
            }
            // 设置物品NBT
            if (configSection.contains("nbt")) {
                // 获取配置NBT
                let itemNBT = toItemTagNBT_NI(toHashMap_NI(configSection.get("nbt")))
                itemTag = mergeItemTag(itemTag, itemNBT)
            }
            try {
                itemTag.saveTo(itemStack)
            } catch (e) {
                let invalidItemMessage = invalidItem.replace(/{itemID}/, configSection.getName())
                if (sender) {
                    sender.sendMessage(invalidNBT)
                    sender.sendMessage(invalidItemMessage)
                } else {
                    bukkitServer.getConsoleSender().sendMessage(invalidNBT)
                    bukkitServer.getConsoleSender().sendMessage(invalidItemMessage)
                }
            }
            // 删除节点缓存
            delete sectionData
                    return itemStack
        } else {
            delete sectionData
                    return null
        }
    }
}
