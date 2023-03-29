// 文件名不重要, 写成啥都行
// enable函数会自动执行
function enable() {
    // 导入相应的类, 这两行看不懂的话直接抄就行
    const ActionManager = Packages.pers.neige.neigeitems.manager.ActionManager.INSTANCE
    const SectionUtils = Packages.pers.neige.neigeitems.utils.SectionUtils

    // 插入新的自定义动作
    ActionManager.addAction(
        // 动作名称
        "test",
        // 动作内容(一般是异步调用的, 所以需要同步执行的内容需要自行同步)
        function(player, string) {
            // 调用动作
            ActionManager.runAction(player, "tell: 123")
            ActionManager.runAction(player, "tell: 456")
            player.sendMessage(SectionUtils.parseSection("<number::0_10_2>"))
            // 每个动作都一定要返回一个布尔量(true或false), 返回false相当于终止一连串动作的执行
            return true
        })
}
