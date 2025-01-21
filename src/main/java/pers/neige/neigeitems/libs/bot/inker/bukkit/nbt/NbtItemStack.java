package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtComponentLike;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.invoke.InvokeUtil;
import pers.neige.neigeitems.ref.core.component.RefDataComponentHolder;
import pers.neige.neigeitems.ref.core.component.RefDataComponents;
import pers.neige.neigeitems.ref.nbt.*;
import pers.neige.neigeitems.ref.world.item.component.RefCustomData;

import java.util.Map;

public final class NbtItemStack {
    /**
     * 1.20.5+ 版本起, Mojang献祭了自己的亲妈, 换来了物品格式的改动.
     */
    private final static boolean MOJANG_MOTHER_DEAD = CbVersion.v1_20_R4.isSupport();

    private final ItemStack itemStack;
    private final RefCraftItemStack craftItemStack;

    public NbtItemStack(@NotNull ItemStack itemStack) {
        if (itemStack instanceof RefCraftItemStack) {
            this.itemStack = itemStack;
            this.craftItemStack = (RefCraftItemStack) itemStack;
        } else {
            if (MOJANG_MOTHER_DEAD) {
                itemStack = ((RefBukkitItemStack) (Object) itemStack).craftDelegate;
                this.itemStack = itemStack;
                this.craftItemStack = (RefCraftItemStack) itemStack;
            } else {
                this.itemStack = itemStack;
                this.craftItemStack = null;
            }
        }
    }

    @Nullable
    public NbtComponentLike getDirectTag() {
        if (MOJANG_MOTHER_DEAD) {
            return getTag();
        } else {
            if (craftItemStack == null) {
                return new NbtBukkitItemComponent(itemStack);
            } else {
                return new NbtCraftItemComponent(craftItemStack);
            }
        }
    }

    @Nullable
    public NbtCompound getTag() {
        if (craftItemStack == null) {
            RefCraftMetaItem meta = (RefCraftMetaItem) (Object) InvokeUtil.getItemMeta(itemStack);
            if (meta == null) {
                return null;
            } else {
                if (MOJANG_MOTHER_DEAD) {
                    return new NbtCompound(InvokeUtil.getCustomTag(meta));
                } else {
                    NbtCompound compound = new NbtCompound();
                    meta.applyToItem(compound.delegate);
                    Map<String, RefNbtBase> tags = compound.delegate.tags;
                    for (Map.Entry<String, RefNbtBase> entry : tags.entrySet()) {
                        String key = entry.getKey();
                        RefNbtBase value = entry.getValue();
                        // applyToItem 直接把 unhandledTags 丢进去了, 这样是不行的, 必须 clone 一下
                        if (!RefCraftMetaItem.HANDLED_TAGS.contains(key)
                                && (value instanceof RefNbtTagCompound || value instanceof RefNbtTagList)) {
                            tags.put(key, value.rClone());
                        }
                    }
                    return compound.isEmpty() ? null : compound;
                }
            }
        } else {
            if (MOJANG_MOTHER_DEAD) {
                RefCustomData customData = ((RefDataComponentHolder) (Object) craftItemStack.handle).get(RefDataComponents.CUSTOM_DATA);
                return customData == null ? null : new NbtCompound(customData.getUnsafe());
            } else {
                RefNbtTagCompound tag = craftItemStack.handle.getTag();
                return tag == null ? null : new NbtCompound(tag);
            }
        }
    }

    public void setTag(@NotNull NbtCompound compound) {
        if (craftItemStack == null) {
            InvokeUtil.setItemMeta(itemStack, null);
            RefCraftItemStack craftItemStack = RefCraftItemStack.asCraftCopy(itemStack);
            if (MOJANG_MOTHER_DEAD) {
                craftItemStack.handle.set(RefDataComponents.CUSTOM_DATA, RefCustomData.of(compound.delegate));
            } else {
                craftItemStack.handle.setTag(compound.delegate);
            }
            InvokeUtil.setItemMeta(itemStack, craftItemStack.getItemMeta());
        } else {
            if (MOJANG_MOTHER_DEAD) {
                craftItemStack.handle.set(RefDataComponents.CUSTOM_DATA, RefCustomData.of(compound.delegate));
            } else {
                craftItemStack.handle.setTag(compound.delegate);
            }
        }
    }

    public @NotNull NbtCompound getOrCreateTag() {
        if (craftItemStack == null) {
            RefCraftMetaItem meta = (RefCraftMetaItem) (Object) InvokeUtil.getItemMeta(itemStack);
            if (meta == null) {
                return new NbtCompound();
            } else {
                if (MOJANG_MOTHER_DEAD) {
                    ItemMeta itemMeta = NbtUtils.getItemMeta(itemStack);
                    RefNbtTagCompound customTag = InvokeUtil.getCustomTag(meta);
                    if (customTag == null) {
                        customTag = new RefNbtTagCompound();
                        InvokeUtil.setCustomTag(itemMeta, customTag);
                    }
                    return new NbtCompound(customTag);
                } else {
                    NbtCompound compound = new NbtCompound();
                    meta.applyToItem(compound.delegate);
                    Map<String, RefNbtBase> tags = compound.delegate.tags;
                    for (Map.Entry<String, RefNbtBase> entry : tags.entrySet()) {
                        String key = entry.getKey();
                        RefNbtBase value = entry.getValue();
                        // applyToItem 直接把 unhandledTags 丢进去了, 这样是不行的, 必须 clone 一下
                        if (!RefCraftMetaItem.HANDLED_TAGS.contains(key)
                                && (value instanceof RefNbtTagCompound || value instanceof RefNbtTagList)) {
                            tags.put(key, value.rClone());
                        }
                    }
                    return compound;
                }
            }
        } else {
            if (MOJANG_MOTHER_DEAD) {
                RefCustomData customData = ((RefDataComponentHolder) (Object) craftItemStack.handle).get(RefDataComponents.CUSTOM_DATA);
                if (customData == null) {
                    customData = RefCustomData.of(new RefNbtTagCompound());
                    craftItemStack.handle.set(RefDataComponents.CUSTOM_DATA, customData);
                }
                return new NbtCompound(customData.getUnsafe());
            } else {
                return new NbtCompound(NbtUtils.getOrCreateTag(craftItemStack.handle));
            }
        }
    }

    public ItemStack asBukkitCopy() {
        if (craftItemStack == null) {
            return itemStack.clone();
        } else {
            return RefCraftItemStack.asBukkitCopy(craftItemStack.handle);
        }
    }

    public ItemStack asCraftCopy() {
        if (craftItemStack == null) {
            return RefCraftItemStack.asCraftCopy(itemStack);
        } else {
            return itemStack.clone();
        }
    }

    public ItemStack asCopy() {
        if (craftItemStack == null) {
            return NbtUtils.bukkitCopy(itemStack);
        } else {
            return itemStack.clone();
        }
    }

    public boolean isBukkitItemStack() {
        return craftItemStack == null;
    }

    public boolean isCraftItemStack() {
        return craftItemStack != null;
    }

    public ItemStack asItemStack() {
        return itemStack;
    }

    @Override
    public NbtItemStack clone() throws CloneNotSupportedException {
        return new NbtItemStack(NbtUtils.asCopy(itemStack));
    }
}
