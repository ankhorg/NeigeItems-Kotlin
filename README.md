# NeigeItems

A bukkit item manage plugin.

Javadoc: https://ankhorg.github.io/NeigeItems-Kotlin/

Wiki付费, 99元永久, 如欲购买请添加作者QQ: 2468629609

## Development

```kotlin
repositories {
  maven("https://r.irepo.space/maven/")
}

dependencies {
  compileOnly("pers.neige.neigeitems:NeigeItems:[latest release version]")
}
```

## bStats

![](https://bstats.org/signatures/bukkit/NeigeItems.svg)

## API

### 获取物品

```java
ItemStack itemStack = ItemManager.INSTANCE.getItemStack(itemId, player, data);
```

### 获取物品包

```java
ItemPack itemPack = ItemPackManager.INSTANCE.getItemPack(packId);
if (itemPack == null) return;
List<ItemStack> itemStacks = itemPack.getItemStacks(player, data);
```

### 获取NI物品ID

```java
String itemId = ItemUtils.getItemId(itemStack);
if (itemId == null) return;
```

### 获取NI物品ID及物品节点信息

```java
ItemInfo itemInfo = ItemUtils.isNiItem(itemStack);
if (itemInfo == null) return;
```

### 我想在我的插件里使用NI的动作系统

```java
// 继承一个BaseActionManager
public class ActionManager extends BaseActionManager {
    public ActionManager(@NotNull Plugin plugin) {
        super(plugin);
        // 加载一下NI里一些已有的js库
        // 这些库将反映在condition判断和js动作执行中
        loadJSLib("NeigeItems", "JavaScriptLib/lib.js");
    }
}
```

```java
// 插件里new一个ActionManager出来, new的时候把插件实例传进去
public class MyPlugin extends JavaPlugin {
    private static MyPlugin INSTANCE;
    private static ActionManager actionManager = null;

    public static MyPlugin getInstance() {
        return INSTANCE;
    }

    public static ActionManager getActionManager() {
        return actionManager;
    }

    public MyPlugin() {
        super();
        INSTANCE = this;
        actionManager = new ActionManager(this);
    }

    public MyPlugin(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
        INSTANCE = this;
        actionManager = new ActionManager(this);
    }
}
```

```java
// 动作是需要编译的
// config就是Bukkit的ConfigurationSection
Action action = MyPlugin.getActionManager().compile(config.get("xxx"));
// 动作的执行是需要提供一个上下文的
// 如果需要传入玩家, 就 new ActionContext(player)
// 如果你需要向condition传入其他变量，就把变量丢进一个Map里，作为params丢进去
// 如果你需要这些变量可以在非JS动作中以<变量名>的形式访问，就作为把这个Map同时作为global丢进去
ActionContext context = new ActionContext();

// ActionContext context = new ActionContext(player);

// Map<String, Object> params = new HashMap<>();
// params.put("test", "测试参数");

// 第一个参数是global, 第二个参数是params
// ActionContext context = new ActionContext(player, null, params);
// 条件示例 condition: 'test == "测试参数"'

// ActionContext context = new ActionContext(player, params, null);
// 条件示例 condition: 'global.test == "测试参数"'
// 动作示例 'tell: 参数测试<test>'

// ActionContext context = new ActionContext(player, params, params);
// 条件示例 condition: 'test == "测试参数"'
// 条件示例 condition: 'global.test == "测试参数"'
// 动作示例 'tell: 参数测试<test>'

// 动作执行
MyPlugin.getActionManager().runAction(action, context);
```

### 我想通过NI获取MM的怪物配置

```java
MythicMobsHooker hooker = HookerManager.INSTANCE.getMythicMobsHooker();
if (hooker == null) return;
// key是怪物ID, value是怪物配置
Map<String, ConfigurationSection> mobInfos = hooker.getMobInfos();
```

MM重载时NI会尝试重新加载怪物配置并触发MobInfoReloadedEvent
