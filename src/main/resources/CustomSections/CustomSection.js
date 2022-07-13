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
        // 用于解析私有节点
        function(data, cache, player, sections) {
            if (data.containsKey("values")) return data["values"].length + ""
            return null
        },
        // 用于解析即时节点
        function(args, cache, player, sections) {
            return args.length + ""
        })
    // 节点注册
    SectionManager.loadParser(customSection)
}
