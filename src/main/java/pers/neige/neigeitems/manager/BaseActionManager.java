package pers.neige.neigeitems.manager;

import kotlin.text.StringsKt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ResultType;
import pers.neige.neigeitems.action.impl.*;
import pers.neige.neigeitems.action.result.DelayResult;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker;
import pers.neige.neigeitems.hook.vault.VaultHooker;
import pers.neige.neigeitems.item.ItemInfo;
import pers.neige.neigeitems.item.action.ComboInfo;
import pers.neige.neigeitems.utils.ItemUtils;
import pers.neige.neigeitems.utils.PlayerUtils;
import pers.neige.neigeitems.utils.SchedulerUtils;
import pers.neige.neigeitems.utils.StringUtils;

import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static pers.neige.neigeitems.utils.ListUtils.*;

public abstract class BaseActionManager {
    @NotNull
    private final Plugin plugin;
    /**
     * 物品动作实现函数
     */
    @NotNull
    private final HashMap<String, BiFunction<ActionContext, String, ActionResult>> actions = new HashMap<>();
    /**
     * 用于编译condition的脚本引擎
     */
    @NotNull
    private final ScriptEngine engine = HookerManager.INSTANCE.getNashornHooker().getGlobalEngine();
    /**
     * 缓存的已编译condition脚本
     */
    @NotNull
    private final ConcurrentHashMap<String, CompiledScript> conditionScripts = new ConcurrentHashMap<>();
    /**
     * 缓存的已编译action脚本
     */
    @NotNull
    private final ConcurrentHashMap<String, CompiledScript> actionScripts = new ConcurrentHashMap<>();

