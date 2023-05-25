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
let NBTTagCompound
if (CraftItemStack.asNMSCopy(tempItem).getTag !== undefined) {
    NBTTagCompound = CraftItemStack.asNMSCopy(tempItem).getTag().class.static
} else {
    NBTTagCompound = CraftItemStack.asNMSCopy(tempItem).v().class.static
}
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

function getNBTType(nbtBase) {
    return ItemTagType.values()[getTypeId(nbtBase)]
}

toNBT = function(data) {
    if (data instanceof ConfigurationSection) {
        const itemTag = new NBTTagCompound()
        data.getKeys(false).forEach(function(key) {
            nbtMap(itemTag).put(key, toNBT(data.get(key)))
        })
        return itemTag
    } else if (data instanceof Map) {
        const itemTag = new NBTTagCompound()
        data.entrySet().forEach(function(entry) {
            nbtMap(itemTag).put(entry.key, toNBT(entry.value))
        })
        return itemTag
    } else if (data instanceof List) {
        let allByte = true
        let allInt = true
        const list = new ArrayList()
        for (let i = 0; i < data.size(); i++) {
            const current = ItemUtils.cast(data[i])
            if (!(current instanceof Byte)) allByte = false
            if (!(current instanceof Integer)) allInt = false
            list.add(current)
        }
        if (allByte) {
            const result = new ByteArray(list.size())
            for (let i = 0; i < list.size(); i++) {
                result[i] = list[i]
            }
            return asNBT(result)
        }
        if (allInt) {
            const result = new IntArray(list.size())
            for (let i = 0; i < list.size(); i++) {
                result[i] = list[i]
            }
            return asNBT(result)
        }
        const result = new NBTTagList()
        for (let i = 0; i < list.size(); i++) {
            result.add(toNBT(data[i]))
        }
        return result
    } else {
        return asNBT(data)
    }
}

