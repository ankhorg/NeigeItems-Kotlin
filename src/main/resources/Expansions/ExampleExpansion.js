/**
 * 服务器开启后 及 ni reload后异步执行
 */
function enable() {
    // 调用指令注册
    commandExample()
    // 调用监听器注册
    listenerExample()
    // 调用PAPI变量注册
    placeholderExample()
    // 调用Bukkit任务
    taskExample()
}

/**
 * 服务器关闭前同步执行 及 ni reload前异步执行
 */
function disable() {
}

/**
 * 服务器开启后异步执行
 */
function serverEnable() {
}

/**
 * 服务器关闭前同步执行
 */
function serverDisable() {
}

/**
 * 指令注册示例
 */
function commandExample() {
    /**
     * 新建一个名为test的指令
     * 你需要在最后通过.register()注册它
     * 在ni reload前, 所有脚本执行过disable后, 所有通过脚本注册的指令都会自动取消注册
     * 因此你需要注册就好, 不用担心后续的事情
     * 插件内部已经做过同步处理, 因此你可以放心异步执行注册
     */
    new Command("test")
        // 设置指令命名空间, 默认命名空间为指令名(test:test), 当前设置后可通过"test"或"neige:test"执行指令
        .setNameSpace("neige")
        // 我真不知道label是啥, 这玩意儿默认是跟指令名一样的
        .setLabel("test")
        // 指令别名, test指令可以用tt或ttt代替
        .setAliases(["tt", "ttt"])
        // 指令所需权限
        .setPermission("tt.command")
        // 权限不足时的提示
        .setPermissionMessage("权限不足")
        // 指令用法
        .setUsage("用来测试的指令")
        // 指令描述
        .setDescription("用来测试的指令")
        /**
         * 指令执行器
         *
         * @param sender CommandSender 指令执行者(Player/ConsoleCommandSender)
         * @param command Command 指令本身
         * @param label
         * @param args 指令参数
         * @return Boolean 指令是否执行成功
         */
        .setExecutor(function (sender, command, label, args) {
            sender.sendMessage("测试指令")
            return true
        })
        /**
         * 指令补全器
         *
         * @param sender CommandSender 指令执行者(Player/ConsoleCommandSender)
         * @param command Command 指令本身
         * @param label
         * @param args 指令参数
         * @return List<String> 补全内容
         */
        .setTabCompleter(function (sender, command, label, args) {
            return ["测试补全"]
        })
    /**
     * 注册指令
     * 为了防止默认配置给你带来困扰, 我默认不进行注册
     * 你想实验的话删掉前面的"// "就好, 那是注释符号
     */
    // .register()
}

/**
 * 监听器注册示例
 */
function listenerExample() {
    /**
     * 新建一个AsyncPlayerChatEvent事件的监听器
     * 你需要在最后通过.register()注册它
     * 在ni reload前, 所有脚本执行过disable后, 所有通过脚本注册的监听器都会自动取消注册
     * 因此你需要注册就好, 不用担心后续的事情
     * 插件内部已经做过同步处理, 因此你可以放心异步执行注册
     */
    new Listener(Packages.org.bukkit.event.player.AsyncPlayerChatEvent.class)
        /**
         * 事件监听优先级, 默认为EventPriority.NORMAL(所以这行实际上不用写)
         * 可用的优先级有:
         * EventPriority.LOWEST (最先处理)
         * EventPriority.LOW
         * EventPriority.NORMAL
         * EventPriority.HIGH
         * EventPriority.HIGHEST
         * EventPriority.MONITOR (最后处理)
         */
        .setPriority(EventPriority.NORMAL)
        // 是否忽略已取消事件, 默认为true(所以这行实际上不用写)
        .setIgnoreCancelled(true)
        /**
         * 事件处理器
         *
         * @param event 你监听的事件
         */
        .setExecutor(function (event) {
            event.player.sendMessage(
                '你是不是打算说: " ' + event.message + ' " ?'
            )
        })
    /**
     * 注册监听器
     * 为了防止默认配置给你带来困扰, 我默认不进行注册
     * 你想实验的话删掉前面的"// "就好, 那是注释符号
     */
    // .register()
}

/**
 * PAPI变量注册示例
 */
function placeholderExample() {
    /**
     * 新建一个名为test的papi变量
     * 你需要在最后通过.register()注册它
     * 在ni reload前, 所有脚本执行过disable后, 所有通过脚本注册的papi变量都会自动取消注册
     * 因此你需要注册就好, 不用担心后续的事情
     * 插件内部已经做过同步处理, 因此你可以放心异步执行注册
     * 插件内部已经做过兼容处理, 因此不安装PlaceholderAPI不会导致这段代码报错, 只会让它不生效
     */
    new Placeholder("test")
        // 设置变量作者, 默认为"unknown"(所以这行实际上不用写)
        .setAuthor("Neige")
        // 设置变量版本, 默认为"1.0.0"(所以这行实际上不用写)
        .setVersion("1.0.0")
        /**
         * 变量处理器
         *
         * @param player OfflinePlayer 当前玩家
         * @param params String 变量参数
         * @return String 变量返回值
         */
        .setExecutor(function (player, params) {
            // %test% 返回玩家名
            return player.name
        })
    /**
     * 注册PAPI变量
     * 为了防止默认配置给你带来困扰, 我默认不进行注册
     * 你想实验的话删掉前面的"// "就好, 那是注释符号
     */
    // .register()
}

/**
 * Bukkit任务注册示例
 */
function taskExample() {
    /**
     * 新建一个Bukkit任务
     * 你需要在最后通过.register()注册它
     * 在ni reload前, 所有脚本执行过disable后, 所有通过脚本注册的Bukkit任务都会自动取消注册
     * 因此你需要注册就好, 不用担心后续的事情
     * 插件内部已经做过同步处理, 因此你可以放心异步执行注册
     */
    new Task()
        // 设置任务中你要执行的代码
        .setTask(function () {
            print(Bukkit.isPrimaryThread())
        })
        // 设置任务执行间隔(不设置的话任务只会执行一次)
        .setPeriod(10)
        // 设置任务执行延迟(不设置的话任务会立即执行)
        .setDelay(10)
        // 设置任务是否异步执行(不设置的话任务将同步进行)
        .setAsync(true)
    /**
     * 注册Bukkit任务
     * 为了防止默认配置给你带来困扰, 我默认不进行注册
     * 你想实验的话删掉前面的"// "就好, 那是注释符号
     */
    // .register()
}
