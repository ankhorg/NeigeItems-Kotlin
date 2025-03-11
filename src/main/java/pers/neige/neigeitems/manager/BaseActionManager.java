package pers.neige.neigeitems.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import kotlin.Pair;
import kotlin.text.StringsKt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
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
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker;
import pers.neige.neigeitems.hook.placeholderapi.PapiHooker;
import pers.neige.neigeitems.hook.vault.VaultHooker;
import pers.neige.neigeitems.item.action.ComboInfo;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils;
import pers.neige.neigeitems.text.Text;
import pers.neige.neigeitems.text.impl.NullText;
import pers.neige.neigeitems.user.User;
import pers.neige.neigeitems.utils.*;

import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import java.io.InputStream;
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
    public final @NotNull Action NULL_ACTION = new NullAction(this);
    public final @NotNull Text NULL_TEXT = new NullText(this);
    public final @NotNull Evaluator<String> NULL_STRING_EVALUATOR = new Evaluator<>(this, String.class);
    public final @NotNull Evaluator<Integer> NULL_INTEGER_EVALUATOR = new Evaluator<>(this, Integer.class);
    public final @NotNull Evaluator<Double> NULL_DOUBLE_EVALUATOR = new Evaluator<>(this, Double.class);
    private final @NotNull Plugin plugin;
    /**
     * 物品动作实现函数
     */
    @NotNull
    private final HashMap<String, BiFunction<ActionContext, String, CompletableFuture<ActionResult>>> actions = new HashMap<>();
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
    public HashMap<String, BiFunction<ActionContext, String, CompletableFuture<ActionResult>>> getActions() {
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
            @Nullable Object action,
            @NotNull Action def
    ) {
        if (action == null) return def;
        return compile(action);
    }

    @NotNull
    public Action compile(
            @Nullable Object action,
            @Nullable Object def
    ) {
        if (action == null) return compile(def);
        return compile(action);
    }

    @NotNull
    public Action compile(
            @Nullable Object action
    ) {
        if (action == null) return NULL_ACTION;
        if (action instanceof Action) return (Action) action;
        if (action instanceof String) {
            String string = (String) action;
            String[] info = string.split(": ", 2);
            String key = info[0].toLowerCase(Locale.ROOT);
            String content = info.length > 1 ? info[1] : "";
            if (key.equals("js")) {
                return new JsAction(this, content);
            } else {
                content = PapiHooker.toAllSection(content);
                return new StringAction(this, string, key, content);
            }
        } else if (action instanceof List<?>) {
            return new ListAction(this, (List<?>) action);
        }
        ConfigReader config = ConfigReader.parse(action);
        if (config == null) return NULL_ACTION;
        String type = config.getString("type", "").toLowerCase();
        switch (type) {
            case "condition": {
                return new ConditionAction(this, config);
            }
            case "condition-weight": {
                return new ConditionWeightAction(this, config);
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
        }
        return NULL_ACTION;
    }

    /**
     * 执行动作
     *
     * @param action 动作内容
     * @return 永远返回 Results.SUCCESS, 这是历史遗留问题, 要获取真实结果请调用 runActionWithResult 方法.
     */
    @NotNull
    @Deprecated
    @kotlin.Deprecated(message = "使用Action.eval(ActionContext)方法代替")
    public ActionResult runAction(
            @NotNull Action action
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
    @NotNull
    @Deprecated
    @kotlin.Deprecated(message = "使用Action.eval(ActionContext)方法代替")
    public CompletableFuture<ActionResult> runActionWithResult(
            @NotNull Action action
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
    @NotNull
    @Deprecated
    @kotlin.Deprecated(message = "使用Action.eval(ActionContext)方法代替")
    public ActionResult runAction(
            @NotNull Action action,
            @NotNull ActionContext context
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
    @NotNull
    @Deprecated
    @kotlin.Deprecated(message = "使用Action.eval(ActionContext)方法代替")
    public CompletableFuture<ActionResult> runActionWithResult(
            @NotNull Action action,
            @NotNull ActionContext context
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull RawStringAction action,
            @NotNull ActionContext context
    ) {
        BiFunction<ActionContext, String, CompletableFuture<ActionResult>> handler = action.getHandler();
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull StringAction action,
            @NotNull ActionContext context
    ) {
        BiFunction<ActionContext, String, CompletableFuture<ActionResult>> handler = action.getHandler();
        if (handler == null) return CompletableFuture.completedFuture(Results.SUCCESS);
        // 对动作内容进行节点解析
        String content = SectionUtils.parseSection(
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
    @Nullable
    public ConfigurationSection getSectionConfig(@NotNull ActionContext context) {
        return null;
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull JsAction action,
            @NotNull ActionContext context
    ) {
        Object result;
        try {
            result = action.getScript().eval(context.getBindings());
            if (result == null) {
                return CompletableFuture.completedFuture(Results.SUCCESS);
            }
        } catch (Throwable error) {
            plugin.getLogger().warning("JS动作执行异常, 动作内容如下:");
            String[] lines = action.getScriptString().split("\n");
            for (int i = 0; i < lines.length; i++) {
                String contentLine = lines[i];
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull ListAction action,
            @NotNull ActionContext context
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull ListAction action,
            @NotNull ActionContext context,
            int fromIndex
    ) {
        List<Action> actions = action.getActions();
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull ConditionAction action,
            @NotNull ActionContext context
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull LabelAction action,
            @NotNull ActionContext context
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull WeightAction action,
            @NotNull ActionContext context
    ) {
        if (action.getTotalWeight() <= 0 || action.getActions().isEmpty())
            return CompletableFuture.completedFuture(Results.SUCCESS);
        int amount = action.getAmount(this, context);
        if (amount >= action.getActions().size()) {
            CompletableFuture<ActionResult> result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (Pair<Action, Double> currentAction : action.getActions()) {
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
            List<SamplingUtils.SamplingResult<Action>> actions = SamplingUtils.weightWithIndex(action.getActions(), amount);
            if (action.isOrder()) {
                actions.sort(Comparator.comparingInt(SamplingUtils.SamplingResult::getIndex));
            }
            CompletableFuture<ActionResult> result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (SamplingUtils.SamplingResult<Action> currentAction : actions) {
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull ConditionWeightAction action,
            @NotNull ActionContext context
    ) {
        List<Pair<Action, Double>> actions = action.getActions(context);
        if (actions.isEmpty()) return CompletableFuture.completedFuture(Results.SUCCESS);
        int amount = action.getAmount(this, context);
        if (amount >= actions.size()) {
            CompletableFuture<ActionResult> result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (Pair<Action, Double> currentAction : actions) {
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
            Action result = SamplingUtils.weight(actions);
            if (result != null) {
                return result.evalAsyncSafe(this, context);
            }
        } else if (amount > 1) {
            List<SamplingUtils.SamplingResult<Action>> finalActions = SamplingUtils.weightWithIndex(actions, amount);
            if (action.isOrder()) {
                finalActions.sort(Comparator.comparingInt(SamplingUtils.SamplingResult::getIndex));
            }
            CompletableFuture<ActionResult> result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (SamplingUtils.SamplingResult<Action> currentAction : finalActions) {
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull WhileAction action,
            @NotNull ActionContext context
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
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull KeyAction action,
            @NotNull ActionContext context
    ) {
        String key = action.getKey().get(context);
        context.getGlobal().put(action.getGlobalId(), key);
        if (key == null) {
            return action.getDefaultAction().evalAsyncSafe(this, context);
        }
        Action targetAction = action.getActions().getOrDefault(key, action.getDefaultAction());
        return targetAction.evalAsyncSafe(this, context);
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @NotNull
    public <T extends Comparable<?>> CompletableFuture<ActionResult> runAction(
            @NotNull TreeAction<T> action,
            @NotNull ActionContext context
    ) {
        T key = action.getKey().get(context);
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
        Action targetAction = entry == null ? action.getDefaultAction() : entry.getValue();
        return targetAction.evalAsyncSafe(this, context);
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull RepeatAction action,
            @NotNull ActionContext context
    ) {
        final int repeat = action.getRepeat().getOrDefault(context, 0);
        return runAction(action, context, repeat, 0);
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @NotNull
    public CompletableFuture<ActionResult> runAction(
            @NotNull RepeatAction action,
            @NotNull ActionContext context,
            int repeat,
            int count
    ) {
        if (count < repeat) {
            context.getGlobal().put(action.getGlobalId(), count);
            return action.getActions().evalAsyncSafe(this, context).thenCompose((result) -> {
                if (result.getType() == ResultType.STOP) {
                    return CompletableFuture.completedFuture(result);
                } else {
                    return runAction(action, context, repeat, count + 1);
                }
            });
        } else {
            return CompletableFuture.completedFuture(Results.SUCCESS);
        }
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
    @NotNull
    public ActionResult parseCondition(
            @Nullable ScriptWithSource condition,
            @NotNull ActionContext context
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
    @NotNull
    public ActionResult parseCondition(
            @Nullable String conditionString,
            @Nullable CompiledScript condition,
            @NotNull ActionContext context
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
                String[] lines = conditionString.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String conditionLine = lines[i];
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
            @NotNull Collection<String> ids,
            @Nullable BiFunction<ActionContext, String, CompletableFuture<ActionResult>> function
    ) {
        if (function == null) return;
        for (String id : ids) {
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
            @NotNull Collection<String> ids,
            boolean asyncSafe,
            @Nullable BiFunction<ActionContext, String, CompletableFuture<ActionResult>> function
    ) {
        if (function == null) return;
        for (String id : ids) {
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
            @NotNull String id,
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
            @NotNull String id,
            boolean asyncSafe,
            @Nullable BiFunction<ActionContext, String, CompletableFuture<ActionResult>> function
    ) {
        if (function == null) return;
        BiFunction<ActionContext, String, CompletableFuture<ActionResult>> handler;
        if (asyncSafe) {
            handler = (context, content) -> {
                CompletableFuture<ActionResult> result = function.apply(context, content);
                if (result == null) result = CompletableFuture.completedFuture(Results.SUCCESS);
                return result;
            };
        } else {
            handler = (SyncActionHandler) (context, content) -> {
                CompletableFuture<ActionResult> result = function.apply(context, content);
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
            @NotNull Collection<String> ids,
            @Nullable BiConsumer<ActionContext, String> consumer
    ) {
        if (consumer == null) return;
        for (String id : ids) {
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
            @NotNull Collection<String> ids,
            boolean asyncSafe,
            @Nullable BiConsumer<ActionContext, String> consumer
    ) {
        if (consumer == null) return;
        for (String id : ids) {
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
            @NotNull String id,
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
            @NotNull String id,
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
            @NotNull String pluginName,
            @NotNull String resourceName
    ) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
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
            @NotNull Plugin plugin,
            @NotNull String resourceName
    ) {
        try (InputStream input = plugin.getResource(resourceName)) {
            if (input != null) {
                try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
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
            Player player = context.getPlayer();
            if (player == null) return;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', content));
        });
        // 向玩家发送消息(不将&解析为颜色符号)
        addConsumer(Arrays.asList("tell-no-color", "tellNoColor"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.sendMessage(content);
        });
        // 强制玩家发送消息
        addConsumer("chat", false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.chat(content);
        });
        // 强制玩家发送消息(将&解析为颜色符号)
        addConsumer(Arrays.asList("chat-with-color", "chatWithColor"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.chat(ChatColor.translateAlternateColorCodes('&', content));
        });
        // 强制玩家执行指令
        addConsumer(Arrays.asList("command", "player"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            Bukkit.dispatchCommand(player, ChatColor.translateAlternateColorCodes('&', content));
        });
        // 强制玩家执行指令(不将&解析为颜色符号)
        addConsumer(Arrays.asList("command-no-color", "commandNoColor"), false, (context, content) -> {
            Player player = context.getPlayer();
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
        addConsumer("broadcast", (context, content) -> {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', content));
        });
        // 公告(不将&解析为颜色符号)
        addConsumer(Arrays.asList("broadcast-no-color", "broadcastNoColor"), (context, content) -> {
            Bukkit.broadcastMessage(content);
        });
        // 发送Title
        addConsumer("title", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            ArrayList<String> args = StringUtils.split(ChatColor.translateAlternateColorCodes('&', content), ' ', '\\');
            String title = getOrNull(args, 0);
            String subtitle = getOrDefault(args, 1, "");
            int fadeIn = getAndApply(args, 2, 10, StringsKt::toIntOrNull);
            int stay = getAndApply(args, 3, 70, StringsKt::toIntOrNull);
            int fadeOut = getAndApply(args, 4, 20, StringsKt::toIntOrNull);
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        });
        // 发送Title(不将&解析为颜色符号)
        addConsumer(Arrays.asList("title-no-color", "titleNoColor"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            ArrayList<String> args = StringUtils.split(content, ' ', '\\');
            String title = getOrNull(args, 0);
            String subtitle = getOrDefault(args, 1, "");
            int fadeIn = getAndApply(args, 2, 10, StringsKt::toIntOrNull);
            int stay = getAndApply(args, 3, 70, StringsKt::toIntOrNull);
            int fadeOut = getAndApply(args, 4, 20, StringsKt::toIntOrNull);
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        });
        // 发送全体Title
        addConsumer("broadcast-title", false, (context, content) -> {
            ArrayList<String> args = StringUtils.split(ChatColor.translateAlternateColorCodes('&', content), ' ', '\\');
            String title = getOrNull(args, 0);
            String subtitle = getOrDefault(args, 1, "");
            int fadeIn = getAndApply(args, 2, 10, StringsKt::toIntOrNull);
            int stay = getAndApply(args, 3, 70, StringsKt::toIntOrNull);
            int fadeOut = getAndApply(args, 4, 20, StringsKt::toIntOrNull);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }
        });
        // 发送全体Title(不将&解析为颜色符号)
        addConsumer("broadcast-title-no-color", false, (context, content) -> {
            ArrayList<String> args = StringUtils.split(content, ' ', '\\');
            String title = getOrNull(args, 0);
            String subtitle = getOrDefault(args, 1, "");
            int fadeIn = getAndApply(args, 2, 10, StringsKt::toIntOrNull);
            int stay = getAndApply(args, 3, 70, StringsKt::toIntOrNull);
            int fadeOut = getAndApply(args, 4, 20, StringsKt::toIntOrNull);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }
        });
        // 发送ActionBar
        addConsumer("actionbar", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            PlayerUtils.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', content));
        });
        // 发送ActionBar(不将&解析为颜色符号)
        addConsumer(Arrays.asList("actionbar-no-color", "actionbarNoColor"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            PlayerUtils.sendActionBar(player, content);
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
        addConsumer(Arrays.asList("give-money", "giveMoney"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            VaultHooker hooker = HookerManager.INSTANCE.getVaultHooker();
            if (hooker == null) return;
            hooker.giveMoney(player, StringUtils.toDouble(content, 0.0));
        });
        // 扣除玩家金钱
        addConsumer(Arrays.asList("take-money", "takeMoney"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            VaultHooker hooker = HookerManager.INSTANCE.getVaultHooker();
            if (hooker == null) return;
            hooker.takeMoney(player, StringUtils.toDouble(content, 0.0));
        });
        // 给予玩家经验
        addConsumer(Arrays.asList("give-exp", "giveExp"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            HookerManager.INSTANCE.getNmsHooker().giveExp(player, StringUtils.toInt(content, 0));
        });
        // 扣除玩家经验
        addConsumer(Arrays.asList("take-exp", "takeExp"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            HookerManager.INSTANCE.getNmsHooker().giveExp(player, StringUtils.toInt(content, 0) * -1);
        });
        // 设置玩家经验
        addConsumer(Arrays.asList("set-exp", "setExp"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            HookerManager.INSTANCE.getNmsHooker().giveExp(player, StringUtils.toInt(content, 0) - player.getTotalExperience());
        });
        // 给予玩家经验等级
        addConsumer(Arrays.asList("give-level", "giveLevel"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.giveExpLevels(StringUtils.toInt(content, 0));
        });
        // 扣除玩家经验等级
        addConsumer(Arrays.asList("take-level", "takeLevel"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.giveExpLevels(StringUtils.toInt(content, 0) * -1);
        });
        // 设置玩家经验等级
        addConsumer(Arrays.asList("set-level", "setLevel"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setLevel(StringUtils.toInt(content, 0));
        });
        // 给予玩家饱食度
        addConsumer(Arrays.asList("give-food", "giveFood"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setFoodLevel(player.getFoodLevel() + Math.max(0, Math.min(20, StringUtils.toInt(content, 0))));
        });
        // 扣除玩家饱食度
        addConsumer(Arrays.asList("take-food", "takeFood"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setFoodLevel(player.getFoodLevel() - Math.max(0, Math.min(20, StringUtils.toInt(content, 0))));
        });
        // 设置玩家饱食度
        addConsumer(Arrays.asList("set-food", "setFood"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setFoodLevel(Math.max(0, Math.min(20, StringUtils.toInt(content, 0))));
        });
        // 给予玩家饱和度
        addConsumer(Arrays.asList("give-saturation", "giveSaturation"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), player.getSaturation() + StringUtils.toFloat(content, 0))));
        });
        // 扣除玩家饱和度
        addConsumer(Arrays.asList("take-saturation", "takeSaturation"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), player.getSaturation() - StringUtils.toFloat(content, 0))));
        });
        // 设置玩家饱和度
        addConsumer(Arrays.asList("set-saturation", "setSaturation"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), StringUtils.toFloat(content, 0))));
        });
        // 给予玩家生命
        addConsumer(Arrays.asList("give-health", "giveHealth"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setHealth(Math.max(0, Math.min(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), player.getHealth() + StringUtils.toDouble(content, 0))));
        });
        // 扣除玩家生命
        addConsumer(Arrays.asList("take-health", "takeHealth"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setHealth(Math.max(0, Math.min(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), player.getHealth() - StringUtils.toDouble(content, 0))));
        });
        // 设置玩家生命
        addConsumer(Arrays.asList("set-health", "setHealth"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            player.setHealth(Math.max(0, Math.min(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), StringUtils.toDouble(content, 0))));
        });
        // 释放MM技能
        addConsumer(Arrays.asList("cast-skill", "castSkill"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            MythicMobsHooker hooker = HookerManager.INSTANCE.getMythicMobsHooker();
            if (hooker == null) return;
            hooker.castSkill(player, content, player);
        });
        // 连击记录
        addConsumer("combo", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            String[] info = content.split(" ", 2);
            // 连击组
            String comboGroup = info[0];
            // 连击类型
            String comboType = getOrDefault(info, 1, "");
            // 为空则填入
            if (!PlayerUtils.hasMetadataEZ(player, "Combo-" + comboGroup)) {
                PlayerUtils.setMetadataEZ(player, "Combo-" + comboGroup, new ArrayList<ComboInfo>());
            }
            // 当前时间
            long time = System.currentTimeMillis();
            // 记录列表
            ArrayList<ComboInfo> comboInfos = (ArrayList<ComboInfo>) PlayerUtils.getMetadataEZ(player, "Combo-" + comboGroup, "");
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
        addConsumer(Arrays.asList("combo-clear", "comboClear"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            PlayerUtils.setMetadataEZ(player, "Combo-" + content, new ArrayList<ComboInfo>());
        });
        // 设置药水效果
        addConsumer(Arrays.asList("set-potion", "setPotion", "set-potion-effect", "setPotionEffect"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            String[] args = content.split(" ", 3);
            if (args.length < 3) return;
            PotionEffectType type = PotionEffectType.getByName(args[0].toUpperCase(Locale.ROOT));
            Integer amplifier = StringUtils.toIntOrNull(args[1]);
            Integer duration = StringUtils.toIntOrNull(args[2]);
            if (type == null || duration == null || amplifier == null) return;
            player.addPotionEffect(new PotionEffect(type, duration * 20, amplifier - 1), true);
        });
        // 移除药水效果
        addConsumer(Arrays.asList("remove-potion", "removePotion", "remove-potion-effect", "removePotionEffect"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            PotionEffectType type = PotionEffectType.getByName(content.toUpperCase(Locale.ROOT));
            if (type != null) return;
            player.removePotionEffect(type);
        });
        // 延迟(单位是tick)
        addFunction("delay", (context, content) -> {
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            SchedulerUtils.runLater(plugin, StringUtils.toInt(content, 0), () -> {
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
            String[] args = content.split(" ", 2);
            int priority = getAndApply(args, 0, 1, StringsKt::toIntOrNull);
            String label = getOrNull(args, 1);
            return CompletableFuture.completedFuture(new StopResult(label, priority));
        });
        // 向global中设置内容
        addConsumer(Arrays.asList("set-global", "setGlobal"), (context, content) -> {
            String[] args = content.split(" ", 2);
            if (args.length > 1) {
                context.getGlobal().put(args[0], args[1]);
            }
        });
        // 从global中删除内容
        addConsumer(Arrays.asList("del-global", "delGlobal"), (context, content) -> context.getGlobal().remove(content));
        // 扣除NI物品
        addConsumer(Arrays.asList("take-ni-item", "takeNiItem"), false, (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            String[] args = content.split(" ", 2);
            if (args.length < 2) return;
            String itemId = args[0];
            int amount = StringUtils.toInt(args[1], 0);
            ItemStack[] contents = player.getInventory().getContents();
            for (ItemStack itemStack : contents) {
                String currentItemId = ItemUtils.getItemId(itemStack);
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
            Player player = context.getPlayer();
            if (player == null) return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
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
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            Bukkit.getScheduler().runTask(plugin, () -> result.complete(Results.SUCCESS));
            return result;
        });
        // 切换至异步线程
        addFunction("async", (context, content) -> {
            context.setSync(false);
            if (!Bukkit.isPrimaryThread()) {
                return CompletableFuture.completedFuture(Results.SUCCESS);
            }
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> result.complete(Results.SUCCESS));
            return result;
        });
        // 聊天捕获器
        addFunction(Arrays.asList("catch-chat", "catchChat"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            ArrayList<String> args = StringUtils.split(content, ' ', '\\');
            String messageKey = getOrDefault(args, 0, "catchChat");
            boolean cancel = getAndApply(args, 1, true, StringsKt::toBooleanStrictOrNull);
            User user = UserManager.INSTANCE.get(player.getUniqueId());
            if (user == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            user.addChatCatcher(new ChatCatcher(this, messageKey, cancel, context, result));
            return result;
        });
        // 告示牌捕获器
        addFunction(Arrays.asList("catch-sign", "catchSign"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            User user = UserManager.INSTANCE.get(player.getUniqueId());
            if (user == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            user.addSignCatcher(new SignCatcher(this, content, context, result));
            EntityPlayerUtils.openSign(player);
            return result;
        });
        // 清除聊天捕获器
        addConsumer("clear-catch-chat", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            User user = UserManager.INSTANCE.get(player.getUniqueId());
            if (user == null) return;
            user.clearChatCatcher();
        });
        // 清除告示牌捕获器
        addConsumer("clear-catch-sign", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            User user = UserManager.INSTANCE.get(player.getUniqueId());
            if (user == null) return;
            user.clearSignCatcher();
        });
        // 触发动作组
        addFunction("func", (context, content) -> {
            Action function = ActionManager.INSTANCE.getFunctions().get(content);
            if (function == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            return function.eval(context);
        });
    }
}
