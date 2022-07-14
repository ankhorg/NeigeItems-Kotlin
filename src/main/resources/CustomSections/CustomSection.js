// 文件名不重要, 写成啥都行
// main类会自动执行
function main() {
    // 导入相应的类, 这两行看不懂的话直接抄就行
    const SectionManager = Packages.pers.neige.neigeitems.manager.SectionManager.INSTANCE
    const CustomSection = Packages.pers.neige.neigeitems.section.impl.CustomSection

    // 创建自定义节点
    const customSection = new CustomSection(
        // 节点id
        "test",
        /**
         * 用于私有节点解析
         * @param data ConfigurationSection 节点内容
         * @param cache HashMap<String, String>? 解析值缓存
         * @param player OfflinePlayer? 待解析玩家
         * @param sections ConfigurationSection? 节点池
         * @return 解析值
         */
        function(data, cache, player, sections) {
            if (data.contains("values")) return data.getStringList("values").size() + ""
            return null
        },
        /**
         * 用于即时节点解析
         * @param args List<String> 节点参数
         * @param cache HashMap<String, String>? 解析值缓存
         * @param player OfflinePlayer? 待解析玩家
         * @param sections ConfigurationSection? 节点池
         * @return 解析值
         */
        function(args, cache, player, sections) {
            return args.length + ""
        })
    // 节点注册
    SectionManager.loadParser(customSection)
}
