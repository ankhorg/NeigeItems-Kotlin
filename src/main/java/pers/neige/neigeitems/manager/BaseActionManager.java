package pers.neige.neigeitems.manager;

import com.google.common.io.ByteStreams;
import kotlin.text.StringsKt;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.action.*;
import pers.neige.neigeitems.action.catcher.ChatCatcher;
import pers.neige.neigeitems.action.catcher.SignCatcher;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.handler.SyncActionHandler;
import pers.neige.neigeitems.action.impl.*;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.action.result.StopResult;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.hook.placeholderapi.PapiHooker;
import pers.neige.neigeitems.item.action.ComboInfo;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils;
import pers.neige.neigeitems.text.Text;
import pers.neige.neigeitems.text.impl.NullText;
import pers.neige.neigeitems.utils.*;

import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.logging.Level;

import static pers.neige.neigeitems.utils.ListUtils.*;

@SuppressWarnings("unchecked")
public abstract class BaseActionManager {
    public final @NonNull Action NULL_ACTION = new NullAction(this);
    public final @NonNull Text NULL_TEXT = new NullText(this);
    public final @NonNull Evaluator<String> NULL_STRING_EVALUATOR = new Evaluator<>(this, String.class);
    public final @NonNull Evaluator<Integer> NULL_INTEGER_EVALUATOR = new Evaluator<>(this, Integer.class);
    public final @NonNull Evaluator<Long> NULL_LONG_EVALUATOR = new Evaluator<>(this, Long.class);
    public final @NonNull Evaluator<Double> NULL_DOUBLE_EVALUATOR = new Evaluator<>(this, Double.class);
    private final @NonNull Plugin plugin;
    /**
     * 物品动作实现函数
     */
    private final @NonNull HashMap<String, BiFunction<ActionContext, String, CompletableFuture<ActionResult>>> actions = new HashMap<>();
    /**
     * 用于编译condition的脚本引擎
     */
    private final @NonNull ScriptEngine engine = HookerManager.INSTANCE.getNashornHooker().getGlobalEngine();
    /**
     * 缓存的已编译condition脚本
     */
    private final @NonNull ConcurrentHashMap<String, CompiledScript> conditionScripts = new ConcurrentHashMap<>();
    /**
     * 缓存的已编译action脚本
     */
    private final @NonNull ConcurrentHashMap<String, CompiledScript> actionScripts = new ConcurrentHashMap<>();

    public BaseActionManager(
            @NonNull Plugin plugin
    ) {
        this.plugin = plugin;
        engine.put("plugin", plugin);
        engine.put("manager", this);
        // 加载基础动作
        loadBasicActions();
    }

    public @NonNull Plugin getPlugin() {
        return plugin;
    }

    public @NonNull HashMap<String, BiFunction<ActionContext, String, CompletableFuture<ActionResult>>> getActions() {
        return actions;
    }

    public @NonNull ScriptEngine getEngine() {
        return engine;
    }

    public @NonNull ConcurrentHashMap<String, CompiledScript> getConditionScripts() {
        return conditionScripts;
    }

    public @NonNull ConcurrentHashMap<String, CompiledScript> getActionScripts() {
        return actionScripts;
    }

    public void reload() {
        conditionScripts.clear();
        actionScripts.clear();
    }

    public @NonNull Action compile(
            @Nullable Object action,
            @NonNull Action def
    ) {
        if (action == null) return def;
        return compile(action);
    }

    public @NonNull Action compile(
            @Nullable Object action,
            @Nullable Object def
    ) {
        if (action == null) return compile(def);
        return compile(action);
    }

    public @NonNull Action compile(
            @Nullable Object action
    ) {
        if (action == null) return NULL_ACTION;
        if (action instanceof Action) return (Action) action;
        if (action instanceof String) {
            val string = (String) action;
            val info = string.split(": ", 2);
            val key = info[0].toLowerCase(Locale.ROOT);
            var content = info.length > 1 ? info[1] : "";
            if (key.equals("js")) {
                return new JsAction(this, content);
            } else {
                content = PapiHooker.toAllSection(content);
                return new StringAction(this, string, key, content);
            }
        } else if (action instanceof List<?>) {
            return new ListAction(this, (List<?>) action);
        }
        val config = ConfigReader.parse(action);
        if (config == null) return NULL_ACTION;
        val type = config.getString("type", "").toLowerCase();
        switch (type) {
            case "condition": {
                return new ConditionAction(this, config);
            }
            case "condition-weight": {
                return new ConditionWeightAction(this, config);
            }
            case "contains": {
                return new ContainsAction(this, config);
            }
            case "double-tree": {
                return new DoubleTreeAction(this, config);
            }
            case "int-tree": {
                return new IntTreeAction(this, config);
            }
            case "key": {
                return new KeyAction(this, config);
            }
            case "label": {
                return new LabelAction(this, config);
            }
            case "weight": {
                return new WeightAction(this, config);
            }
            case "while": {
                return new WhileAction(this, config);
            }
            case "repeat": {
                return new RepeatAction(this, config);
            }
        }
        if (config.containsKey("condition")) {
            return new ConditionAction(this, config);
        } else if (config.containsKey("repeat")) {
            return new RepeatAction(this, config);
        } else if (config.containsKey("while")) {
            return new WhileAction(this, config);
        } else if (config.containsKey("label")) {
            return new LabelAction(this, config);
            // 处理有人闲的没事干多包了一层actions的情况
        } else if (config.containsKey("actions")) {
            return compile(config.get("actions"));
        } else {
            // 处理有人连引号都能忘的情况('tell: 123'写成了tell: 123, 也就是字符串写成了长度为1的map)
            val keySet = config.keySet();
            if (keySet.size() != 1) return NULL_ACTION;
            val key = keySet.iterator().next();
            val value = config.get(key);
            if (!(value instanceof String)) return NULL_ACTION;
            var content = (String) value;
            if (key.equals("js")) {
                return new JsAction(this, content);
            } else {
                content = PapiHooker.toAllSection(content);
                return new StringAction(this, key, content);
            }
        }
    }

