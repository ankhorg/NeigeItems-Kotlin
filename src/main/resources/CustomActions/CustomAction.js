// 文件名不重要, 写成啥都行
// main函数会自动执行
function main() {
    // 导入相应的类, 这两行看不懂的话直接抄就行
    const ActionManager = Packages.pers.neige.neigeitems.manager.ActionManager.INSTANCE
    const bukkitScheduler = Packages.org.bukkit.Bukkit.getScheduler()
    const pluginManager = Packages.org.bukkit.Bukkit.getPluginManager()

    // 插入新的自定义动作
    ActionManager.addAction(
        // 动作名称
        "test",
        // 动作内容(一般是异步调用的, 所以需要同步执行的内容需要自行同步)
        function(player, string) {
            player.sendMessage("1233211234567")
            // 每个动作都一定要返回一个布尔量(true或false)
            return true
        })
}
