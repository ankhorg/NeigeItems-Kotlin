// 文件名不重要, 写成啥都行
// main函数会自动执行
function main() {
    // 导入相应的类, 这两行看不懂的话直接抄就行
    const ItemEditorManager = Packages.pers.neige.neigeitems.manager.ItemEditorManager.INSTANCE

    // 这是我写这段代码用到的类, 不是每次添加自定义物品编辑函数都要用到
    const ArrayList = Packages.java.util.ArrayList
    const ChatColor = Packages.org.bukkit.ChatColor
    const Material = Packages.org.bukkit.Material

    // 添加自定义物品编辑函数
    // 这里我添加了一个名为"test"的物品编辑函数, 但实际上它的功能与addLore函数相同
    ItemEditorManager.addItemEditor(
        // 函数名
        "test",
        /**
         * 物品编辑函数
         * @param player Player 物品拥有者
         * @param itemStack ItemStack 待编辑物品
         * @param content String 传入的文本
         */
        function(player, itemStack, content) {
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                const itemMeta = itemStack.itemMeta
                if (itemMeta != null) {
                    // 获取并设置lore
                    let lore = itemMeta.lore
                    if (lore == null) lore = new ArrayList()
                    lore.addAll(ChatColor.translateAlternateColorCodes('&', content).split("\\n"))
                    itemMeta.lore = lore
                    // 将改动完成的itemMeta设置回去
                    itemStack.setItemMeta(itemMeta)
                    // 物品编辑都需要返回一个布尔量, 判断你是否编辑成功
                    return true
                }
            }
            // 物品编辑都需要返回一个布尔量, 判断你是否编辑成功
            return false
        }
    )
}
