package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtComponentLike;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;

@Deprecated
public final class NbtCraftItemComponent extends NbtCompound implements NbtComponentLike {
    NbtCraftItemComponent(RefCraftItemStack itemStack) {
        super(NbtUtils.getOrCreateTag(itemStack.handle));
    }
}
