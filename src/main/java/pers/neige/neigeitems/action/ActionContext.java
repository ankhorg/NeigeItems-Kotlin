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
    private final @NonNull Bindings bindings;
    private final @Nullable Object caster;
    private final @NonNull Map<String, Object> global;
    private final @Nullable Map<String, Object> params;
    private final Map<ContextKey<?>, Object> values = new HashMap<>();
    private boolean sync = Bukkit.isPrimaryThread();

    public ActionContext() {
        this(null);
    }

    public ActionContext(
        @Nullable Object caster
    ) {
        this(caster, null, null);
    }

    public ActionContext(
        @Nullable Object caster,
        @Nullable Map<String, Object> global,
        @Nullable Map<String, Object> params
    ) {
        this.caster = caster;
        if (global == null) {
            this.global = new HashMap<>();
        } else {
            this.global = global;
        }
        this.params = params;
        this.bindings = toBindings();
    }

    public ActionContext(
        @Nullable Player caster
    ) {
        this((Object) caster, null, null);
    }

    public ActionContext(
        @Nullable Player caster,
        @Nullable Map<String, Object> global,
        @Nullable Map<String, Object> params
    ) {
        this((Object) caster, global, params);
    }

    @Deprecated
    public ActionContext(
        @Nullable Player caster,
        @Nullable Map<String, Object> params
    ) {
        this(caster, null, params);
    }

    @Deprecated
    public ActionContext(
        @Nullable Player caster,
        @Nullable Map<String, Object> global,
        @Nullable Map<String, Object> params,
        @Nullable ItemStack itemStack,
        @Nullable NbtCompound nbt,
        @Nullable Map<String, String> data
    ) {
        this.caster = caster;
        if (global == null) {
            this.global = new HashMap<>();
        } else {
            this.global = global;
        }
        this.params = params;
        this.values.put(ContextKeys.ITEM_STACK, itemStack);
        this.values.put(ContextKeys.NBT, nbt);
        this.values.put(ContextKeys.DATA, data);
        this.bindings = toBindings();
    }

    @Deprecated
    public ActionContext(
        @Nullable Player caster,
        @Nullable Map<String, Object> global,
        @Nullable Map<String, Object> params,
        @Nullable ItemStack itemStack,
        @Nullable NbtCompound nbt,
        @Nullable Map<String, String> data,
        @Nullable Event event
    ) {
        this.caster = caster;
        if (global == null) {
            this.global = new HashMap<>();
        } else {
            this.global = global;
        }
        this.params = params;
        this.values.put(ContextKeys.ITEM_STACK, itemStack);
        this.values.put(ContextKeys.NBT, nbt);
        this.values.put(ContextKeys.DATA, data);
        this.values.put(ContextKeys.EVENT, event);
        this.bindings = toBindings();
    }

    /**
     * 返回一个空空荡荡的 ActionContext.
     */
    public static @NonNull ActionContext empty() {
        return new ActionContext();
    }

    public static Builder builder() {
        return new Builder();
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
        bindings.put("target", caster);
        if (caster instanceof Player) {
            bindings.put("player", caster);
        } else {
            bindings.put("player", null);
        }
        values.forEach((key, value) -> {
            key.getNames().forEach((alias) -> {
                if (key.isPutInGlobal()) global.put(alias, value);
                bindings.put(alias, value);
            });
        });
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
                    bindings.put(key, value);
                }
            });
        }
    }

    /**
     * 获取用于传入 js 的 Bindings.
     */
    public @NonNull Bindings getBindings() {
        return bindings;
    }

    /**
     * 获取动作执行者.
     */
    public @Nullable Object getCaster() {
        return caster;
    }

    /**
     * 获取执行动作的玩家.
     */
    public @Nullable Player getPlayer() {
        return caster instanceof Player ? (Player) caster : null;
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

    public <T> void set(@NonNull ContextKey<T> key, @Nullable T value) {
        values.put(key, value);
        key.getNames().forEach((alias) -> {
            if (key.isPutInGlobal()) global.put(alias, value);
            bindings.put(alias, value);
        });
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(@NonNull ContextKey<T> key) {
        return (T) values.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T remove(@NonNull ContextKey<T> key) {
        key.getNames().forEach((alias) -> {
            if (key.isPutInGlobal()) global.remove(alias);
            bindings.remove(alias);
        });
        return (T) values.remove(key);
    }

    /**
     * 获取触发动作的物品.
     */
    public @Nullable ItemStack getItemStack() {
        return get(ContextKeys.ITEM_STACK);
    }

    /**
     * 获取触发动作物品的 NBT.
     */
    public @Nullable NbtCompound getNbt() {
        return get(ContextKeys.NBT);
    }

    /**
     * 获取触发动作物品的 NeigeItems 节点信息.
     */
    public @Nullable Map<String, String> getData() {
        return get(ContextKeys.DATA);
    }

    /**
     * 获取触发动作的事件.
     */
    public @Nullable Event getEvent() {
        return get(ContextKeys.EVENT);
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

    public static class Builder {
        private final @NonNull Map<ContextKey<?>, Object> values = new HashMap<>();
        private @Nullable Object caster;
        private @Nullable Map<String, Object> global;
        private @Nullable Map<String, Object> params;

        public Builder caster(Object caster) {
            this.caster = caster;
            return this;
        }

        public Builder global(Map<String, Object> global) {
            this.global = global;
            return this;
        }

        public Builder params(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public <T> Builder with(@NonNull ContextKey<T> key, @Nullable T value) {
            values.put(key, value);
            return this;
        }

        public ActionContext build() {
            ActionContext context = new ActionContext(caster, global, params);
            values.forEach((key, value) -> {
                context.values.put(key, value);
                key.getNames().forEach((alias) -> {
                    if (key.isPutInGlobal()) context.global.put(alias, value);
                    context.bindings.put(alias, value);
                });
            });
            return context;
        }
    }
}
