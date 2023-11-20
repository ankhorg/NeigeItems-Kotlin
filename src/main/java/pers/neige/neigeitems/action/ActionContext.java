package pers.neige.neigeitems.action;

import bot.inker.bukkit.nbt.NbtCompound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionContext {
    private static final ActionContext EMPTY = new ActionContext();
    @NotNull
    private final Bindings basicBindings;
    @Nullable
    private final Player player;
    @NotNull
    private final Map<String, Object> global;
    @Nullable
    private final Map<String, Object> params;
    @Nullable
    private final ItemStack itemStack;
    @Nullable
    private final NbtCompound nbt;
    @Nullable
    private final Map<String, String> data;
    @Nullable
    private final Event event;

    public ActionContext() {
        this(null);
    }

    public ActionContext(
            @Nullable Player player
    ) {
        this(player, null);
    }

    public ActionContext(
            @Nullable Player player,
            @Nullable Map<String, Object> params
    ) {
        this(player, null, params);
    }

    public ActionContext(
            @Nullable Player player,
            @Nullable Map<String, Object> global,
            @Nullable Map<String, Object> params
    ) {
        this(player, global, params, null, null, null, null);
    }

    public ActionContext(
            @Nullable Player player,
            @Nullable Map<String, Object> global,
            @Nullable Map<String, Object> params,
            @Nullable ItemStack itemStack,
            @Nullable NbtCompound nbt,
            @Nullable Map<String, String> data
    ) {
        this(player, global, params, itemStack, nbt, data, null);
    }

    public ActionContext(
            @Nullable Player player,
            @Nullable Map<String, Object> global,
            @Nullable Map<String, Object> params,
            @Nullable ItemStack itemStack,
            @Nullable NbtCompound nbt,
            @Nullable Map<String, String> data,
            @Nullable Event event
    ) {
        this.player = player;
        if (global == null) {
            this.global = new HashMap<>();
        } else {
            this.global = global;
        }
        this.params = params;
        this.itemStack = itemStack;
        this.nbt = nbt;
        this.data = data;
        this.event = event;
        basicBindings = toBindings();
    }

    /**
     * 返回一个空空荡荡的 ActionContext.
     */
    @NotNull
    public static ActionContext empty() {
        return EMPTY;
    }

    /**
     * 根据当前内容构建一个用于传入 js 的 Bindings.
     */
    @NotNull
    private Bindings toBindings() {
        Bindings bindings = new SimpleBindings();
        if (params != null) {
            params.forEach((key, value) -> {
                if (value != null) {
                    bindings.put(key, value);
                }
            });
        }
        if (player != null) {
            bindings.put("player", player);
        }
        if (itemStack != null) {
            bindings.put("itemStack", itemStack);
        }
        if (nbt != null) {
            bindings.put("itemTag", nbt);
            bindings.put("nbt", nbt);
        }
        if (data != null) {
            bindings.put("data", data);
        }
        if (event != null) {
            bindings.put("event", event);
        }
        bindings.put("global", global);
        bindings.put("glo", global);
        bindings.put("context", this);
        return bindings;
    }

    /**
     * 修改 params 后请调用该方法刷新 Bindings.
     */
    public void refreshParams() {
        if (params != null) {
            params.forEach((key, value) -> {
                if (value != null) {
                    basicBindings.put(key, value);
                }
            });
        }
    }

    /**
     * 获取用于传入 js 的 Bindings.
     */
    @NotNull
    public Bindings getBindings() {
        Bindings bindings = new SimpleBindings();
        Map<String, Object> vars = new HashMap<>();
        bindings.put("variables", vars);
        bindings.put("vars", vars);
        bindings.putAll(basicBindings);
        return bindings;
    }

    /**
     * 获取触发动作的玩家.
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    /**
     * 获取 js 动作及 condition 中调用的名为 global 的 Map.
     */
    @NotNull
    public Map<String, Object> getGlobal() {
        return global;
    }

    /**
     * 获取准备放入 js 中的变量.
     */
    @Nullable
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * 获取触发动作的物品.
     */
    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 获取触发动作物品的 NBT.
     */
    @Nullable
    public NbtCompound getNbt() {
        return nbt;
    }

    /**
     * 获取触发动作物品的 NeigeItems 节点信息.
     */
    @Nullable
    public Map<String, String> getData() {
        return data;
    }

    /**
     * 获取触发动作的事件.
     */
    @Nullable
    public Event getEvent() {
        return event;
    }
}
