package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtFloatLike;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagFloat;

public final class NbtFloat extends NbtNumeric<RefNbtTagFloat> implements NbtFloatLike {
    private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
    private static final NbtFloat ZERO = new NbtFloat(OF_SUPPORTED
            ? RefNbtTagFloat.of(0.0F)
            : new RefNbtTagFloat(0.0F)
    );

    NbtFloat(RefNbtTagFloat delegate) {
        super(delegate);
    }

    public static NbtFloat valueOf(float value) {
        return value == 0.0F ? ZERO : new NbtFloat(OF_SUPPORTED
                ? RefNbtTagFloat.of(value)
                : new RefNbtTagFloat(value));
    }

    static NbtFloat fromNmsImpl(RefNbtTagFloat delegate) {
        return delegate.asFloat() == 0.0F ? ZERO : new NbtFloat(delegate);
    }

    @Override
    public String getAsString() {
        return String.valueOf(getAsFloat());
    }

    @Override
    public NbtFloat clone() {
        return this;
    }

    @Override
    public int compareTo(@NotNull Nbt<?> o) {
        if (o instanceof NbtFloat) {
            return Float.compare(getAsFloat(), ((NbtFloat) o).getAsFloat());
        }
        return super.compareTo(o);
    }
}
