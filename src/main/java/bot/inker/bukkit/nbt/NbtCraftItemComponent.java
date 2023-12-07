package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.api.NbtComponentLike;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;

public final class NbtCraftItemComponent extends NbtCompound implements NbtComponentLike {
    NbtCraftItemComponent(RefCraftItemStack itemStack) {
        super(NbtUtils.getOrCreateTag(itemStack.handle));
    }
}
