package pers.neige.neigeitems.nms

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.script.CompiledScript
import java.io.InputStreamReader

object NMSGeneric {
    val script: CompiledScript = let {
        val reader = InputStreamReader(plugin.getResource("JavaScriptLib/nms.js")!!, "UTF-8")
        val script = CompiledScript(reader)
        reader.close()
        script
    }

    @JvmStatic
    fun ConfigurationSection.toItemTag(): ItemTag {
        return ItemTag(script.simpleInvoke("toNBT", this)!!)
    }

    @JvmStatic
    fun ConfigurationSection.toNiItemTag(): ItemTag {
        return ItemTag(script.simpleInvoke("toNBT", this)!!)
    }

    @JvmStatic
    fun ItemStack.getItemTag(): ItemTag {
        return ItemTag(script.simpleInvoke("getNMSItemTag", this)!!)
    }

    @JvmStatic
    fun ItemStack.getDeepValue(key: String): Any? {
        return script.simpleInvoke("getItemStackDeepValue", this, key)
    }

    fun getItemTag1(itemStack: ItemStack): ItemTag {
        return ItemTag(script.simpleInvoke("getNMSItemTag", itemStack)!!)
    }

    @JvmStatic
    fun ItemStack.setItemTag(itemTag: ItemTag) {
        script.simpleInvoke("setNMSItemTag", this, itemTag.nbt)
    }

    @JvmStatic
    fun ItemStack.asNMSCopy(): Any {
        return script.simpleInvoke("asNMSCopy", this)!!
    }

    @JvmStatic
    fun getNMSItemTag(itemStack: ItemStack): Any? {
        return script.simpleInvoke("getNMSItemTag", itemStack)
    }

    @JvmStatic
    fun setNMSItemTag(itemStack: ItemStack, nbtBase: Any?) {
        script.simpleInvoke("setNMSItemTag", itemStack, nbtBase)
    }

    @JvmStatic
    fun toString(nbtBase: Any?): String? {
        return script.simpleInvoke("toString", nbtBase) as? String?
    }

    @JvmStatic
    fun asNBT(value: Any?): Any? {
        return script.simpleInvoke("asNBT", value)
    }

    @JvmStatic
    fun asValue(nbtBase: Any?): Any? {
        return script.simpleInvoke("asValue", nbtBase)
    }

    @JvmStatic
    fun getDeep(nbtBase: Any?, key: String): Any? {
        return script.simpleInvoke("getDeep", nbtBase, key)
    }

    @JvmStatic
    fun getDeepValue(nbtBase: Any?, key: String): Any? {
        return script.simpleInvoke("getDeepValue", nbtBase, key)
    }

    @JvmStatic
    fun putDeep(nbtBase: Any?, key: String, value: Any?): Any? {
        script.simpleInvoke("putDeep", nbtBase, key, value)
        return value
    }

    @JvmStatic
    fun newItemTag(): Any {
        return script.simpleInvoke("newItemTag")!!
    }

    @JvmStatic
    fun newItemTagList(): java.util.AbstractList<Any> {
        return script.simpleInvoke("newItemTagList")!! as java.util.AbstractList<Any>
    }

    @JvmStatic
    fun asByte(nbtBase: Any): Byte {
        return script.simpleInvoke("asByte", nbtBase) as Byte
    }

    @JvmStatic
    fun asShort(nbtBase: Any): Short {
        return script.simpleInvoke("asShort", nbtBase) as Short
    }

    @JvmStatic
    fun asInt(nbtBase: Any): Int {
        return script.simpleInvoke("asInt", nbtBase) as Int
    }

    @JvmStatic
    fun asLong(nbtBase: Any): Long {
        return script.simpleInvoke("asLong", nbtBase) as Long
    }

    @JvmStatic
    fun asFloat(nbtBase: Any): Float {
        return script.simpleInvoke("asFloat", nbtBase) as Float
    }

    @JvmStatic
    fun asDouble(nbtBase: Any): Double {
        return script.simpleInvoke("asDouble", nbtBase) as Double
    }

    @JvmStatic
    fun asString(nbtBase: Any): String {
        return script.simpleInvoke("asString", nbtBase) as String
    }

    @JvmStatic
    fun asByteArray(nbtBase: Any): ByteArray {
        return script.simpleInvoke("asByteArray", nbtBase) as ByteArray
    }

    @JvmStatic
    fun asIntArray(nbtBase: Any): IntArray {
        return script.simpleInvoke("asIntArray", nbtBase) as IntArray
    }

    @JvmStatic
    fun getNbtMap(nbtBase: Any): java.util.Map<String, Any> {
        return script.simpleInvoke("nbtMap", nbtBase) as java.util.Map<String, Any>
    }

    @JvmStatic
    fun getNbtSize(nbtBase: Any): Int {
        return script.simpleInvoke("nbtSize", nbtBase) as Int
    }

    @JvmStatic
    fun getNBTType(nbtBase: Any): ItemTagType {
        return script.simpleInvoke("getNBTType", nbtBase) as ItemTagType
    }
}