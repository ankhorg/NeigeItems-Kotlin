package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.core.component.RefDataComponentType;
import pers.neige.neigeitems.ref.core.component.RefPatchedDataComponentMap;
import pers.neige.neigeitems.ref.item.RefItem;

import javax.annotation.Nullable;

@HandleBy(reference = "net/minecraft/world/item/ItemStack", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/ItemStack", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefNmsItemStack {
    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;components:Lnet/minecraft/core/component/PatchedDataComponentMap;", useAccessor = true, predicates = "craftbukkit_version:[v1_20_R4,)")
    public RefPatchedDataComponentMap components;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemStack;<init>(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefNmsItemStack(RefNbtTagCompound nbt) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemStack;<init>(Lnet/minecraft/server/v1_12_R1/Item;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefNmsItemStack(RefItem item) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/ItemStack;<init>(Lnet/minecraft/server/v1_13_R1/IMaterial;)V", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    public RefNmsItemStack(RefIMaterial imaterial) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;of(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/item/ItemStack;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/ItemStack;a(Lnet/minecraft/server/v1_13_R1/NBTTagCompound;)Lnet/minecraft/server/v1_13_R1/ItemStack;", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    public native static RefNmsItemStack of(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;hasTag()Z", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemStack;hasTag()Z", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native boolean hasTag();

    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;getTag()Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemStack;getTag()Lnet/minecraft/server/v1_12_R1/NBTTagCompound;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefNbtTagCompound getTag();

    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;setTag(Lnet/minecraft/nbt/CompoundTag;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemStack;setTag(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native void setTag(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemStack;getItem()Lnet/minecraft/server/v1_12_R1/Item;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefItem getItem();

    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;save(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemStack;save(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)Lnet/minecraft/server/v1_12_R1/NBTTagCompound;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefNbtTagCompound save(RefNbtTagCompound nbt);

    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;load(Lnet/minecraft/nbt/CompoundTag;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/ItemStack;load(Lnet/minecraft/server/v1_13_R1/NBTTagCompound;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemStack;load(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native void load(RefNbtTagCompound nbt);

    @Nullable
    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native <T> T set(RefDataComponentType<? super T> type, @Nullable T value);

    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;getDisplayName()Lnet/minecraft/network/chat/Component;", predicates = "craftbukkit_version:[v1_20_R4,)")
    public native RefComponent getDisplayName();

//    @HandleBy(reference = "Lnet/minecraft/world/item/ItemStack;applyComponents(Lnet/minecraft/core/component/DataComponentPatch;)V", predicates = "craftbukkit_version:[v1_20_R4,)")
//    public native void applyComponents(DataComponentPatch changes);
}
