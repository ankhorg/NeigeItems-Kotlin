package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.jetbrains.annotations.NotNull;
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
        return delegate.asString().isEmpty() ? EMPTY : new NbtString(delegate);
    }

    public String getAsString() {
        return delegate.asString();
    }

    @Override
    public NbtString clone() {
        return this;
    }

    @Override
    public int compareTo(@NotNull Nbt<?> o) {
        if (o instanceof NbtString) {
            return getAsString().compareTo(o.getAsString());
        }
        return super.compareTo(o);
    }
}
