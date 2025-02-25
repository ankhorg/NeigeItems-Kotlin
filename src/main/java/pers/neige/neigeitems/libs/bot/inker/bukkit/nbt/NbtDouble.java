package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtDoubleLike;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagDouble;

public final class NbtDouble extends NbtNumeric<RefNbtTagDouble> implements NbtDoubleLike {
    private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
    private static final NbtDouble ZERO = new NbtDouble(OF_SUPPORTED
            ? RefNbtTagDouble.of(0.0)
            : new RefNbtTagDouble(0.0));

    NbtDouble(RefNbtTagDouble delegate) {
        super(delegate);
    }

    public static NbtDouble valueOf(double value) {
        return value == 0.0 ? ZERO : new NbtDouble(OF_SUPPORTED
                ? RefNbtTagDouble.of(value)
                : new RefNbtTagDouble(value));
    }

    static NbtDouble fromNmsImpl(RefNbtTagDouble delegate) {
        return delegate.asDouble() == 0.0 ? ZERO : new NbtDouble(delegate);
    }

    @Override
    public String getAsString() {
        return String.valueOf(getAsDouble());
    }

    @Override
    public NbtDouble clone() {
        return this;
    }

    @Override
    public int compareTo(@NotNull Nbt<?> o) {
        if (o instanceof NbtDouble) {
            return Double.compare(getAsDouble(), ((NbtDouble) o).getAsDouble());
        }
        return super.compareTo(o);
    }
}