if (tempInventory.getItem(0).handle !== undefined) {
    // 1.16
    if (CraftItemStack.asNMSCopy(tempItem).setTag !== undefined) {
        getTypeId = function(nbtBase) {
            return nbtBase.getTypeId()
        }

        nbtMap = function(nbtBase) {
            return nbtBase.map
        }

        nbtSize = function(nbtBase) {
            return nbtBase.map.size()
        }

        asValue = function(nbtBase) {
            if (nbtBase instanceof NBTBase) {
                switch (getTypeId(nbtBase)) {
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

        toString = function(nbtBase) {
            return nbtBase.toString()
        }

        asByte = function(nbtBase) {
            return nbtBase.asByte()
        }

        asShort = function(nbtBase) {
            return nbtBase.asShort()
        }

        asInt = function(nbtBase) {
            return nbtBase.asInt()
        }

        asLong = function(nbtBase) {
            return nbtBase.asLong()
        }

        asFloat = function(nbtBase) {
            return nbtBase.asFloat()
        }

        asDouble = function(nbtBase) {
            return nbtBase.asDouble()
        }

        asString = function(nbtBase) {
            return nbtBase.asString()
        }

        asByteArray = function(nbtBase) {
            return nbtBase.asBytes()
        }

        asIntArray = function(nbtBase) {
            return nbtBase.asInts()
        }

        getOriginNMSItemTag = function(itemStack) {
            if (itemStack instanceof CraftItemStack) {
                const item = itemStack.handle
                if (item.getTag() == null) {
                    return new NBTTagCompound()
                }
                return item.getTag()
            } else if (itemStack instanceof ItemStack) {
                const item = CraftItemStack.asNMSCopy(itemStack)
                if (item.getTag() == null) {
                    return new NBTTagCompound()
                }
                return item.getTag()
            } else if (itemStack instanceof NMSItemStack) {
                if (itemStack.getTag() == null) {
                    return new NBTTagCompound()
                }
                return itemStack.getTag()
            }
        }

        getNMSItemTag = function(itemStack) {
            if (itemStack instanceof CraftItemStack) {
                const item = itemStack.handle
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
    // 1.18
    } else {
        getTypeId = function(nbtBase) {
            return nbtBase.b()
        }

        nbtMap = function(nbtBase) {
            return nbtBase.x
        }

        nbtSize = function(nbtBase) {
            return nbtBase.x.size()
        }

        asValue = function(nbtBase) {
            if (nbtBase instanceof NBTBase) {
                switch (getTypeId(nbtBase)) {
                    case CraftMagicNumbers.NBT.TAG_BYTE: {
                        return nbtBase.i()
                    }
                    case CraftMagicNumbers.NBT.TAG_SHORT: {
                        return nbtBase.h()
                    }
                    case CraftMagicNumbers.NBT.TAG_INT: {
                        return nbtBase.g()
                    }
                    case CraftMagicNumbers.NBT.TAG_LONG: {
                        return nbtBase.f()
                    }
                    case CraftMagicNumbers.NBT.TAG_FLOAT: {
                        return nbtBase.k()
                    }
                    case CraftMagicNumbers.NBT.TAG_DOUBLE: {
                        return nbtBase.j()
                    }
                    case CraftMagicNumbers.NBT.TAG_BYTE_ARRAY: {
                        return nbtBase.e()
                    }
                    case CraftMagicNumbers.NBT.TAG_STRING: {
                        return nbtBase.f_()
                    }
                    case CraftMagicNumbers.NBT.TAG_LIST: {
                        return nbtBase
                    }
                    case CraftMagicNumbers.NBT.TAG_COMPOUND: {
                        return nbtBase
                    }
                    case CraftMagicNumbers.NBT.TAG_INT_ARRAY: {
                        return nbtBase.g()
                    }
                    default: {
                        return null
                    }
                }
            } else {
                return nbtBase
            }
        }

        toString = function(nbtBase) {
            return nbtBase.f_()
        }

        asByte = function(nbtBase) {
            return nbtBase.i()
        }

        asShort = function(nbtBase) {
            return nbtBase.h()
        }

        asInt = function(nbtBase) {
            return nbtBase.g()
        }

        asLong = function(nbtBase) {
            return nbtBase.f()
        }

        asFloat = function(nbtBase) {
            return nbtBase.k()
        }

        asDouble = function(nbtBase) {
            return nbtBase.j()
        }

        asString = function(nbtBase) {
            return nbtBase.f_()
        }

        asByteArray = function(nbtBase) {
            return nbtBase.e()
        }

        asIntArray = function(nbtBase) {
            return nbtBase.g()
        }

        getOriginNMSItemTag = function(itemStack) {
            if (itemStack instanceof CraftItemStack) {
                const item = itemStack.handle
                if (item.v() == null) {
                    return new NBTTagCompound()
                }
                return item.v()
            } else if (itemStack instanceof ItemStack) {
                const item = CraftItemStack.asNMSCopy(itemStack)
                if (item.v() == null) {
                    return new NBTTagCompound()
                }
                return item.v()
            } else if (itemStack instanceof NMSItemStack) {
                if (itemStack.v() == null) {
                    return new NBTTagCompound()
                }
                return itemStack.v()
            }
        }

        getNMSItemTag = function(itemStack) {
            if (itemStack instanceof CraftItemStack) {
                const item = itemStack.handle
                if (item.v() == null) {
                    return new NBTTagCompound()
                }
                return item.v().h()
            } else if (itemStack instanceof ItemStack) {
                const item = CraftItemStack.asNMSCopy(itemStack)
                if (item.v() == null) {
                    return new NBTTagCompound()
                }
                return item.v().h()
            } else if (itemStack instanceof NMSItemStack) {
                if (itemStack.v() == null) {
                    return new NBTTagCompound()
                }
                return itemStack.v().h()
            }
        }

        setNMSItemTag = function(itemStack, itemTag) {
            if (itemStack instanceof CraftItemStack) {
                itemStack.handle.c(itemTag)
            } else if (itemStack instanceof ItemStack) {
                const item = CraftItemStack.asNMSCopy(itemStack)
                item.c(itemTag)
                itemStack.setItemMeta(CraftItemStack.asBukkitCopy(item).getItemMeta())
            } else if (itemStack instanceof NMSItemStack) {
                itemStack.c(itemTag)
            }
        }
    }
    // 1.12
} else {
    const handle = CraftItemStack.class.getDeclaredField("handle")
    handle.setAccessible(true)

    getTypeId = function(nbtBase) {
        return nbtBase.getTypeId()
    }

    nbtMap = function(nbtBase) {
        return nbtBase.map
    }

    nbtSize = function(nbtBase) {
        return nbtBase.map.size()
    }

    asValue = function(nbtBase) {
        if (nbtBase instanceof NBTBase) {
            switch (getTypeId(nbtBase)) {
                case CraftMagicNumbers.NBT.TAG_BYTE: {
                    return nbtBase.g()
                }
                case CraftMagicNumbers.NBT.TAG_SHORT: {
                    return nbtBase.f()
                }
                case CraftMagicNumbers.NBT.TAG_INT: {
                    return nbtBase.e()
                }
                case CraftMagicNumbers.NBT.TAG_LONG: {
                    return nbtBase.d()
                }
                case CraftMagicNumbers.NBT.TAG_FLOAT: {
                    return nbtBase.i()
                }
                case CraftMagicNumbers.NBT.TAG_DOUBLE: {
                    return nbtBase.asDouble()
                }
                case CraftMagicNumbers.NBT.TAG_BYTE_ARRAY: {
                    return nbtBase.c()
                }
                case CraftMagicNumbers.NBT.TAG_STRING: {
                    return nbtBase.c_()
                }
                case CraftMagicNumbers.NBT.TAG_LIST: {
                    return nbtBase
                }
                case CraftMagicNumbers.NBT.TAG_COMPOUND: {
                    return nbtBase
                }
                case CraftMagicNumbers.NBT.TAG_INT_ARRAY: {
                    return nbtBase.d()
                }
                default: {
                    return null
                }
            }
        } else {
            return nbtBase
        }
    }

    toString = function(nbtBase) {
        return nbtBase.toString()
    }

    asByte = function(nbtBase) {
        return nbtBase.g()
    }

    asShort = function(nbtBase) {
        return nbtBase.f()
    }

    asInt = function(nbtBase) {
        return nbtBase.e()
    }

    asLong = function(nbtBase) {
        return nbtBase.d()
    }

    asFloat = function(nbtBase) {
        return nbtBase.i()
    }

    asDouble = function(nbtBase) {
        return nbtBase.asDouble()
    }

    asString = function(nbtBase) {
        return nbtBase.c_()
    }

    asByteArray = function(nbtBase) {
        return nbtBase.c()
    }

    asIntArray = function(nbtBase) {
        return nbtBase.d()
    }

    getOriginNMSItemTag = function(itemStack) {
        if (itemStack instanceof CraftItemStack) {
            const item = handle.get(itemStack)
            if (item.getTag() == null) {
                return new NBTTagCompound()
            }
            return item.getTag()
        } else if (itemStack instanceof ItemStack) {
            const item = CraftItemStack.asNMSCopy(itemStack)
            if (item.getTag() == null) {
                return new NBTTagCompound()
            }
            return item.getTag()
        } else if (itemStack instanceof NMSItemStack) {
            if (itemStack.getTag() == null) {
                return new NBTTagCompound()
            }
            return itemStack.getTag()
        }
    }

    getNMSItemTag = function(itemStack) {
        if (itemStack instanceof CraftItemStack) {
            const item = handle.get(itemStack)
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
        switch (getTypeId(value)) {
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
                value = nbtMap(value).get(currentKey)
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

function getItemStackDeepValue(itemStack, key) {
    return asValue(getDeep(getOriginNMSItemTag(itemStack), key))
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
        switch (getTypeId(temp)) {
            case CraftMagicNumbers.NBT.TAG_COMPOUND: {
                // 记录父级ItemTag
                father = temp
                // 获取下一级, 如果下一级是空的就创建一个新NBTTagCompound()丢进去
                if (!nbtMap(temp).containsKey(node)) {
                    nbtMap(temp).put(node, new NBTTagCompound())
                }
                temp = nbtMap(temp).get(node)
                // 记录当前ItemTagData的Id
                tempId = node
                break
            }
            default: {
                // 新建一个ItemTag
                const fatherItemTag = new NBTTagCompound()
                // 覆盖上一层
                nbtMap(father).put(tempId, fatherItemTag)
                // 新建当前ItemTagData
                const tempItemTag = new NBTTagCompound()
                // 建立下一级ItemTagData
                nbtMap(fatherItemTag).put(node, tempItemTag)
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
    if (getTypeId(temp) === CraftMagicNumbers.NBT.TAG_COMPOUND) {
        // 东西丢进去
        if (value != null) {
            nbtMap(temp).put(node, asNBT(value))
        } else {
            nbtMap(temp).remove(node)
        }
        // 如果当前ItemTagData是其他类型
    } else {
        // 新建一个NBTTagCompound
        const newItemTag = new NBTTagCompound()
        // 东西丢进去
        if (value != null) {
            nbtMap(newItemTag).put(node, asNBT(value))
        } else {
            nbtMap(newItemTag).remove(node)
        }
        // 覆盖上一层
        nbtMap(father).put(tempId, newItemTag)
    }
}
