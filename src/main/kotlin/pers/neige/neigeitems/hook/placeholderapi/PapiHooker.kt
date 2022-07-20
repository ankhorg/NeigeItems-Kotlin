package pers.neige.neigeitems.hook.placeholderapi

import org.bukkit.OfflinePlayer

// 本部分相关API暴露于HookManager
abstract class PapiHooker {
    // 解析一段文本中的papi变量, 不解析其中的颜色代码
    // 在以往的众多版本中, papi都会强制解析文本中的代码
    // 在2.11.2版本中, papi移除了该功能
    // 但管他呢, 自己实现就完事儿了
    abstract fun papi(player: OfflinePlayer, text: String): String
}