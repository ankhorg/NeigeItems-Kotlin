// 文件名不重要, 写成啥都行
// enable函数会自动执行
function enable() {
    // 导入相应的类, 看不懂的话直接抄就行
    const SectionManager = Packages.pers.neige.neigeitems.manager.SectionManager.INSTANCE
    const CustomSection = Packages.pers.neige.neigeitems.section.impl.CustomSection

    // 节点注册
    SectionManager.loadParser(
        new CustomSection(
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
                if (data.contains("values")) {
                    // SectionUtils.parseSection("待解析字符串", cache, player, sections)用于解析节点内容
                    return SectionUtils.parseSection("<number::0_1_2>", cache, player, sections)
                }
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
                return SectionUtils.parseSection("<number::0_1_2>", cache, player, sections)
            }
        )
    )
}
