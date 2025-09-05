package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import lombok.NonNull;
import lombok.val;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtStringLike;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagString;

public final class NbtString extends Nbt<RefNbtTagString> implements NbtStringLike {
    private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
    private static final NbtString EMPTY = new NbtString(OF_SUPPORTED
            ? RefNbtTagString.of("")
            : new RefNbtTagString(""));

    NbtString(RefNbtTagString delegate) {
        super(delegate);
    }

    public static NbtString valueOf(String value) {
        return value.isEmpty()
                ? EMPTY
                : new NbtString(OF_SUPPORTED
                ? RefNbtTagString.of(value)
                : new RefNbtTagString(value));
    }

    static NbtString fromNmsImpl(RefNbtTagString delegate) {
        val string = NBT_FORMAT_CHANGE ? delegate.asString1().orElse("") : delegate.asString0();
        return string.isEmpty() ? EMPTY : new NbtString(delegate);
    }

    @Override
    public NbtString clone() {
        return this;
    }

    @Override
    public int compareTo(@NonNull Nbt<?> o) {
        if (o instanceof NbtString) {
            return getAsString().compareTo(o.getAsString());
        }
        return super.compareTo(o);
    }
}
