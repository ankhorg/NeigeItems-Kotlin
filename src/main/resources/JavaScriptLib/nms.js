const Bukkit = Packages.org.bukkit.Bukkit
const ByteArray = Java.type("byte[]")
const IntArray = Java.type("int[]")
const Byte = Packages.java.lang.Byte
const Short = Packages.java.lang.Short
const Integer = Packages.java.lang.Integer
const Double = Packages.java.lang.Double
const Float = Packages.java.lang.Float
const Long = Packages.java.lang.Long
const String = Packages.java.lang.String
const List = Packages.java.util.List
const Map = Packages.java.util.Map
const ArrayList = Packages.java.util.ArrayList
const Material = Packages.org.bukkit.Material
const ItemStack = Packages.org.bukkit.inventory.ItemStack
const tempInventory = Bukkit.createInventory(null, 9, "")
tempInventory.setItem(0, new ItemStack(Material.STONE))
const CraftItemStack = tempInventory.getItem(0).class.static
const NMSItemStack = CraftItemStack.asNMSCopy(tempInventory.getItem(0)).class.static
const craftMagicNumbers = Bukkit.getUnsafe()
const CraftMagicNumbers = craftMagicNumbers.class.static
const tempItem = new ItemStack(Material.STONE)
const tempMeta = tempItem.getItemMeta()
tempMeta.setDisplayName("test")
tempItem.setItemMeta(tempMeta)
const NBTTagCompound = CraftItemStack.asNMSCopy(tempItem).getTag().class.static
const NBTBasePrefix = NBTTagCompound.class.getCanonicalName().replace("NBTTagCompound", "")
const NBTBase = Java.type(NBTBasePrefix + "NBTBase")
const NBTTagByte = Java.type(NBTBasePrefix + "NBTTagByte")
const NBTTagByteArray = Java.type(NBTBasePrefix + "NBTTagByteArray")
const NBTTagShort = Java.type(NBTBasePrefix + "NBTTagShort")
const NBTTagInt = Java.type(NBTBasePrefix + "NBTTagInt")
const NBTTagIntArray = Java.type(NBTBasePrefix + "NBTTagIntArray")
const NBTTagDouble = Java.type(NBTBasePrefix + "NBTTagDouble")
const NBTTagFloat = Java.type(NBTBasePrefix + "NBTTagFloat")
const NBTTagLong = Java.type(NBTBasePrefix + "NBTTagLong")
const NBTTagString = Java.type(NBTBasePrefix + "NBTTagString")
const NBTTagList = Java.type(NBTBasePrefix + "NBTTagList")
const StringUtils = Packages.pers.neige.neigeitems.utils.StringUtils
const ItemUtils = Packages.pers.neige.neigeitems.utils.ItemUtils
const NMSGeneric = Packages.pers.neige.neigeitems.nms.NMSGeneric.INSTANCE
const ItemTagData = Packages.pers.neige.neigeitems.nms.ItemTagData
const ItemTagType = Packages.pers.neige.neigeitems.nms.ItemTagType
const ConfigurationSection = Packages.org.bukkit.configuration.ConfigurationSection
const YamlConfiguration = Packages.org.bukkit.configuration.file.YamlConfiguration

function asNMSCopy(itemStack) {
    return CraftItemStack.asNMSCopy(itemStack)
}

function newItemTag() {
    return new NBTTagCompound()
}

function newItemTagList() {
    return new NBTTagList()
}

function toString(nbtBase) {
    return nbtBase.asString()
}

function asByte(nbtBase) {
    return nbtBase.asByte()
}

function asShort(nbtBase) {
    return nbtBase.asShort()
}

function asInt(nbtBase) {
    return nbtBase.asInt()
}

function asLong(nbtBase) {
    return nbtBase.asLong()
}

function asFloat(nbtBase) {
    return nbtBase.asFloat()
}

function asDouble(nbtBase) {
    return nbtBase.asDouble()
}

function asString(nbtBase) {
    return nbtBase.asString()
}

function asByteArray(nbtBase) {
    return nbtBase.asBytes()
}

function asIntArray(nbtBase) {
    return nbtBase.asInts()
}

function asList(nbtBase) {
    return nbtBase.asInts()
}

function nbtMap(nbtBase) {
    return nbtBase.map
}

function nbtSize(nbtBase) {
    return nbtBase.map.size()
}

function getNBTType(nbtBase) {
    return ItemTagType.values()[nbtBase.getTypeId()]
}

function getNMSItemTag(itemStack) {
    if (itemStack instanceof CraftItemStack) {
        const item = CraftItemStack.asNMSCopy(itemStack)
        if (item.getTag() == null) {
            return new NBTTagCompound()
        }
        return item.getTag().clone()
    } else if (itemStack instanceof ItemStack) {
        const item = CraftItemStack.asNMSCopy(itemStack)
        if (item.getTag() == null) {
            return new NBTTagCompound()
        }
        return item.getTag().clone()
    } else if (itemStack instanceof NMSItemStack) {
        if (itemStack.getTag() == null) {
            return new NBTTagCompound()
        }
        return itemStack.getTag().clone()
    }
}

