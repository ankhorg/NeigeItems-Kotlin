package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtComponentLike;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.invoke.InvokeUtil;
import pers.neige.neigeitems.ref.nbt.*;

import java.util.Map;

public final class NbtItemStack {
    private final ItemStack itemStack;
    private final RefCraftItemStack craftItemStack;

    public NbtItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        if (itemStack instanceof RefCraftItemStack) {
            this.craftItemStack = (RefCraftItemStack) itemStack;
        } else {
            this.craftItemStack = null;
        }
    }

    public NbtComponentLike getDirectTag() {
        if (craftItemStack == null) {
            return new NbtBukkitItemComponent(itemStack);
        } else {
            return new NbtCraftItemComponent(craftItemStack);
        }
    }

    @Nullable
    public NbtCompound getTag() {
        if (craftItemStack == null) {
            RefCraftMetaItem meta = (RefCraftMetaItem) (Object) InvokeUtil.getItemMeta(itemStack);
            if (meta == null) {
                return null;
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
        } else {
            RefNbtTagCompound tag = craftItemStack.handle.getTag();
            return tag == null ? null : new NbtCompound(tag);
        }
    }

    public void setTag(@NotNull NbtCompound compound) {
        if (craftItemStack == null) {
            InvokeUtil.setItemMeta(itemStack, null);
            RefCraftItemStack craftItemStack = RefCraftItemStack.asCraftCopy(itemStack);
            craftItemStack.handle.setTag(compound.delegate);
            InvokeUtil.setItemMeta(itemStack, craftItemStack.getItemMeta());
        } else {
            craftItemStack.handle.setTag(compound.delegate);
        }
    }

    public @NotNull NbtCompound getOrCreateTag() {
        if (craftItemStack == null) {
            RefCraftMetaItem meta = (RefCraftMetaItem) (Object) InvokeUtil.getItemMeta(itemStack);
            if (meta == null) {
                return new NbtCompound();
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
        } else {
            return new NbtCompound(NbtUtils.getOrCreateTag(craftItemStack.handle));
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
