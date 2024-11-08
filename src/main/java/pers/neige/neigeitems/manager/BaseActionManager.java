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
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ResultType;
import pers.neige.neigeitems.action.catcher.ChatCatcher;
import pers.neige.neigeitems.action.catcher.SignCatcher;
import pers.neige.neigeitems.action.impl.*;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.action.result.StopResult;
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker;
import pers.neige.neigeitems.hook.placeholderapi.PapiHooker;
import pers.neige.neigeitems.hook.vault.VaultHooker;
import pers.neige.neigeitems.item.action.ComboInfo;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils;
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

public abstract class BaseActionManager {
    @NotNull
    private final Plugin plugin;
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
            @Nullable Object action
    ) {
        if (action instanceof String) {
            String string = (String) action;
            String[] info = string.split(": ", 2);
            String key = info[0].toLowerCase(Locale.ROOT);
            String content = HookerManager.toSection(info.length > 1 ? info[1] : "", false);
            if (key.equals("js")) {
                return new JsAction(this, content);
            } else {
                return new StringAction(string, key, content);
            }
        } else if (action instanceof List<?>) {
            return new ListAction(this, (List<?>) action);
        } else if (action instanceof Map<?, ?>) {
            Map<?, ?> current = (Map<?, ?>) action;
            if (current.containsKey("type")) {
                Object rawType = current.get("type");
                if (rawType instanceof String) {
                    String type = ((String) rawType).toLowerCase();
                    switch (type) {
                        case "condition": {
                            return new ConditionAction(this, current);
                        }
                        case "condition-weight": {
                            return new ConditionWeightAction(this, current);
                        }
                        case "label": {
                            return new LabelAction(this, current);
                        }
                        case "weight": {
                            return new WeightAction(this, current);
                        }
                        case "while": {
                            return new WhileAction(this, current);
                        }
                    }
                }
            }
            if (current.containsKey("condition")) {
                return new ConditionAction(this, current);
            } else if (current.containsKey("while")) {
                return new WhileAction(this, current);
            } else if (current.containsKey("label")) {
                return new LabelAction(this, current);
            }
        } else if (action instanceof ConfigurationSection) {
            ConfigurationSection current = (ConfigurationSection) action;
            if (current.contains("type")) {
                String type = current.getString("type");
                if (type != null) {
                    type = type.toLowerCase();
                    switch (type) {
                        case "condition": {
                            return new ConditionAction(this, current);
                        }
                        case "condition-weight": {
                            return new ConditionWeightAction(this, current);
                        }
                        case "label": {
                            return new LabelAction(this, current);
                        }
                        case "weight": {
                            return new WeightAction(this, current);
                        }
                        case "while": {
                            return new WhileAction(this, current);
                        }
                    }
                }
            }
            if (current.contains("condition")) {
                return new ConditionAction(this, current);
            } else if (current.contains("while")) {
                return new WhileAction(this, current);
            } else if (current.contains("label")) {
                return new LabelAction(this, current);
            }
        }
        return NullAction.INSTANCE;
    }

    /**
     * 执行动作
     *
     * @param action 动作内容
     * @return 永远返回 Results.SUCCESS, 这是历史遗留问题, 要获取真实结果请调用 runActionWithResult 方法.
     */
    @NotNull
    public ActionResult runAction(
            @NotNull Action action
    ) {
        runAction(action, ActionContext.empty());
        return Results.SUCCESS;
    }

    /**
     * 执行动作
     *
     * @param action 动作内容
     * @return 执行结果
     */
    @NotNull
    public CompletableFuture<ActionResult> runActionWithResult(
            @NotNull Action action
    ) {
        return runActionWithResult(action, ActionContext.empty());
    }

    /**
     * 执行动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 永远返回 Results.SUCCESS, 这是历史遗留问题, 要获取真实结果请调用 runActionWithResult 方法.
     */
    @NotNull
    public ActionResult runAction(
            @NotNull Action action,
            @NotNull ActionContext context
    ) {
        action.eval(this, context);
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
    public CompletableFuture<ActionResult> runActionWithResult(
            @NotNull Action action,
            @NotNull ActionContext context
    ) {
        return action.eval(this, context);
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
        BiFunction<ActionContext, String, CompletableFuture<ActionResult>> handler = actions.get(action.getKey());
        if (handler != null) {
            return handler.apply(context, action.getContent());
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
            @NotNull StringAction action,
            @NotNull ActionContext context
    ) {
        BiFunction<ActionContext, String, CompletableFuture<ActionResult>> handler = actions.get(action.getKey());
        if (handler != null) {
            String content = SectionUtils.parseSection(
                    action.getContent(),
                    (Map<String, String>) (Object) context.getGlobal(),
                    context.getPlayer(),
                    getSectionConfig(context)
            );
            return handler.apply(context, content);
        }
        return CompletableFuture.completedFuture(Results.SUCCESS);
    }

    /**
     * 执行 StringAction 进行节点解析的时候传入的节点配置
     *
     * @param context 动作上下文
     * @return 执行结果
     */
    @Nullable
    protected ConfigurationSection getSectionConfig(@NotNull ActionContext context) {
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
        return actions.get(fromIndex).eval(this, context).thenCompose((result) -> {
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
        if (parseCondition(action.getConditionString(), action.getCondition(), context).getType() == ResultType.SUCCESS) {
            // 执行动作
            SchedulerUtils.sync(plugin, () -> action.getSync().eval(this, context));
            SchedulerUtils.async(plugin, () -> action.getAsync().eval(this, context));
            return action.getActions().eval(this, context);
            // 条件未通过
        } else {
            // 执行deny动作
            return action.getDeny().eval(this, context);
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
        return action.getActions().eval(this, context).thenApply((result) -> {
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
                result = result.thenCombine(currentAction.getFirst().eval(this, context), (r1, r2) -> {
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
                return result.eval(this, context);
            }
        } else if (amount > 1) {
            List<SamplingUtils.SamplingResult<Action>> actions = SamplingUtils.weightWithIndex(action.getActions(), amount);
            if (action.isOrder()) {
                actions.sort(Comparator.comparingInt(SamplingUtils.SamplingResult::getIndex));
            }
            CompletableFuture<ActionResult> result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (SamplingUtils.SamplingResult<Action> currentAction : actions) {
                result = result.thenCombine(currentAction.getValue().eval(this, context), (r1, r2) -> {
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
        List<Pair<Action, Double>> actions = action.getActions(this, context);
        if (actions.isEmpty()) return CompletableFuture.completedFuture(Results.SUCCESS);
        int amount = action.getAmount(this, context);
        if (amount >= actions.size()) {
            CompletableFuture<ActionResult> result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (Pair<Action, Double> currentAction : actions) {
                result = result.thenCombine(currentAction.getFirst().eval(this, context), (r1, r2) -> {
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
                return result.eval(this, context);
            }
        } else if (amount > 1) {
            List<SamplingUtils.SamplingResult<Action>> finalActions = SamplingUtils.weightWithIndex(actions, amount);
            if (action.isOrder()) {
                finalActions.sort(Comparator.comparingInt(SamplingUtils.SamplingResult::getIndex));
            }
            CompletableFuture<ActionResult> result = CompletableFuture.completedFuture(Results.SUCCESS);
            for (SamplingUtils.SamplingResult<Action> currentAction : finalActions) {
                result = result.thenCombine(currentAction.getValue().eval(this, context), (r1, r2) -> {
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
        if (parseCondition(action.getConditionString(), action.getCondition(), context).getType() == ResultType.SUCCESS) {
            return action.getActions().eval(this, context).thenCompose((result) -> {
                // 执行中止
                if (result.getType() == ResultType.STOP) {
                    // 执行finally块
                    return action.getFinally().eval(this, context);
                } else {
                    // 继续执行
                    return runAction(action, context);
                }
            });
        } else {
            // 执行finally块
            return action.getFinally().eval(this, context);
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
     * @param id       动作ID
     * @param function 动作执行函数
     */
    public void addFunction(
            @NotNull String id,
            @Nullable BiFunction<ActionContext, String, CompletableFuture<ActionResult>> function
    ) {
        if (function == null) return;
        actions.put(id.toLowerCase(Locale.ROOT), (context, content) -> {
            CompletableFuture<ActionResult> result = function.apply(context, content);
            if (result == null) result = CompletableFuture.completedFuture(Results.SUCCESS);
            return result;
        });
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
     * @param id       动作ID
     * @param consumer 动作执行函数
     */
    public void addConsumer(
            @NotNull String id,
            @Nullable BiConsumer<ActionContext, String> consumer
    ) {
        if (consumer == null) return;
        actions.put(id.toLowerCase(Locale.ROOT), (context, content) -> {
            consumer.accept(context, content);
            return CompletableFuture.completedFuture(Results.SUCCESS);
        });
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
        addFunction("chat", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.chat(content);
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 强制玩家发送消息(将&解析为颜色符号)
        addFunction(Arrays.asList("chat-with-color", "chatWithColor"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.chat(ChatColor.translateAlternateColorCodes('&', content));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 强制玩家执行指令
        addFunction("command", (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                Bukkit.dispatchCommand(player, ChatColor.translateAlternateColorCodes('&', content));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 强制玩家执行指令
        addFunction("player", actions.get("command"));
        // 强制玩家执行指令(不将&解析为颜色符号)
        addFunction(Arrays.asList("command-no-color", "commandNoColor"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                Bukkit.dispatchCommand(player, content);
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 后台执行指令
        addFunction("console", (context, content) -> {
            Player player = context.getPlayer();
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            if (player != null) {
                SchedulerUtils.sync(plugin, () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', content));
                    SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
                });
            } else {
                SchedulerUtils.sync(plugin, () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', content));
                    SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
                });
            }
            return result;
        });
        // 后台执行指令(不将&解析为颜色符号)
        addFunction(Arrays.asList("console-no-color", "consoleNoColor"), (context, content) -> {
            Player player = context.getPlayer();
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            if (player != null) {
                SchedulerUtils.sync(plugin, () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), content);
                    SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
                });
            } else {
                SchedulerUtils.sync(plugin, () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), content);
                    SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
                });
            }
            return result;
        });
        // 公告
        addConsumer("broadcast", (context, content) -> {
            Player player = context.getPlayer();
            if (player != null) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', content));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', content));
            }
        });
        // 公告(不将&解析为颜色符号)
        addConsumer(Arrays.asList("broadcast-no-color", "broadcastNoColor"), (context, content) -> {
            Player player = context.getPlayer();
            if (player != null) {
                Bukkit.broadcastMessage(content);
            } else {
                Bukkit.broadcastMessage(content);
            }
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
            if (hooker != null) {
                hooker.giveMoney(player, StringUtils.toDouble(content, 0.0));
            }
        });
        // 扣除玩家金钱
        addConsumer(Arrays.asList("take-money", "takeMoney"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return;
            VaultHooker hooker = HookerManager.INSTANCE.getVaultHooker();
            if (hooker != null) {
                hooker.takeMoney(player, StringUtils.toDouble(content, 0.0));
            }
        });
        // 给予玩家经验
        addFunction(Arrays.asList("give-exp", "giveExp"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.giveExp(StringUtils.toInt(content, 0));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 扣除玩家经验
        addFunction(Arrays.asList("take-exp", "takeExp"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.giveExp(StringUtils.toInt(content, 0) * -1);
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 设置玩家经验
        addFunction(Arrays.asList("set-exp", "setExp"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setTotalExperience(StringUtils.toInt(content, 0));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 给予玩家经验等级
        addFunction(Arrays.asList("give-level", "giveLevel"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.giveExpLevels(StringUtils.toInt(content, 0));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 扣除玩家经验等级
        addFunction(Arrays.asList("take-level", "takeLevel"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.giveExpLevels(StringUtils.toInt(content, 0) * -1);
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 设置玩家经验等级
        addFunction(Arrays.asList("set-level", "setLevel"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setLevel(StringUtils.toInt(content, 0));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 给予玩家饱食度
        addFunction(Arrays.asList("give-food", "giveFood"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setFoodLevel(player.getFoodLevel() + Math.max(0, Math.min(20, StringUtils.toInt(content, 0))));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 扣除玩家饱食度
        addFunction(Arrays.asList("take-food", "takeFood"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setFoodLevel(player.getFoodLevel() - Math.max(0, Math.min(20, StringUtils.toInt(content, 0))));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 设置玩家饱食度
        addFunction(Arrays.asList("set-food", "setFood"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setFoodLevel(Math.max(0, Math.min(20, StringUtils.toInt(content, 0))));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 给予玩家饱和度
        addFunction(Arrays.asList("give-saturation", "giveSaturation"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), player.getSaturation() + StringUtils.toFloat(content, 0))));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 扣除玩家饱和度
        addFunction(Arrays.asList("take-saturation", "takeSaturation"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), player.getSaturation() - StringUtils.toFloat(content, 0))));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 设置玩家饱和度
        addFunction(Arrays.asList("set-saturation", "setSaturation"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setSaturation(Math.max(0, Math.min(player.getFoodLevel(), StringUtils.toFloat(content, 0))));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 给予玩家生命
        addFunction(Arrays.asList("give-health", "giveHealth"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setHealth(Math.max(0, Math.min(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), player.getHealth() + StringUtils.toDouble(content, 0))));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 扣除玩家生命
        addFunction(Arrays.asList("take-health", "takeHealth"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setHealth(Math.max(0, Math.min(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), player.getHealth() - StringUtils.toDouble(content, 0))));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 设置玩家生命
        addFunction(Arrays.asList("set-health", "setHealth"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
                player.setHealth(Math.max(0, Math.min(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), StringUtils.toDouble(content, 0))));
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
        });
        // 释放MM技能
        addConsumer(Arrays.asList("cast-skill", "castSkill"), (context, content) -> {
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
        addFunction(Arrays.asList("set-potion", "setPotion", "set-potion-effect", "setPotionEffect"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            String[] args = content.split(" ", 3);
            if (args.length == 3) {
                PotionEffectType type = PotionEffectType.getByName(args[0].toUpperCase(Locale.ROOT));
                Integer amplifier = StringUtils.toIntOrNull(args[1]);
                Integer duration = StringUtils.toIntOrNull(args[2]);
                if (type != null && duration != null && amplifier != null) {
                    CompletableFuture<ActionResult> result = new CompletableFuture<>();
                    boolean isPrimaryThread = Bukkit.isPrimaryThread();
                    SchedulerUtils.sync(plugin, () -> {
                        player.addPotionEffect(new PotionEffect(type, duration * 20, amplifier - 1), true);
                        SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
                    });
                    return result;
                }
            }
            return CompletableFuture.completedFuture(Results.SUCCESS);
        });
        // 移除药水效果
        addFunction(Arrays.asList("remove-potion", "removePotion", "remove-potion-effect", "removePotionEffect"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            PotionEffectType type = PotionEffectType.getByName(content.toUpperCase(Locale.ROOT));
            if (type != null) {
                CompletableFuture<ActionResult> result = new CompletableFuture<>();
                boolean isPrimaryThread = Bukkit.isPrimaryThread();
                SchedulerUtils.sync(plugin, () -> {
                    player.removePotionEffect(type);
                    SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
                });
            }
            return CompletableFuture.completedFuture(Results.SUCCESS);
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
            String[] info = content.split(" ", 2);
            if (info.length > 1) {
                context.getGlobal().put(info[0], info[1]);
            }
        });
        // 从global中删除内容
        addConsumer(Arrays.asList("del-global", "delGlobal"), (context, content) -> context.getGlobal().remove(content));
        // 扣除NI物品
        addFunction(Arrays.asList("take-ni-item", "takeNiItem"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            String parsedContent = content;
            String[] args = parsedContent.split(" ", 2);
            if (args.length < 2) return CompletableFuture.completedFuture(Results.SUCCESS);
            String itemId = args[0];
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            boolean isPrimaryThread = Bukkit.isPrimaryThread();
            SchedulerUtils.sync(plugin, () -> {
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
                        amount = 0;
                        break;
                    }
                }
                SchedulerUtils.run(plugin, isPrimaryThread, () -> result.complete(Results.SUCCESS));
            });
            return result;
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
            if (Bukkit.isPrimaryThread()) {
                return CompletableFuture.completedFuture(Results.SUCCESS);
            }
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            Bukkit.getScheduler().runTask(plugin, () -> result.complete(Results.SUCCESS));
            return result;
        });
        // 切换至异步线程
        addFunction("async", (context, content) -> {
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
            User user = NeigeItems.getUserManager().get(player.getUniqueId());
            if (user == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            user.addChatCatcher(new ChatCatcher(this, messageKey, cancel, context, result));
            return result;
        });
        // 告示牌捕获器
        addFunction(Arrays.asList("catch-sign", "catchSign"), (context, content) -> {
            Player player = context.getPlayer();
            if (player == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            User user = NeigeItems.getUserManager().get(player.getUniqueId());
            if (user == null) return CompletableFuture.completedFuture(Results.SUCCESS);
            CompletableFuture<ActionResult> result = new CompletableFuture<>();
            user.addSignCatcher(new SignCatcher(this, content, context, result));
            EntityPlayerUtils.openSign(player);
            return result;
        });
    }
}