if (tempInventory.getItem(0).handle != undefined) {
    setNMSItemTag = function(itemStack, itemTag) {
        if (itemStack instanceof CraftItemStack) {
            itemStack.handle.setTag(itemTag)
        } else if (itemStack instanceof ItemStack) {
            const item = CraftItemStack.asNMSCopy(itemStack)
            item.setTag(itemTag)
            itemStack.setItemMeta(CraftItemStack.asBukkitCopy(item).getItemMeta())
        } else if (itemStack instanceof NMSItemStack) {
            itemStack.setTag(itemTag)
        }
    }
} else {
    const handle = CraftItemStack.class.getDeclaredField("handle")
    handle.setAccessible(true)
    setNMSItemTag = function(itemStack, itemTag) {
        if (itemStack instanceof CraftItemStack) {
            handle.get(itemStack).setTag(itemTag)
        } else if (itemStack instanceof ItemStack) {
            const item = CraftItemStack.asNMSCopy(itemStack)
            item.setTag(itemTag)
            itemStack.setItemMeta(CraftItemStack.asBukkitCopy(item).getItemMeta())
        } else if (itemStack instanceof NMSItemStack) {
            itemStack.setTag(itemTag)
        }
    }
}

function asValue(nbtBase) {
    if (nbtBase instanceof NBTBase) {
        switch (nbtBase.getTypeId()) {
            case CraftMagicNumbers.NBT.TAG_BYTE: {
                return nbtBase.asByte()
            }
            case CraftMagicNumbers.NBT.TAG_SHORT: {
                return nbtBase.asShort()
            }
            case CraftMagicNumbers.NBT.TAG_INT: {
                return nbtBase.asInt()
            }
            case CraftMagicNumbers.NBT.TAG_LONG: {
                return nbtBase.asLong()
            }
            case CraftMagicNumbers.NBT.TAG_FLOAT: {
                return nbtBase.asFloat()
            }
            case CraftMagicNumbers.NBT.TAG_DOUBLE: {
                return nbtBase.asDouble()
            }
            case CraftMagicNumbers.NBT.TAG_BYTE_ARRAY: {
                return nbtBase.getBytes()
            }
            case CraftMagicNumbers.NBT.TAG_STRING: {
                return nbtBase.asString()
            }
            case CraftMagicNumbers.NBT.TAG_LIST: {
                return nbtBase
            }
            case CraftMagicNumbers.NBT.TAG_COMPOUND: {
                return nbtBase
            }
            case CraftMagicNumbers.NBT.TAG_INT_ARRAY: {
                return nbtBase.getInts()
            }
            default: {
                return null
            }
        }
    } else {
        return nbtBase
    }
}

try {
    new NBTTagString("123")
    newByte = function(value) {
        return new NBTTagByte(value)
    }
    newShort = function(value) {
        return new NBTTagShort(value)
    }
    newInt = function(value) {
        return new NBTTagInt(value)
    }
    newLong = function(value) {
        return new NBTTagLong(value)
    }
    newFloat = function(value) {
        return new NBTTagFloat(value)
    }
    newDouble = function(value) {
        return new NBTTagDouble(value)
    }
    newString = function(value) {
        return new NBTTagString(value)
    }
} catch(error) {
    newByte = function(value) {
        return NBTTagByte.a(value)
    }
    newShort = function(value) {
        return NBTTagShort.a(value)
    }
    newInt = function(value) {
        return NBTTagInt.a(value)
    }
    newLong = function(value) {
        return NBTTagLong.a(value)
    }
    newFloat = function(value) {
        return NBTTagFloat.a(value)
    }
    newDouble = function(value) {
        return NBTTagDouble.a(value)
    }
    newString = function(value) {
        return NBTTagString.a(value)
    }
}

function asNBT(value) {
    if (value instanceof Byte) {
        return newByte(value)
    } else if (value instanceof Short) {
        return newShort(value)
    } else if (value instanceof Integer) {
        return newInt(value)
    } else if (value instanceof Long) {
        return newLong(value)
    } else if (value instanceof Float) {
        return newFloat(value)
    } else if (value instanceof Double) {
        return newDouble(value)
    } else if (value instanceof ByteArray) {
        return new NBTTagByteArray(value)
    } else if (value instanceof String) {
        value = ItemUtils.cast(value)
        if (value instanceof String) {
            return newString(value)
        } else {
            return asNBT(value)
        }
    } else if (value instanceof IntArray) {
        return new NBTTagIntArray(value)
    } else if (value instanceof List && !(value instanceof NBTTagList)) {
        const list = new NBTTagList()
        for (let index = 0; index < value.length; index++) {
            list.add(index, asNBT(value[index]))
        }
        return list
    }

    return value
}

