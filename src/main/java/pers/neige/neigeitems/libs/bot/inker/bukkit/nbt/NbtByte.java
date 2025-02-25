package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtByteLike;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefNbtTagByte;

public final class NbtByte extends NbtNumeric<RefNbtTagByte> implements NbtByteLike {
    private static final boolean OF_SUPPORTED = CbVersion.v1_15_R1.isSupport();
    private static final NbtByte[] instanceCache = buildInstanceCache();
    private static final NbtByte falseInstance = valueOf((byte) 0);
    private static final NbtByte trueInstance = valueOf((byte) 1);

    private NbtByte(RefNbtTagByte delegate) {
        super(delegate);
    }

    private static NbtByte[] buildInstanceCache() {
        NbtByte[] result = new NbtByte[256];
        for (int i = 0; i < result.length; i++) {
            result[i] = OF_SUPPORTED
                    ? new NbtByte(RefNbtTagByte.of((byte) (i - 128)))
                    : new NbtByte(new RefNbtTagByte((byte) (i - 128)));
        }
        return result;
    }

    public static NbtByte valueOf(byte value) {
        return instanceCache[value + 128];
    }

    static NbtByte fromNmsImpl(RefNbtTagByte delegate) {
        return instanceCache[delegate.asByte() + 128];
    }

    public static NbtByte valueOf(boolean value) {
        return value ? trueInstance : falseInstance;
    }

    public boolean getAsBoolean() {
        return getAsByte() != 0;
    }

    @Override
    public String getAsString() {
        return String.valueOf(getAsByte());
    }

    @Override
    public NbtByte clone() {
        return this;
    }

    @Override
    public int compareTo(@NotNull Nbt<?> o) {
        if (o instanceof NbtByte) {
            return Byte.compare(getAsByte(), ((NbtByte) o).getAsByte());
        }
        return super.compareTo(o);
    }
}