    /**
     * 执行动作
     *
     * @param action 动作内容
     * @return 永远返回 Results.SUCCESS, 这是历史遗留问题, 要获取真实结果请调用 runActionWithResult 方法.
     */
    @Deprecated
    @kotlin.Deprecated(message = "使用Action.eval(ActionContext)方法代替")
    public @NonNull ActionResult runAction(
            @NonNull Action action
    ) {
        action.eval(ActionContext.empty());
        return Results.SUCCESS;
    }

    /**
     * 执行动作
     *
     * @param action 动作内容
     * @return 执行结果
     */
    @Deprecated
    @kotlin.Deprecated(message = "使用Action.eval(ActionContext)方法代替")
    public @NonNull CompletableFuture<ActionResult> runActionWithResult(
            @NonNull Action action
    ) {
        return action.eval(ActionContext.empty());
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 永远返回 Results.SUCCESS, 这是历史遗留问题, 要获取真实结果请调用 runActionWithResult 方法.
     */
    @Deprecated
    @kotlin.Deprecated(message = "使用Action.eval(ActionContext)方法代替")
    public @NonNull ActionResult runAction(
            @NonNull Action action,
            @NonNull ActionContext context
    ) {
        action.eval(context);
        return Results.SUCCESS;
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @Deprecated
    @kotlin.Deprecated(message = "使用Action.eval(ActionContext)方法代替")
    public @NonNull CompletableFuture<ActionResult> runActionWithResult(
            @NonNull Action action,
            @NonNull ActionContext context
    ) {
        return action.eval(context);
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull RawStringAction action,
            @NonNull ActionContext context
    ) {
        val handler = action.getHandler();
        if (handler == null) return CompletableFuture.completedFuture(Results.SUCCESS);
        return handler.apply(context, action.getContent());
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull StringAction action,
            @NonNull ActionContext context
    ) {
        val handler = action.getHandler();
        if (handler == null) return CompletableFuture.completedFuture(Results.SUCCESS);
        // 对动作内容进行节点解析
        val content = SectionUtils.parseSection(
                action.getContent(),
                (Map<String, String>) (Object) context.getGlobal(),
                context.getPlayer(),
                getSectionConfig(context)
        );
        return handler.apply(context, content);
    }

    /**
     * 执行 StringAction 进行节点解析的时候传入的节点配置
     *
     * @param context 动作上下文
     * @return 执行结果
     */
    public @Nullable ConfigurationSection getSectionConfig(@NonNull ActionContext context) {
        return null;
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull JsAction action,
            @NonNull ActionContext context
    ) {
        Object result;
        try {
            result = action.getScript().eval(context.getBindings());
            if (result == null) {
                return CompletableFuture.completedFuture(Results.SUCCESS);
            }
        } catch (Throwable error) {
            plugin.getLogger().warning("JS动作执行异常, 动作内容如下:");
            val lines = action.getScriptString().split("\n");
            for (int i = 0; i < lines.length; i++) {
                val contentLine = lines[i];
                plugin.getLogger().warning((i + 1) + ". " + contentLine);
            }
            error.printStackTrace();
            return CompletableFuture.completedFuture(Results.STOP);
        }
        if (result instanceof ActionResult) {
            return CompletableFuture.completedFuture((ActionResult) result);
        } else if (result instanceof Boolean) {
            return CompletableFuture.completedFuture(Results.fromBoolean((Boolean) result));
        } else if (result instanceof CompletableFuture<?>) {
            return (CompletableFuture<ActionResult>) result;
        } else {
            return CompletableFuture.completedFuture(Results.SUCCESS);
        }
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull ListAction action,
            @NonNull ActionContext context
    ) {
        return runAction(action, context, 0);
    }

    /**
     * 执行动作
     *
     * @param action    动作内容
     * @param context   动作上下文
     * @param fromIndex 从这个索引对应的位置开始执行
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull ListAction action,
            @NonNull ActionContext context,
            int fromIndex
    ) {
        val actions = action.getActions();
        if (fromIndex >= actions.size()) return CompletableFuture.completedFuture(Results.SUCCESS);
        return actions.get(fromIndex).evalAsyncSafe(this, context).thenCompose((result) -> {
            if (result.getType() == ResultType.STOP) {
                return CompletableFuture.completedFuture(result);
            }
            return runAction(action, context, fromIndex + 1);
        });
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull ConditionAction action,
            @NonNull ActionContext context
    ) {
        // 如果条件通过
        if (action.getCondition().easyCheck(context)) {
            // 执行动作
            SchedulerUtils.sync(plugin, () -> action.getSync().evalAsyncSafe(this, context));
            SchedulerUtils.async(plugin, () -> action.getAsync().evalAsyncSafe(this, context));
            return action.getActions().evalAsyncSafe(this, context);
            // 条件未通过
        } else {
            // 执行deny动作
            return action.getDeny().evalAsyncSafe(this, context);
        }
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull LabelAction action,
            @NonNull ActionContext context
    ) {
        return action.getActions().evalAsyncSafe(this, context).thenApply((result) -> {
            if (result instanceof StopResult && action.getLabel().equals(((StopResult) result).getLabel())) {
                return Results.SUCCESS;
            }
            return result;
        });
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull WeightAction action,
            @NonNull ActionContext context
    ) {
        if (action.getTotalWeight() <= 0 || action.getActions().isEmpty())
            return CompletableFuture.completedFuture(Results.SUCCESS);
        int amount = action.getAmount(this, context);
        if (amount >= action.getActions().size()) {
            var result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (val currentAction : action.getActions()) {
                result = result.thenCombine(currentAction.getFirst().evalAsyncSafe(this, context), (r1, r2) -> {
                    if (r1.compareTo(r2) < 0) {
                        return r2;
                    } else {
                        return r1;
                    }
                });
            }
            return result;
        }
        if (amount == 1) {
            Action result;
            if (action.isEqualWeight()) {
                result = action.getActions().get(ThreadLocalRandom.current().nextInt(0, action.getActions().size())).getFirst();
            } else {
                result = SamplingUtils.weight(action.getActions(), action.getTotalWeight());
            }
            if (result != null) {
                return result.evalAsyncSafe(this, context);
            }
        } else if (amount > 1) {
            val actions = SamplingUtils.weightWithIndex(action.getActions(), amount);
            if (action.isOrder()) {
                actions.sort(Comparator.comparingInt(SamplingUtils.SamplingResult::getIndex));
            }
            var result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (val currentAction : actions) {
                result = result.thenCombine(currentAction.getValue().evalAsyncSafe(this, context), (r1, r2) -> {
                    if (r1.compareTo(r2) < 0) {
                        return r2;
                    } else {
                        return r1;
                    }
                });
            }
            return result;
        }
        return CompletableFuture.completedFuture(Results.SUCCESS);
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull ConditionWeightAction action,
            @NonNull ActionContext context
    ) {
        val actions = action.getActions(context);
        if (actions.isEmpty()) return CompletableFuture.completedFuture(Results.SUCCESS);
        val amount = action.getAmount(this, context);
        if (amount >= actions.size()) {
            var result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (val currentAction : actions) {
                result = result.thenCombine(currentAction.getFirst().evalAsyncSafe(this, context), (r1, r2) -> {
                    if (r1.compareTo(r2) < 0) {
                        return r2;
                    } else {
                        return r1;
                    }
                });
            }
            return result;
        }
        if (amount == 1) {
            val result = SamplingUtils.weight(actions);
            if (result != null) {
                return result.evalAsyncSafe(this, context);
            }
        } else if (amount > 1) {
            val finalActions = SamplingUtils.weightWithIndex(actions, amount);
            if (action.isOrder()) {
                finalActions.sort(Comparator.comparingInt(SamplingUtils.SamplingResult::getIndex));
            }
            var result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (val currentAction : finalActions) {
                result = result.thenCombine(currentAction.getValue().evalAsyncSafe(this, context), (r1, r2) -> {
                    if (r1.compareTo(r2) < 0) {
                        return r2;
                    } else {
                        return r1;
                    }
                });
            }
            return result;
        }
        return CompletableFuture.completedFuture(Results.SUCCESS);
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull WhileAction action,
            @NonNull ActionContext context
    ) {
        // while循环判断条件
        if (action.getCondition().easyCheck(context)) {
            return action.getActions().evalAsyncSafe(this, context).thenCompose((result) -> {
                // 执行中止
                if (result.getType() == ResultType.STOP) {
                    // 执行finally块
                    return action.getFinally().evalAsyncSafe(this, context);
                } else {
                    // 继续执行
                    return runAction(action, context);
                }
            });
        } else {
            // 执行finally块
            return action.getFinally().evalAsyncSafe(this, context);
        }
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull KeyAction action,
            @NonNull ActionContext context
    ) {
        val key = action.getKey().get(context);
        context.getGlobal().put(action.getGlobalId(), key);
        if (key == null) {
            return action.getDefaultAction().evalAsyncSafe(this, context);
        }
        val targetAction = action.getActions().get(key);
        if (targetAction == null) {
            return action.getDefaultAction().evalAsyncSafe(this, context);
        }
        return action.getMatchAction().evalAsyncSafe(this, context).thenCompose((result) -> {
            if (result.getType() == ResultType.STOP) {
                return CompletableFuture.completedFuture(result);
            } else {
                return targetAction.evalAsyncSafe(this, context);
            }
        });
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull ContainsAction action,
            @NonNull ActionContext context
    ) {
        val key = action.getKey().get(context);
        context.getGlobal().put(action.getGlobalId(), key);
        if (action.getElements().contains(key)) {
            return action.getContainsAction().evalAsyncSafe(this, context);
        } else {
            return action.getDefaultAction().evalAsyncSafe(this, context);
        }
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull <T extends Comparable<?>> CompletableFuture<ActionResult> runAction(
            @NonNull TreeAction<T> action,
            @NonNull ActionContext context
    ) {
        val key = action.getKey().get(context);
        context.getGlobal().put(action.getGlobalId(), key);
        if (key == null) {
            return action.getDefaultAction().evalAsyncSafe(this, context);
        }
        Map.Entry<T, Action> entry = null;
        switch (action.getActionType()) {
            case LOWER:
                entry = action.getActions().lowerEntry(key);
                break;
            case FLOOR:
                entry = action.getActions().floorEntry(key);
                break;
            case HIGHER:
                entry = action.getActions().higherEntry(key);
                break;
            case CEILING:
                entry = action.getActions().ceilingEntry(key);
                break;
        }
        if (entry == null) {
            return action.getDefaultAction().evalAsyncSafe(this, context);
        }
        val targetAction = entry.getValue();
        return action.getMatchAction().evalAsyncSafe(this, context).thenCompose((result) -> {
            if (result.getType() == ResultType.STOP) {
                return CompletableFuture.completedFuture(result);
            } else {
                return targetAction.evalAsyncSafe(this, context);
            }
        });
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            @NonNull RepeatAction action,
            @NonNull ActionContext context
    ) {
        final int repeat = action.getRepeat().getOrDefault(context, 0);
        if (repeat <= 0) return CompletableFuture.completedFuture(Results.SUCCESS);
        return runAction(action, context, repeat, 0);
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    public @NonNull CompletableFuture<ActionResult> runAction(
            final @NonNull RepeatAction action,
            final @NonNull ActionContext context,
            final int repeat,
            final int count
    ) {
        context.getGlobal().put(action.getGlobalId(), count);
        return action.getActions().evalAsyncSafe(this, context).thenCompose((result) -> {
            if (result.getType() == ResultType.STOP) {
                return CompletableFuture.completedFuture(result);
            } else {
                val newCount = count + 1;
                if (newCount < repeat) {
                    return runAction(action, context, repeat, newCount);
                } else {
                    return CompletableFuture.completedFuture(result);
                }
            }
        });
    }

    /**
     * 解析条件
     *
     * @param condition 条件内容
     * @param context   动作上下文
     * @return 执行结果
     */
    public @NonNull ActionResult parseCondition(
            @Nullable String condition,
            @NonNull ActionContext context
    ) {
        if (condition == null) {
            return Results.SUCCESS;
        }
        return parseCondition(
                condition,
                conditionScripts.computeIfAbsent(condition, (key) -> HookerManager.INSTANCE.getNashornHooker().compile(engine, key)),
                context
        );
    }

    /**
     * 解析条件
     *
     * @param condition 条件内容
     * @param context   动作上下文
     * @return 执行结果
     */
    public @NonNull ActionResult parseCondition(
            @Nullable ScriptWithSource condition,
            @NonNull ActionContext context
    ) {
        if (condition == null) {
            return Results.SUCCESS;
        }
        return parseCondition(
                condition.getSource(),
                condition,
                context
        );
    }

    /**
     * 解析条件
     *
     * @param conditionString 条件文本(用于在报错的时候向后台提示)
     * @param condition       已编译条件
     * @param context         动作上下文
     * @return 执行结果
     */
    public @NonNull ActionResult parseCondition(
            @Nullable String conditionString,
            @Nullable CompiledScript condition,
            @NonNull ActionContext context
    ) {
        if (condition == null) {
            return Results.SUCCESS;
        }
        Object result;
        try {
            result = condition.eval(context.getBindings());
            if (result == null) {
                return Results.STOP;
            }
        } catch (Throwable error) {
            if (conditionString != null) {
                plugin.getLogger().warning("条件解析异常, 条件内容如下:");
                val lines = conditionString.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    val conditionLine = lines[i];
                    plugin.getLogger().warning((i + 1) + ". " + conditionLine);
                }
            } else {
                plugin.getLogger().warning("条件解析异常, 条件内容未知");
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
     * @param ids      动作ID
     * @param function 动作执行函数
     */
    public void addFunction(
            @NonNull Collection<String> ids,
            @Nullable BiFunction<ActionContext, String, CompletableFuture<ActionResult>> function
    ) {
        if (function == null) return;
        for (val id : ids) {
            addFunction(id, function);
        }
    }

    /**
     * 添加物品动作
     *
     * @param ids       动作ID
     * @param asyncSafe 动作是否可以异步执行
     * @param function  动作执行函数
     */
    public void addFunction(
            @NonNull Collection<String> ids,
            boolean asyncSafe,
            @Nullable BiFunction<ActionContext, String, CompletableFuture<ActionResult>> function
    ) {
        if (function == null) return;
        for (val id : ids) {
            addFunction(id, asyncSafe, function);
        }
    }

    /**
     * 添加物品动作
     *
     * @param id       动作ID
     * @param function 动作执行函数
     */
    public void addFunction(
            @NonNull String id,
            @Nullable BiFunction<ActionContext, String, CompletableFuture<ActionResult>> function
    ) {
        addFunction(id, true, function);
    }

    /**
     * 添加物品动作
     *
     * @param id        动作ID
     * @param asyncSafe 动作是否可以异步执行
     * @param function  动作执行函数
     */
    public void addFunction(
            @NonNull String id,
            boolean asyncSafe,
            @Nullable BiFunction<ActionContext, String, CompletableFuture<ActionResult>> function
    ) {
        if (function == null) return;
        BiFunction<ActionContext, String, CompletableFuture<ActionResult>> handler;
        if (asyncSafe) {
            handler = (context, content) -> {
                var result = function.apply(context, content);
                if (result == null) result = CompletableFuture.completedFuture(Results.SUCCESS);
                return result;
            };
        } else {
            handler = (SyncActionHandler) (context, content) -> {
                var result = function.apply(context, content);
                if (result == null) result = CompletableFuture.completedFuture(Results.SUCCESS);
                return result;
            };
        }
        actions.put(id.toLowerCase(Locale.ROOT), handler);
    }

    /**
     * 添加物品动作
     *
     * @param ids      动作ID
     * @param consumer 动作执行函数
     */
    public void addConsumer(
            @NonNull Collection<String> ids,
            @Nullable BiConsumer<ActionContext, String> consumer
    ) {
        if (consumer == null) return;
        for (val id : ids) {
            addConsumer(id, consumer);
        }
    }

    /**
     * 添加物品动作
     *
     * @param ids       动作ID
     * @param asyncSafe 动作是否可以异步执行
     * @param consumer  动作执行函数
     */
    public void addConsumer(
            @NonNull Collection<String> ids,
            boolean asyncSafe,
            @Nullable BiConsumer<ActionContext, String> consumer
    ) {
        if (consumer == null) return;
        for (val id : ids) {
            addConsumer(id, asyncSafe, consumer);
        }
    }

    /**
     * 添加物品动作
     *
     * @param id       动作ID
     * @param consumer 动作执行函数
     */
    public void addConsumer(
            @NonNull String id,
            @Nullable BiConsumer<ActionContext, String> consumer
    ) {
        addConsumer(id, true, consumer);
    }

    /**
     * 添加物品动作
     *
     * @param id        动作ID
     * @param asyncSafe 动作是否可以异步执行
     * @param consumer  动作执行函数
     */
    public void addConsumer(
            @NonNull String id,
            boolean asyncSafe,
            @Nullable BiConsumer<ActionContext, String> consumer
    ) {
        if (consumer == null) return;
        BiFunction<ActionContext, String, CompletableFuture<ActionResult>> handler;
        if (asyncSafe) {
            handler = (context, content) -> {
                consumer.accept(context, content);
                return CompletableFuture.completedFuture(Results.SUCCESS);
            };
        } else {
            handler = (SyncActionHandler) (context, content) -> {
                consumer.accept(context, content);
                return CompletableFuture.completedFuture(Results.SUCCESS);
            };
        }
        actions.put(id.toLowerCase(Locale.ROOT), handler);
    }

    /**
     * 将当前BaseActionManager所属插件的"JavaScriptLib/lib.js"资源文件加载至JS引擎
     */
    public void loadJSLib() {
        loadJSLib(plugin, "JavaScriptLib/lib.js");
    }

    /**
     * 将其他插件的资源文件加载至JS引擎
     *
     * @param pluginName   目标插件名
     * @param resourceName 资源文件名
     */
    public void loadJSLib(
            @NonNull String pluginName,
            @NonNull String resourceName
    ) {
        val plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin == null) return;
        loadJSLib(plugin, resourceName);
    }

    /**
     * 将其他插件的资源文件加载至JS引擎
     *
     * @param plugin       目标插件
     * @param resourceName 资源文件名
     */
    public void loadJSLib(
            @NonNull Plugin plugin,
            @NonNull String resourceName
    ) {
        try (val input = plugin.getResource(resourceName)) {
            if (input != null) {
                try (val reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                    getEngine().eval(reader);
                } catch (Throwable error) {
                    this.plugin.getLogger().log(Level.WARNING, "error occurred while loading " + resourceName + " from " + plugin.getName(), error);
                }
            } else {
                this.plugin.getLogger().log(Level.WARNING, "there is no resource called " + resourceName + " in " + plugin.getName());
            }
        } catch (Throwable error) {
            this.plugin.getLogger().log(Level.WARNING, "error occurred while loading " + resourceName + " from " + plugin.getName(), error);
        }
    }

    /**
     * 加载基础物品动作
     */
    protected void loadBasicActions() {
        // 向玩家发送消息
        addConsumer("tell", (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', content));
        });
        // 向玩家发送消息(不将&解析为颜色符号)
        addConsumer(Arrays.asList("tell-no-color", "tellNoColor"), (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.sendMessage(content);
        });
        // 向玩家发送消息, 未指定玩家则向后台发送消息
        addConsumer("tell-or-print", (context, content) -> {
            CommandSender sender = context.getPlayer();
            sender = sender == null ? Bukkit.getConsoleSender() : sender;
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', content));
        });
        // 向玩家发送消息(不将&解析为颜色符号), 未指定玩家则向后台发送消息
        addConsumer("tell-or-print-no-color", (context, content) -> {
            CommandSender sender = context.getPlayer();
            sender = sender == null ? Bukkit.getConsoleSender() : sender;
            sender.sendMessage(content);
        });
        // 强制玩家发送消息
        addConsumer("chat", false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.chat(content);
        });
        // 强制玩家发送消息(将&解析为颜色符号)
        addConsumer(Arrays.asList("chat-with-color", "chatWithColor"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.chat(ChatColor.translateAlternateColorCodes('&', content));
        });
        // 强制玩家执行指令
        addConsumer(Arrays.asList("command", "player"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            Bukkit.dispatchCommand(player, ChatColor.translateAlternateColorCodes('&', content));
        });
        // 强制玩家执行指令(不将&解析为颜色符号)
        addConsumer(Arrays.asList("command-no-color", "commandNoColor"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            Bukkit.dispatchCommand(player, content);
        });
        // 后台执行指令
        addConsumer("console", false, (context, content) -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', content));
        });
        // 后台执行指令(不将&解析为颜色符号)
        addConsumer(Arrays.asList("console-no-color", "consoleNoColor"), false, (context, content) -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), content);
        });
        // 公告
        addConsumer("broadcast", false, (context, content) -> {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', content));
        });
        // 公告(不将&解析为颜色符号)
        addConsumer(Arrays.asList("broadcast-no-color", "broadcastNoColor"), false, (context, content) -> {
            Bukkit.broadcastMessage(content);
        });
        // 发送Title
        addConsumer("title", (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val args = StringUtils.split(ChatColor.translateAlternateColorCodes('&', content), ' ', '\\');
            val title = getOrNull(args, 0);
            val subtitle = getOrDefault(args, 1, "");
            int fadeIn = getAndApply(args, 2, 10, NumberParser::parseInteger);
            int stay = getAndApply(args, 3, 70, NumberParser::parseInteger);
            int fadeOut = getAndApply(args, 4, 20, NumberParser::parseInteger);
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        });
        // 发送Title(不将&解析为颜色符号)
        addConsumer(Arrays.asList("title-no-color", "titleNoColor"), (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val args = StringUtils.split(content, ' ', '\\');
            val title = getOrNull(args, 0);
            val subtitle = getOrDefault(args, 1, "");
            int fadeIn = getAndApply(args, 2, 10, NumberParser::parseInteger);
            int stay = getAndApply(args, 3, 70, NumberParser::parseInteger);
            int fadeOut = getAndApply(args, 4, 20, NumberParser::parseInteger);
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        });
        // 发送全体Title
        addConsumer("broadcast-title", false, (context, content) -> {
            val args = StringUtils.split(ChatColor.translateAlternateColorCodes('&', content), ' ', '\\');
            val title = getOrNull(args, 0);
            val subtitle = getOrDefault(args, 1, "");
            int fadeIn = getAndApply(args, 2, 10, NumberParser::parseInteger);
            int stay = getAndApply(args, 3, 70, NumberParser::parseInteger);
            int fadeOut = getAndApply(args, 4, 20, NumberParser::parseInteger);
            for (val player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }
        });
        // 发送全体Title(不将&解析为颜色符号)
        addConsumer("broadcast-title-no-color", false, (context, content) -> {
            val args = StringUtils.split(content, ' ', '\\');
            val title = getOrNull(args, 0);
            val subtitle = getOrDefault(args, 1, "");
            int fadeIn = getAndApply(args, 2, 10, NumberParser::parseInteger);
            int stay = getAndApply(args, 3, 70, NumberParser::parseInteger);
            int fadeOut = getAndApply(args, 4, 20, NumberParser::parseInteger);
            for (val player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }
        });
        // 发送ActionBar
        addConsumer("actionbar", (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            PlayerUtils.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', content));
        });
        // 发送ActionBar(不将&解析为颜色符号)
        addConsumer(Arrays.asList("actionbar-no-color", "actionbarNoColor"), (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            PlayerUtils.sendActionBar(player, content);
        });
        // 播放音乐
        addConsumer("sound", (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val args = content.split(" ", 3);
            val sound = getOrDefault(args, 0, "");
            float volume = getAndApply(args, 1, 1F, NumberParser::parseFloat);
            float pitch = getAndApply(args, 2, 1F, NumberParser::parseFloat);
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
        // 给予玩家金钱
        addConsumer(Arrays.asList("give-money", "giveMoney"), (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val hooker = HookerManager.INSTANCE.getVaultHooker();
            if (hooker == null) return;
            hooker.giveMoney(player, StringUtils.parseDouble(content, 0.0));
        });
        // 扣除玩家金钱
        addConsumer(Arrays.asList("take-money", "takeMoney"), (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val hooker = HookerManager.INSTANCE.getVaultHooker();
            if (hooker == null) return;
            hooker.takeMoney(player, StringUtils.parseDouble(content, 0.0));
        });
        // 给予玩家经验
        addConsumer(Arrays.asList("give-exp", "giveExp"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            HookerManager.INSTANCE.getNmsHooker().giveExp(player, StringUtils.parseInteger(content, 0));
        });
        // 扣除玩家经验
        addConsumer(Arrays.asList("take-exp", "takeExp"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            HookerManager.INSTANCE.getNmsHooker().giveExp(player, StringUtils.parseInteger(content, 0) * -1);
        });
        // 设置玩家经验
        addConsumer(Arrays.asList("set-exp", "setExp"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            HookerManager.INSTANCE.getNmsHooker().giveExp(player, StringUtils.parseInteger(content, 0) - player.getTotalExperience());
        });
        // 给予玩家经验等级
        addConsumer(Arrays.asList("give-level", "giveLevel"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.giveExpLevels(StringUtils.parseInteger(content, 0));
        });
        // 扣除玩家经验等级
        addConsumer(Arrays.asList("take-level", "takeLevel"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.giveExpLevels(StringUtils.parseInteger(content, 0) * -1);
        });
        // 设置玩家经验等级
        addConsumer(Arrays.asList("set-level", "setLevel"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setLevel(StringUtils.parseInteger(content, 0));
        });
        // 给予玩家饱食度
        addConsumer(Arrays.asList("give-food", "giveFood"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setFoodLevel(player.getFoodLevel() + Math.max(0, Math.min(20, StringUtils.parseInteger(content, 0))));
        });
        // 扣除玩家饱食度
        addConsumer(Arrays.asList("take-food", "takeFood"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setFoodLevel(player.getFoodLevel() - Math.max(0, Math.min(20, StringUtils.parseInteger(content, 0))));
        });
        // 设置玩家饱食度
        addConsumer(Arrays.asList("set-food", "setFood"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setFoodLevel(Math.max(0, Math.min(20, StringUtils.parseInteger(content, 0))));
        });
        // 给予玩家饱和度
        addConsumer(Arrays.asList("give-saturation", "giveSaturation"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), player.getSaturation() + StringUtils.parseFloat(content, 0))));
        });
        // 扣除玩家饱和度
        addConsumer(Arrays.asList("take-saturation", "takeSaturation"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), player.getSaturation() - StringUtils.parseFloat(content, 0))));
        });
        // 设置玩家饱和度
        addConsumer(Arrays.asList("set-saturation", "setSaturation"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), StringUtils.parseFloat(content, 0))));
        });
        // 给予玩家生命
        addConsumer(Arrays.asList("give-health", "giveHealth"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setHealth(Math.max(0, Math.min(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), player.getHealth() + StringUtils.parseDouble(content, 0))));
        });
        // 扣除玩家生命
        addConsumer(Arrays.asList("take-health", "takeHealth"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setHealth(Math.max(0, Math.min(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), player.getHealth() - StringUtils.parseDouble(content, 0))));
        });
        // 设置玩家生命
        addConsumer(Arrays.asList("set-health", "setHealth"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            player.setHealth(Math.max(0, Math.min(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), StringUtils.parseDouble(content, 0))));
        });
        // 释放MM技能
        addConsumer(Arrays.asList("cast-skill", "castSkill"), (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val hooker = HookerManager.INSTANCE.getMythicMobsHooker();
            if (hooker == null) return;
            hooker.castSkill(player, content, player);
        });
        // 连击记录
        addConsumer("combo", (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val info = content.split(" ", 2);
            // 连击组
            val comboGroup = info[0];
            // 连击类型
            val comboType = getOrDefault(info, 1, "");
            // 为空则填入
            if (!PlayerUtils.hasMetadataEZ(player, "Combo-" + comboGroup)) {
                PlayerUtils.setMetadataEZ(player, "Combo-" + comboGroup, new ArrayList<ComboInfo>());
            }
            // 当前时间
            val time = System.currentTimeMillis();
            // 记录列表
            val comboInfos = (ArrayList<ComboInfo>) PlayerUtils.getMetadataEZ(player, "Combo-" + comboGroup, "");
            if (comboInfos == null) return;
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
        addConsumer(Arrays.asList("combo-clear", "comboClear"), (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            PlayerUtils.setMetadataEZ(player, "Combo-" + content, new ArrayList<ComboInfo>());
        });
        // 设置药水效果
        addConsumer(Arrays.asList("set-potion", "setPotion", "set-potion-effect", "setPotionEffect"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val args = content.split(" ", 3);
            if (args.length < 3) return;
            val type = PotionEffectType.getByName(args[0].toUpperCase(Locale.ROOT));
            val amplifier = StringUtils.parseInteger(args[1]);
            val duration = StringUtils.parseInteger(args[2]);
            if (type == null || duration == null || amplifier == null) return;
            player.addPotionEffect(new PotionEffect(type, duration * 20, amplifier - 1), true);
        });
        // 移除药水效果
        addConsumer(Arrays.asList("remove-potion", "removePotion", "remove-potion-effect", "removePotionEffect"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val type = PotionEffectType.getByName(content.toUpperCase(Locale.ROOT));
            if (type == null) return;
            player.removePotionEffect(type);
        });
        // 延迟(单位是tick)
        addFunction("delay", (context, content) -> {
            val result = new CompletableFuture<ActionResult>();
            SchedulerUtils.runLater(plugin, StringUtils.parseInteger(content, 0), () -> {
                result.complete(Results.SUCCESS);
            });
            return result;
        });
        // 终止
        addFunction("return", (context, content) -> {
            if (content.isEmpty()) {
                return CompletableFuture.completedFuture(Results.STOP);
            } else {
                return CompletableFuture.completedFuture(new StopResult(content));
            }
        });
        // 终止
        addFunction(Arrays.asList("return-weight", "returnWeight"), (context, content) -> {
            val args = content.split(" ", 2);
            int priority = getAndApply(args, 0, 1, NumberParser::parseInteger);
            val label = getOrNull(args, 1);
            return CompletableFuture.completedFuture(new StopResult(label, priority));
        });
        // 向global中设置内容
        addConsumer(Arrays.asList("set-global", "setGlobal"), (context, content) -> {
            val args = content.split(" ", 2);
            if (args.length > 1) {
                context.getGlobal().put(args[0], args[1]);
            }
        });
        // 从global中删除内容
        addConsumer(Arrays.asList("del-global", "delGlobal"), (context, content) -> context.getGlobal().remove(content));
        // 扣除NI物品
        addConsumer(Arrays.asList("take-ni-item", "takeNiItem"), false, (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val args = content.split(" ", 2);
            if (args.length < 2) return;
            val itemId = args[0];
            int amount = StringUtils.parseInteger(args[1], 0);
            val contents = player.getInventory().getContents();
            for (val itemStack : contents) {
                val currentItemId = ItemUtils.getItemId(itemStack);
                if (!itemId.equals(currentItemId)) continue;
                if (amount > itemStack.getAmount()) {
                    amount -= itemStack.getAmount();
                    itemStack.setAmount(0);
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    break;
                }
            }
        });
        // 跨服
        addConsumer("server", (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(content);
            player.sendPluginMessage(NeigeItems.getInstance(), "BungeeCord", out.toByteArray());
        });
        // 切换至主线程
        addFunction("sync", (context, content) -> {
            context.setSync(true);
            if (Bukkit.isPrimaryThread()) {
                return CompletableFuture.completedFuture(Results.SUCCESS);
            }
            val result = new CompletableFuture<ActionResult>();
            Bukkit.getScheduler().runTask(plugin, () -> result.complete(Results.SUCCESS));
            return result;
        });
        // 切换至异步线程
        addFunction("async", (context, content) -> {
            context.setSync(false);
            if (!Bukkit.isPrimaryThread()) {
                return CompletableFuture.completedFuture(Results.SUCCESS);
            }
            val result = new CompletableFuture<ActionResult>();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> result.complete(Results.SUCCESS));
            return result;
        });
        // 聊天捕获器
        addFunction(Arrays.asList("catch-chat", "catchChat"), (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            val args = StringUtils.split(content, ' ', '\\');
            val messageKey = getOrDefault(args, 0, "catchChat");
            boolean cancel = getAndApply(args, 1, true, StringsKt::toBooleanStrictOrNull);
            val user = UserManager.INSTANCE.get(player.getUniqueId());
            if (user == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            val result = new CompletableFuture<ActionResult>();
            user.addChatCatcher(new ChatCatcher(this, messageKey, cancel, context, result));
            return result;
        });
        // 告示牌捕获器
        addFunction(Arrays.asList("catch-sign", "catchSign"), (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            val user = UserManager.INSTANCE.get(player.getUniqueId());
            if (user == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            val result = new CompletableFuture<ActionResult>();
            user.addSignCatcher(new SignCatcher(this, content, context, result));
            EntityPlayerUtils.openSign(player);
            return result;
        });
        // 清除聊天捕获器
        addConsumer("clear-catch-chat", (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val user = UserManager.INSTANCE.get(player.getUniqueId());
            if (user == null) return;
            user.clearChatCatcher();
        });
        // 清除告示牌捕获器
        addConsumer("clear-catch-sign", (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val user = UserManager.INSTANCE.get(player.getUniqueId());
            if (user == null) return;
            user.clearSignCatcher();
        });
        // 触发动作组
        addFunction("func", (context, content) -> {
            val function = ActionManager.INSTANCE.getFunctions().get(content);
            if (function == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            return function.evalAsyncSafe(context);
        });
        // 设置分组冷却
        addConsumer("set-cooldown", (context, content) -> {
            val player = context.getPlayer();
            if (player == null) return;
            val user = UserManager.INSTANCE.get(player.getUniqueId());
            if (user == null) return;
            val args = content.split(" ", 2);
            val key = getOrNull(args, 0);
            if (key == null) return;
            val cooldownText = getOrNull(args, 1);
            if (cooldownText == null) return;
            val cooldown = NumberParser.parseLong(cooldownText);
            if (cooldown == null) return;
            user.setCooldown(key, cooldown);
        });
    }
}