function getDeep(itemTag, key) {
    if (!(itemTag instanceof NBTBase) || !(key instanceof String)) return
    // 当前层级ItemTagData
    let value = itemTag
    // key以.作分隔
    const args = StringUtils.split(key, '.', '\\')

    // 逐层深入
    for (let index = 0; index < args.length; index++) {
        const currentKey = args[index]
        // 检测当前ItemTagData类型
        switch (value.getTypeId()) {
            case CraftMagicNumbers.NBT.TAG_LIST: {
                // key无法转换为数字/数组越界将返回null, 其他情况下将返回下一级
                let index = StringUtils.toIntOrNull(currentKey)
                if (index != null) {
                    index = Math.max(index, 0)
                    if (index != null) {
                        const list = value
                        if (list.size() > index) {
                            value = list[index]
                        } else {
                            return null
                        }
                    }
                }
                break
            }
            case CraftMagicNumbers.NBT.TAG_BYTE_ARRAY: {
                // key无法转换为数字/数组越界将返回null, 其他情况下将返回下一级
                let index = StringUtils.toIntOrNull(currentKey)
                if (index != null) {
                    index = Math.max(index, 0)
                    if (index != null) {
                        const array = value.getBytes()
                        if (array.length > index) {
                            value = asNBT(array[index])
                        } else {
                            return null
                        }
                    }
                }
                break
            }
            case CraftMagicNumbers.NBT.TAG_INT_ARRAY: {
                // key无法转换为数字/数组越界将返回null, 其他情况下将返回下一级
                let index = StringUtils.toIntOrNull(currentKey)
                if (index != null) {
                    index = Math.max(index, 0)
                    if (index != null) {
                        const array = value.getInts()
                        if (array.length > index) {
                            value = asNBT(array[index])
                        } else {
                            return null
                        }
                    }
                }
                break
            }
            case CraftMagicNumbers.NBT.TAG_COMPOUND: {
                // 当前Compound不含对应的key将返回null, 其他情况下将返回下一级
                value = value.get(currentKey)
                break
            }
            default: {
                return null
            }
        }
    }

    return value
}

function getDeepValue(itemTag, key) {
    return asValue(getDeep(itemTag, key))
}

function putDeep(itemTag, key, value) {
    if (!(itemTag instanceof NBTBase) || !(key instanceof String)) return
    // 父级ItemTag
    let father = itemTag
    // 当前ItemTagData的Id
    let tempId = ""
    // 当前ItemTagData
    let temp = itemTag
    // 待获取ItemTag键
    const args = StringUtils.split(key, '.', '\\')

    // 逐层深入
    for (let index = 0; index < (args.size() - 1); index++) {
        // 获取下一级Id
        const node = args[index]
        // 判断当前ItemTagData类型
        switch (temp.getTypeId()) {
            case CraftMagicNumbers.NBT.TAG_COMPOUND: {
                // 记录父级ItemTag
                father = temp
                // 获取下一级, 如果下一级是空的就创建一个新NBTTagCompound()丢进去
                if (!temp.hasKey(node)) {
                    temp.set(node, new NBTTagCompound())
                }
                temp = temp.get(node)
                // 记录当前ItemTagData的Id
                tempId = node
                break
            }
            default: {
                // 新建一个ItemTag
                const fatherItemTag = new NBTTagCompound()
                // 覆盖上一层
                father.set(tempId, fatherItemTag)
                // 新建当前ItemTagData
                const tempItemTag = new NBTTagCompound()
                // 建立下一级ItemTagData
                fatherItemTag.set(node, tempItemTag)
                // 记录父级ItemTag
                father = fatherItemTag
                // 记录当前ItemTagData
                temp = tempItemTag
                // 记录当前ItemTagData的Id
                tempId = node
            }
        }
    }

    // 已达末级
    const node = args[args.size() - 1]
    if (temp.getTypeId() == CraftMagicNumbers.NBT.TAG_COMPOUND) {
        // 东西丢进去
        temp.set(node, asNBT(value))
        // 如果当前ItemTagData是其他类型
    } else {
        // 新建一个NBTTagCompound
        const newItemTag = new NBTTagCompound()
        // 东西丢进去
        newItemTag.set(node, asNBT(value))
        // 覆盖上一层
        father.set(tempId, newItemTag)
    }
}

function toNBT(data) {
    if (data instanceof ConfigurationSection) {
        const itemTag = new NBTTagCompound()
        data.getKeys(false).forEach(function(key) {
            itemTag.map.put(key, toNBT(data.get(key)))
        })
        return itemTag
    } else if (data instanceof Map) {
        const itemTag = new NBTTagCompound()
        data.entrySet().forEach(function(entry) {
            itemTag.map.put(entry.key, toNBT(entry.value))
        })
        return itemTag
    } else if (data instanceof List) {
        const list = new NBTTagList()
        for (let i = 0; i < data.size(); i++) {
            list.add(toNBT(data[i]))
        }
        return list
    } else {
        return asNBT(data)
    }
}
