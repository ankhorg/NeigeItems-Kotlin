package pers.neige.neigeitems.hook.placeholderapi

import org.bukkit.OfflinePlayer

/**
 * PlaceholderAPI挂钩
 */
abstract class PapiHooker {
    /**
     * 解析一段文本中的papi变量, 不解析其中的颜色代码
     * 在以往的众多版本中, papi都会强制解析文本中的代码
     * 在2.11.2版本中, papi移除了该功能
     * 不过, 管他呢, 自己实现就完事儿了
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param text 待解析文本
     * @return 解析后文本
     */
    abstract fun papi(player: OfflinePlayer, text: String): String
}