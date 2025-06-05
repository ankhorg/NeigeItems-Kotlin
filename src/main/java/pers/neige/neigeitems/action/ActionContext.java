package pers.neige.neigeitems.action;

import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.Map;

public class ActionContext implements Cloneable {
    private final @NonNull Bindings basicBindings;
    private final @Nullable Player player;
    private final @NonNull Map<String, Object> global;
    private final @Nullable Map<String, Object> params;
    private final @Nullable ItemStack itemStack;
    private final @Nullable NbtCompound nbt;
    private final @Nullable Map<String, String> data;
    private final @Nullable Event event;
    private boolean sync = Bukkit.isPrimaryThread();

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
    public static @NonNull ActionContext empty() {
        return new ActionContext();
    }

    @Override
    public ActionContext clone() {
        try {
            val result = (ActionContext) super.clone();
            result.setSync(Bukkit.isPrimaryThread());
            return result;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据当前内容构建一个用于传入 js 的 Bindings.
     */
    private @NonNull Bindings toBindings() {
        val bindings = new SimpleBindings();
        if (params != null) {
            bindings.putAll(params);
        }
        bindings.put("player", player);
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
    public @NonNull Bindings getBindings() {
        val bindings = new SimpleBindings();
        val vars = new HashMap<String, Object>();
        bindings.put("variables", vars);
        bindings.put("vars", vars);
        bindings.putAll(basicBindings);
        return bindings;
    }

    /**
     * 获取触发动作的玩家.
     */
    public @Nullable Player getPlayer() {
        return player;
    }

    /**
     * 获取 js 动作及 condition 中调用的名为 global 的 Map.
     */
    public @NonNull Map<String, Object> getGlobal() {
        return global;
    }

    /**
     * 获取准备放入 js 中的变量.
     */
    public @Nullable Map<String, Object> getParams() {
        return params;
    }

    /**
     * 获取触发动作的物品.
     */
    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 获取触发动作物品的 NBT.
     */
    public @Nullable NbtCompound getNbt() {
        return nbt;
    }

    /**
     * 获取触发动作物品的 NeigeItems 节点信息.
     */
    public @Nullable Map<String, String> getData() {
        return data;
    }

    /**
     * 获取触发动作的事件.
     */
    public @Nullable Event getEvent() {
        return event;
    }

    /**
     * 获取动作是否应该在主线程运行.
     */
    public boolean isSync() {
        return sync;
    }

    /**
     * 设置动作是否应该在主线程运行.
     */
    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