    public BaseActionManager(
            @NotNull Plugin plugin
    ) {
        this.plugin = plugin;
        // 加载基础物品动作
        loadBasicActions();
    }

    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }

    @NotNull
    public HashMap<String, BiFunction<ActionContext, String, ActionResult>> getActions() {
        return actions;
    }

    @NotNull
    public ScriptEngine getEngine() {
        return engine;
    }

    @NotNull
    public ConcurrentHashMap<String, CompiledScript> getConditionScripts() {
        return conditionScripts;
    }

    @NotNull
    public ConcurrentHashMap<String, CompiledScript> getActionScripts() {
        return actionScripts;
    }

    public void reload() {
        conditionScripts.clear();
        actionScripts.clear();
    }

    @NotNull
    public Action compile(
            @Nullable Object action
    ) {
        if (action instanceof String) {
            return new StringAction((String) action);
        } else if (action instanceof List<?>) {
            return new ListAction(this, (List<?>) action);
        } else if (action instanceof Map<?, ?>) {
            if (((Map<?, ?>) action).containsKey("condition")) {
                return new ConditionAction(this, (Map<?, ?>) action);
            } else if (((Map<?, ?>) action).containsKey("while")) {
                return new WhileAction(this, (Map<?, ?>) action);
            }
        } else if (action instanceof ConfigurationSection) {
            if (((ConfigurationSection) action).contains("condition")) {
                return new ConditionAction(this, (ConfigurationSection) action);
            } else if (((ConfigurationSection) action).contains("while")) {
                return new WhileAction(this, (ConfigurationSection) action);
            }
        }
        return NullAction.INSTANCE;
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @return 执行结果
     */
    @NotNull
    public ActionResult runAction(
            @NotNull Action action
    ) {
        return runAction(action, ActionContext.empty());
    }

    /**
     * 执行物品动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @NotNull
    public ActionResult runAction(
            @NotNull Action action,
            @NotNull ActionContext context
    ) {
        return action.eval(this, context);
    }

    /**
     * 执行物品动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @NotNull
    public ActionResult runAction(
            @NotNull StringAction action,
            @NotNull ActionContext context
    ) {
        BiFunction<ActionContext, String, ActionResult> handler = actions.get(action.getKey());
        if (handler != null) {
            return handler.apply(context, action.getContent());
        } else {
            return Results.SUCCESS;
        }
    }

    /**
     * 执行物品动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @NotNull
    public ActionResult runAction(
            @NotNull ListAction action,
            @NotNull ActionContext context
    ) {
        List<Action> actions = action.getActions();
        for (int index = 0; index < actions.size(); index++) {
            Action value = actions.get(index);
            ActionResult result = value.eval(this, context);
            switch (result.getType()) {
                case DELAY: {
                    int fromIndex = index + 1;
                    int toIndex = actions.size();
                    SchedulerUtils.runLater(plugin, ((DelayResult) result).getDelay(), () -> runAction(action.subList(fromIndex, toIndex), context));
                    return Results.SUCCESS;
                }
                case STOP: {
                    return result;
                }
            }
        }
        return Results.SUCCESS;
    }

    /**
     * 执行物品动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @NotNull
    public ActionResult runAction(
            @NotNull ConditionAction action,
            @NotNull ActionContext context
    ) {
        // 如果条件通过
        if (parseCondition(action.getCondition(), context).getType() == ResultType.SUCCESS) {
            // 执行动作
            Action.eval(action.getSync(), action.getAsync(), this, context);
            return action.getActions().eval(this, context);
            // 条件未通过
        } else {
            // 执行deny动作
            return action.getDeny().eval(this, context);
        }
    }

    /**
     * 执行物品动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @NotNull
    public ActionResult runAction(
            @NotNull WhileAction action,
            @NotNull ActionContext context
    ) {
        // while循环判断条件
        while (parseCondition(action.getCondition(), context).getType() == ResultType.SUCCESS) {
            // 执行动作
            Action.eval(action.getSync(), action.getAsync(), this, context);
            ActionResult result = action.getActions().eval(this, context);
            // 执行中止
            if (result.getType() == ResultType.STOP) {
                action.getFinally().eval(this, context);
                return Results.STOP;
            }
        }
        // 执行finally块
        action.getFinally().eval(this, context);
        return Results.SUCCESS;
    }

    /**
     * 解析条件
     *
     * @param condition 条件内容
     * @param context   动作上下文
     * @return 执行结果
     */
    @NotNull
    public ActionResult parseCondition(
            @Nullable String condition,
            @NotNull ActionContext context
    ) {
        if (condition == null) {
            return Results.SUCCESS;
        }
        Object result;
        try {
            result = conditionScripts.computeIfAbsent(condition, (key) -> HookerManager.INSTANCE.getNashornHooker().compile(engine, key)).eval(context.getBindings());
            if (result == null) {
                return Results.STOP;
            }
        } catch (Throwable error) {
            plugin.getLogger().warning("条件解析异常, 条件内容如下:");
            for (String conditionLine : condition.split("\n")) {
                plugin.getLogger().warning(conditionLine);
            }
            error.printStackTrace();
            return Results.STOP;
        }
        if (result instanceof ActionResult) {
            return (ActionResult) result;
        } else if (result instanceof Boolean) {
            return Results.fromBoolean((Boolean) result);
        } else {
            return Results.STOP;
        }
    }

    /**
     * 添加物品动作
     *
     * @param id       动作ID
     * @param function 动作执行函数
     */
    public void addFunction(
            @NotNull String id,
            @NotNull BiFunction<ActionContext, String, ActionResult> function
    ) {
        actions.put(id.toLowerCase(Locale.ROOT), function);
    }

    /**
     * 添加物品动作
     *
     * @param id       动作ID
     * @param function 动作执行函数
     */
    public void addConsumer(
            @NotNull String id,
            @NotNull BiConsumer<ActionContext, String> function
    ) {
        actions.put(id.toLowerCase(Locale.ROOT), (context, content) -> {
            function.accept(context, content);
            return Results.SUCCESS;
        });
    }

    /**
     * 加载基础物品动作
     */
    protected void loadBasicActions() {
        // 向玩家发送消息
        addConsumer("tell", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.sendMessage(HookerManager.papiColor(player, content));
        });
        // 向玩家发送消息(不将&解析为颜色符号)
        addConsumer("tellNoColor", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.sendMessage(HookerManager.papi(player, content));
        });
        // 强制玩家发送消息
        addConsumer("chat", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.chat(HookerManager.papi(player, content)));
        });
        // 强制玩家发送消息(将&解析为颜色符号)
        addConsumer("chatWithColor", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.chat(HookerManager.papiColor(player, content)));
        });
        // 强制玩家执行指令
        addConsumer("command", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> Bukkit.dispatchCommand(player, HookerManager.papiColor(player, content)));
        });
        // 强制玩家执行指令
        addFunction("player", actions.get("command"));
        // 强制玩家执行指令(不将&解析为颜色符号)
        addConsumer("commandNoColor", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> Bukkit.dispatchCommand(player, HookerManager.papi(player, content)));
        });
        // 后台执行指令
        addConsumer("console", (context, content) -> {
            Player player = context.getPlayer();
            if (player != null) {
                SchedulerUtils.sync(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), HookerManager.papiColor(player, content)));
            } else {
                SchedulerUtils.sync(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', content)));
            }
        });
        // 后台执行指令(不将&解析为颜色符号)
        addConsumer("consoleNoColor", (context, content) -> {
            Player player = context.getPlayer();
            if (player != null) {
                SchedulerUtils.sync(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), HookerManager.papi(player, content)));
            } else {
                SchedulerUtils.sync(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), content));
            }
        });
        // 公告
        addConsumer("broadcast", (context, content) -> {
            Player player = context.getPlayer();
            if (player != null) {
                Bukkit.broadcastMessage(HookerManager.papiColor(player, content));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', content));
            }
        });
        // 公告(不将&解析为颜色符号)
        addConsumer("broadcastNoColor", (context, content) -> {
            Player player = context.getPlayer();
            if (player != null) {
                Bukkit.broadcastMessage(HookerManager.papi(player, content));
            } else {
                Bukkit.broadcastMessage(content);
            }
        });
        // 发送Title
        addConsumer("title", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> {
                ArrayList<String> args = StringUtils.split(HookerManager.papiColor(player, content), ' ', '\\');
                String title = getOrNull(args, 0);
                String subtitle = getOrDefault(args, 1, "");
                int fadeIn = getAndApply(args, 2, 10, StringsKt::toIntOrNull);
                int stay = getAndApply(args, 3, 70, StringsKt::toIntOrNull);
                int fadeOut = getAndApply(args, 4, 20, StringsKt::toIntOrNull);
                player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            });
        });
        // 发送Title(不将&解析为颜色符号)
        addConsumer("titleNoColor", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> {
                ArrayList<String> args = StringUtils.split(HookerManager.papi(player, content), ' ', '\\');
                String title = getOrNull(args, 0);
                String subtitle = getOrDefault(args, 1, "");
                int fadeIn = getAndApply(args, 2, 10, StringsKt::toIntOrNull);
                int stay = getAndApply(args, 3, 70, StringsKt::toIntOrNull);
                int fadeOut = getAndApply(args, 4, 20, StringsKt::toIntOrNull);
                player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            });
        });
        // 发送ActionBar
        addConsumer("actionBar", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> PlayerUtils.sendActionBar(player, HookerManager.papiColor(player, content)));
        });
        // 发送ActionBar(不将&解析为颜色符号)
        addConsumer("actionBarNoColor", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> PlayerUtils.sendActionBar(player, HookerManager.papi(player, content)));
        });
        // 播放音乐
        addConsumer("sound", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            String[] args = content.split(" ", 3);
            String sound = getOrDefault(args, 0, "");
            float volume = getAndApply(args, 1, 1F, StringsKt::toFloatOrNull);
            float pitch = getAndApply(args, 2, 1F, StringsKt::toFloatOrNull);
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
        // 给予玩家金钱
        addConsumer("giveMoney", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            VaultHooker hooker = HookerManager.INSTANCE.getVaultHooker();
            if (hooker != null) {
                hooker.giveMoney(player, StringUtils.toDouble(HookerManager.papi(player, content), 0.0));
            }
        });
        // 扣除玩家金钱
        addConsumer("takeMoney", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            VaultHooker hooker = HookerManager.INSTANCE.getVaultHooker();
            if (hooker != null) {
                hooker.takeMoney(player, StringUtils.toDouble(HookerManager.papi(player, content), 0.0));
            }
        });
        // 给予玩家经验
        addConsumer("giveExp", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.giveExp(StringUtils.toInt(HookerManager.papi(player, content), 0)));
        });
        // 扣除玩家经验
        addConsumer("takeExp", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.giveExp(StringUtils.toInt(HookerManager.papi(player, content), 0) * -1));
        });
        // 设置玩家经验
        addConsumer("setExp", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setTotalExperience(StringUtils.toInt(HookerManager.papi(player, content), 0)));
        });
        // 给予玩家经验等级
        addConsumer("giveLevel", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.giveExpLevels(StringUtils.toInt(HookerManager.papi(player, content), 0)));
        });
        // 扣除玩家经验等级
        addConsumer("takeLevel", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.giveExpLevels(StringUtils.toInt(HookerManager.papi(player, content), 0) * -1));
        });
        // 设置玩家经验等级
        addConsumer("setLevel", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setLevel(StringUtils.toInt(HookerManager.papi(player, content), 0)));
        });
        // 给予玩家饱食度
        addConsumer("giveFood", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setFoodLevel(player.getFoodLevel() + Math.max(0, Math.min(20, StringUtils.toInt(HookerManager.papi(player, content), 0)))));
        });
        // 扣除玩家饱食度
        addConsumer("takeFood", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setFoodLevel(player.getFoodLevel() - Math.max(0, Math.min(20, StringUtils.toInt(HookerManager.papi(player, content), 0)))));
        });
        // 设置玩家饱食度
        addConsumer("setFood", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setFoodLevel(Math.max(0, Math.min(20, StringUtils.toInt(HookerManager.papi(player, content), 0)))));
        });
        // 给予玩家饱和度
        addConsumer("giveSaturation", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), player.getSaturation() + StringUtils.toFloat(HookerManager.papi(player, content), 0)))));
        });
        // 扣除玩家饱和度
        addConsumer("takeSaturation", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), player.getSaturation() - StringUtils.toFloat(HookerManager.papi(player, content), 0)))));
        });
        // 设置玩家饱和度
        addConsumer("setSaturation", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), StringUtils.toFloat(HookerManager.papi(player, content), 0)))));
        });
        // 给予玩家生命
        addConsumer("giveHealth", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setHealth(Math.max(0, Math.min(player.getMaxHealth(), player.getHealth() + StringUtils.toDouble(HookerManager.papi(player, content), 0)))));
        });
        // 扣除玩家生命
        addConsumer("takeHealth", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setHealth(Math.max(0, Math.min(player.getMaxHealth(), player.getHealth() - StringUtils.toDouble(HookerManager.papi(player, content), 0)))));
        });
        // 设置玩家生命
        addConsumer("setHealth", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            SchedulerUtils.sync(plugin, () -> player.setHealth(Math.max(0, Math.min(player.getMaxHealth(), StringUtils.toDouble(HookerManager.papi(player, content), 0)))));
        });
        // 释放MM技能
        addConsumer("castSkill", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            MythicMobsHooker hooker = HookerManager.INSTANCE.getMythicMobsHooker();
            if (hooker != null) {
                hooker.castSkill(player, content, player);
            }
        });
        // 连击记录
        addConsumer("combo", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            String[] info = HookerManager.papi(player, content).split(" ", 2);
            // 连击组
            String comboGroup = info[0];
            // 连击类型
            String comboType = getOrDefault(info, 1, "");
            if (!player.hasMetadata("NI-Combo-" + comboGroup)) {
                PlayerUtils.setMetadataEZ(player, "NI-Combo-" + comboGroup, new ArrayList<ComboInfo>());
            }
            // 当前时间
            long time = System.currentTimeMillis();
            // 记录列表
            ArrayList<ComboInfo> comboInfos = (ArrayList<ComboInfo>) player.getMetadata("NI-Combo-" + comboGroup).get(0).value();
            // 为空则填入
            assert comboInfos != null;
            if (!comboInfos.isEmpty()) {
                // 连击中断
                if (comboInfos.get(comboInfos.size() - 1).getTime() + ConfigManager.INSTANCE.getComboInterval() < time) {
                    comboInfos.clear();
                }
                // 填入信息
            }
            comboInfos.add(new ComboInfo(comboType, time));
        });
        // 连击清空
        addConsumer("comboClear", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            PlayerUtils.setMetadataEZ(player, "NI-Combo-" + HookerManager.papi(player, content), new ArrayList<ComboInfo>());
        });
        // 设置药水效果
        addConsumer("setPotionEffect", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            String[] args = content.split(" ", 3);
            if (args.length == 3) {
                PotionEffectType type = PotionEffectType.getByName(args[0].toUpperCase(Locale.ROOT));
                Integer amplifier = StringUtils.toIntOrNull(args[1]);
                Integer duration = StringUtils.toIntOrNull(args[2]);
                if (type != null && duration != null && amplifier != null) {
                    SchedulerUtils.sync(plugin, () -> player.addPotionEffect(new PotionEffect(type, duration * 20, amplifier - 1), true));
                }
            }
        });
        // 移除药水效果
        addConsumer("removePotionEffect", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            PotionEffectType type = PotionEffectType.getByName(content.toUpperCase(Locale.ROOT));
            if (type != null) {
                SchedulerUtils.sync(plugin, () -> player.removePotionEffect(type));
            }
        });
        // 延迟(单位是tick)
        addFunction("delay", (context, content) -> new DelayResult(StringUtils.toInt(content, 0)));
        // 终止
        addFunction("return", (context, content) -> Results.STOP);
        // js
        addFunction("js", (context, content) -> {
            Object result;
            try {
                result = actionScripts.computeIfAbsent(content, (key) -> HookerManager.INSTANCE.getNashornHooker().compile(engine, key)).eval(context.getBindings());
                if (result == null) {
                    return Results.SUCCESS;
                }
            } catch (Throwable error) {
                plugin.getLogger().warning("JS动作执行异常, 动作内容如下:");
                for (String contentLine : content.split("\n")) {
                    plugin.getLogger().warning(contentLine);
                }
                error.printStackTrace();
                return Results.SUCCESS;
            }
            if (result instanceof ActionResult) {
                return (ActionResult) result;
            } else if (result instanceof Boolean) {
                return Results.fromBoolean((Boolean) result);
            } else {
                return Results.SUCCESS;
            }
        });
        addConsumer("setglobal", (context, content) -> {
            String[] info = content.split(" ", 2);
            if (info.length > 1) {
                context.getGlobal().put(info[0], info[1]);
            }
        });
        addConsumer("delglobal", (context, content) -> context.getGlobal().remove(content));
        // 扣除NI物品
        addConsumer("takeNiItem", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            String parsedContent = HookerManager.papi(player, content);
            String[] args = parsedContent.split(" ", 2);
            if (args.length < 2) return;
            String itemId = args[0];
            SchedulerUtils.sync(plugin, () -> {
                int amount = StringUtils.toInt(args[1], 0);
                ItemStack[] contents = player.getInventory().getContents();
                for (ItemStack itemStack : contents) {
                    ItemInfo itemInfo = ItemUtils.isNiItem(itemStack);
                    if (itemInfo != null && itemInfo.getId().equals(itemId)) {
                        if (amount > itemStack.getAmount()) {
                            amount -= itemStack.getAmount();
                            itemStack.setAmount(0);
                        } else {
                            itemStack.setAmount(itemStack.getAmount() - amount);
                            amount = 0;
                            break;
                        }
                    }
                }
            });
        });
    }
}
