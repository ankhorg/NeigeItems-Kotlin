function main() {
    const SectionManager = Packages.pers.neige.neigeitems.manager.SectionManager.INSTANCE
    const CustomSection = Packages.pers.neige.neigeitems.section.impl.CustomSection
    const Section = Packages.pers.neige.neigeitems.section.Section
    const SectionUtils = Packages.pers.neige.neigeitems.utils.SectionUtils
    const String = Packages.java.lang.String
    const BigDecimal = Packages.java.math.BigDecimal
    const HashMap = Packages.java.util.HashMap
    const Bukkit = Packages.org.bukkit.Bukkit
    const PluginManager = Bukkit.getPluginManager()
    const PlaceholderAPI = Packages.me.clip.placeholderapi.PlaceholderAPI

    // papi节点的js实现, 经实验会给性能带来些微负面影响
    // if (PluginManager.isPluginEnabled("PlaceholderAPI")) {
    //     const placeholderAPI = PluginManager.getPlugin("PlaceholderAPI")
    //     papi = function(player, text) {
    //         let builder = ""
    //         let identifier = ""
    //         let parameters = ""
    //         for (let i = 0; i < text.length; i++) {
    //             let l = text[i]
    //             if (l != "%" || i + 1 >= text.length) {
    //             builder += l
    //             continue
    //             }
    //             let identified,oopsitsbad,hadSpace = false
    //             while (++i < text.length) {
    //                 let p = text[i]
    //                 if (p == ' ' && !identified) {
    //                     hadSpace = true
    //                     break
    //                 }
    //                 if (p == "%") {
    //                     oopsitsbad = true
    //                     break
    //                 }
    //                 if (p == '_' && !identified) {
    //                     identified = true
    //                     continue
    //                 }
    //                 if (identified) {
    //                     parameters += p
    //                 } else {
    //                     identifier += p
    //                 }
    //             }
    //             let identifierString = identifier
    //             let lowercaseIdentifierString = identifierString.toLowerCase()
    //             let parametersString = parameters
    //             identifier = ""
    //             parameters = ""
    //             if (!oopsitsbad) {
    //                 builder += "%" + identifierString
    //                 if (identified) builder += '_' + parametersString
    //                 if (hadSpace) builder += ' '
    //                 continue
    //             }
    //             let placeholder
    //             if (placeholderAPI.getLocalExpansionManager != undefined) {
    //                 placeholder = placeholderAPI.getLocalExpansionManager().getExpansion(lowercaseIdentifierString)
    //             } else {
    //                 placeholder = PlaceholderAPI.getPlaceholders().get(lowercaseIdentifierString)
    //             }
    //             if (placeholder == undefined) {
    //                 builder += "%" + lowercaseIdentifierString
    //                 if (identified) builder += '_'
    //                 builder += parametersString + "%"
    //                 continue
    //             }
    //             let replacement = placeholder.onRequest(player, parametersString)
    //             if (replacement == null) {
    //                 builder += "%" + lowercaseIdentifierString
    //                 if (identified) builder += '_'
    //                 builder += parametersString + "%"
    //                 continue
    //             }
    //             builder += replacement
    //         }
    //         return builder
    //     }
    // } else {
    //     papi = function(player, text) { return text }
    // }
    // SectionManager.loadParser(new CustomSection(
    //     "papi",
    //     function(data, cache, player, sections) {
    //         return null
    //     },
    //     function(args, cache, player, sections) {
    //         if (player != null) {
    //             return SectionUtils.parseSection(papi(player, "%" + String.join("_", args) + "%"), cache, player, sections)
    //         } else {
    //             return "<papi::"+ String.join("_", args) +">"
    //         }
    //     }))

    // 计算节点的js实现, 经实验性能可能有些许提升(存在波动性)
    // SectionManager.loadParser(new CustomSection(
    //     "calculation",
    //     function(data, cache, player, sections) {
    //         return calculationHandler(cache, player, sections, true, [data.getString("formula"), data.getString("fixed"), data.getString("min"), data.getString("max")])
    //     },
    //     function(args, cache, player, sections) {
    //         return calculationHandler(cache, player, sections, false, args) || "<calculation::"+ String.join("_", args) +">"
    //     }))
    // function calculationHandler(cache, player, sections, parse, args) {
    //     try {
    //         // 加载公式
    //         let formula = getOrNull(args, 0)
    //         if (formula != null) {
    //             formula = SectionUtils.parseSection(formula, parse, cache, player, sections)
    //             if (formula != null) {
    //                 // 计算结果
    //                 let result = eval(formula)
    //                 const min = getOrNull(args, 2)
    //                 if (min != null) {
    //                     result = Math.max(min, result)
    //                 }
    //                 const max = getOrNull(args, 3)
    //                 if (max != null) {
    //                     result = Math.min(max, result)
    //                 }
    //                 let fixed = getOrNull(args, 1)
    //                 if (fixed != null) {
    //                     fixed = parseInt(SectionUtils.parseSection(fixed, parse, cache, player, sections))
    //                     if (isNaN(fixed)) fixed = 0
    //                 } else {
    //                     fixed = 0
    //                 }
    //                 return result.toFixed(fixed)
    //             }
    //         }
    //     } catch (error) {
    //         error.printStackTrace()
    //     }
    //     return null
    // }

    // 继承节点的js实现, 经实验性能无差异
    // SectionManager.loadParser(new CustomSection(
    //     "inherit",
    //     function(data, cache, player, sections) {
    //         return inheritHandler(cache, player, sections, true, data.getString("template"))
    //     },
    //     function(args, cache, player, sections) {
    //         return inheritHandler(cache, player, sections, false, String.join("_", args)) || "<inherit::"+ String.join("_", args) +">"
    //     }))
    // function inheritHandler(cache, player, sections, parse, template) {
    //     if (template != null) {
    //         if (sections != null) {
    //             const section = sections.getConfigurationSection(template)
    //             if (section == null) {
    //                 const info = sections.getString(template)
    //                 if (info != null) {
    //                     return SectionUtils.parseSection(info, parse, cache, player, sections)
    //                 }
    //             } else {
    //                 return new Section(section, template).get(cache, player, sections)
    //             }
    //         }
    //     }
    //     return null
    // }

    // 随机数节点的js实现, 经实验带来了约13%的性能提升
    // SectionManager.loadParser(new CustomSection(
    //     "number",
    //     function(data, cache, player, sections) {
    //         return numberHandler(cache, player, sections, true, [data.getString("min") ,data.getString("max"), data.getString("fixed")])
    //     },
    //     function(args, cache, player, sections) {
    //         return numberHandler(cache, player, sections, false, args) || "<number::"+ String.join("_", args) +">"
    //     }))
    // function numberHandler(cache, player, sections, parse, args) {
    //     let min = getOrNull(args, 0)
    //     let max = getOrNull(args, 1)
    //     if (min != null && max != null) {
    //         min = parseFloat(SectionUtils.parseSection(min, parse, cache, player, sections))
    //         max = parseFloat(SectionUtils.parseSection(max, parse, cache, player, sections))
    //         let fixed = getOrNull(args, 2)
    //         if (fixed != null) {
    //             fixed = parseInt(SectionUtils.parseSection(fixed, parse, cache, player, sections))
    //         } else {
    //             fixed = 0
    //         }
    //         if (!isNaN(min) && !isNaN(max)) {
    //             return (min+(Math.random()*(max-min))).toFixed(fixed)
    //         }
    //     }
    //     return null
    // }

    // 字符串节点的js实现, 经实验会给性能带来负面影响
    // SectionManager.loadParser(new CustomSection(
    //     "strings",
    //     function(data, cache, player, sections) {
    //         return stringsHandler(cache, player, sections, true, data.getStringList("values"))
    //     },
    //     function(args, cache, player, sections) {
    //         return stringsHandler(cache, player, sections, false, args) || "<strings::"+ String.join("_", args) +">"
    //     }))
    // function stringsHandler(cache, player, sections, parse, values) {
    //     if (values.isEmpty()) {
    //         return null
    //     } else {
    //         return SectionUtils.parseSection(values[parseInt(Math.random()*(values.length))], parse, cache, player, sections)
    //     }
    // }

    // 权重节点的js实现, 经实验会给性能带来负面影响
    // SectionManager.loadParser(new CustomSection(
    //     "weight",
    //     function(data, cache, player, sections) {
    //         return weightHandler(cache, player, sections, true, data.getStringList("values"))
    //     },
    //     function(args, cache, player, sections) {
    //         return weightHandler(cache, player, sections, false, args) || "<weight::"+ String.join("_", args) +">"
    //     }))
    // function weightHandler(cache, player, sections, parse, values) {
    //     const info = new HashMap()
    //     let total = new BigDecimal(0)
    //     // 加载所有参数并遍历
    //     for (let i = 0; i < values.length; i++) {
    //         const it = values[i]
    //         const value = SectionUtils.parseSection(it, parse, cache, player, sections)
    //         // 检测权重
    //         const index = value.indexOf("::")
    //         if (index == -1) {
    //             const it = info[value]
    //             if (it != undefined) {
    //                 info[value] = it.add(new BigDecimal(1))
    //             } else {
    //                 info[value] = new BigDecimal(1)
    //             }
    //             total = total.add(new BigDecimal(1))
    //         } else {
    //             const weight = parseInt(value.slice(0, index)) || 1
    //             const string = value.slice(index+2)
    //             const it = info[value]
    //             if (it != undefined) {
    //                 info[string] = it.add(new BigDecimal(weight))
    //             } else {
    //                 info[string] = new BigDecimal(weight)
    //             }
    //             total = total.add(new BigDecimal(weight))
    //         }
    //     }
    //     // 根据最后的记录值进行字符随机
    //     if (!info.isEmpty()) {
    //         const random = new BigDecimal(Math.random().toString()).multiply(total)
    //         let current = new BigDecimal(0)
    //         info.forEach(function(key, value) {
    //             current = current.add(value)
    //             if (random <= current) {
    //                 return SectionUtils.parseSection(key, parse, cache, player, sections)
    //             }
    //         })
    //     } else {
    //         return null
    //     }
    // }
}

function getOrNull(arrayList, index) {
    if (index >= 0 && index <= arrayList.length) {
        return arrayList[index]
    } else {
        return null
    }
}
