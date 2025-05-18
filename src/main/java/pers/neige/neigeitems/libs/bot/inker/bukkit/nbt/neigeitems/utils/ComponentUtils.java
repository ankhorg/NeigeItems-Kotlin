package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.RefMinecraftKey;
import pers.neige.neigeitems.ref.core.component.RefDataComponentPatch;
import pers.neige.neigeitems.ref.core.component.RefDataComponentType;
import pers.neige.neigeitems.ref.nbt.RefBukkitItemStack;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;
import pers.neige.neigeitems.ref.registry.RefBuiltInRegistries;

import java.util.List;
import java.util.Optional;

public class ComponentUtils {
    /**
     * 1.20.5+ 版本起, Mojang献祭了自己的亲妈, 换来了物品格式的改动.
     */
    private final static boolean MOJANG_MOTHER_DEAD = CbVersion.v1_20_R4.isSupport();

    /**
     * 1.21+版本, 根据 ResourceLocation 获取对应的 DataComponentType.
     *
     * @param key 可解析为 ResourceLocation 的文本.
     * @return DataComponentType.
     */
    public static Object getDataComponentType(String key) {
        return getDataComponentType0(key);
    }

    protected static RefDataComponentType<?> getDataComponentType0(String key) {
        if (!MOJANG_MOTHER_DEAD) return null;
        return RefBuiltInRegistries.DATA_COMPONENT_TYPE.getValue(RefMinecraftKey.parse(key));
    }

    /**
     * 1.21+版本, 根据 DataComponentType 获取对应的 ResourceLocation.
     *
     * @param type DataComponentType.
     * @return ResourceLocation.
     */
    public static Object getKeyByType(Object type) {
        return getKeyByType0((RefDataComponentType<?>) type);
    }

    protected static RefMinecraftKey getKeyByType0(RefDataComponentType<?> value) {
        if (!MOJANG_MOTHER_DEAD) return null;
        return RefBuiltInRegistries.DATA_COMPONENT_TYPE.getKey(value);
    }

    /**
     * 1.21+版本, 根据组件ID覆盖指定组件.
     *
     * @param receiver   组件接收者.
     * @param provider   组件提供者.
     * @param components 需要覆盖的组件.
     */
    public static void overrideComponent(@NotNull ItemStack receiver, @NotNull ItemStack provider, @NotNull List<String> components) {
        if (!MOJANG_MOTHER_DEAD) return;
        if (!(receiver instanceof RefCraftItemStack)) receiver = ((RefBukkitItemStack) (Object) receiver).craftDelegate;
        if (!(provider instanceof RefCraftItemStack)) provider = ((RefBukkitItemStack) (Object) provider).craftDelegate;
        RefCraftItemStack receiverCraft = (RefCraftItemStack) receiver;
        RefCraftItemStack providerCraft = (RefCraftItemStack) provider;
        RefNmsItemStack receiverNms = receiverCraft.handle;
        RefNmsItemStack providerNms = providerCraft.handle;
        RefDataComponentPatch patch = providerNms.getComponentsPatch();
        components.forEach(componentId -> {
            RefDataComponentType<?> componentType = getDataComponentType0(componentId);
            if (componentType == null) return;
            Optional<?> optional = patch.get(componentType);
            Object component = optional == null ? null : optional.orElse(null);
            if (component == null) {
                receiverNms.remove(componentType);
            } else {
                receiverNms.set((RefDataComponentType<? super Object>) componentType, component);
            }
        });
    }
}
