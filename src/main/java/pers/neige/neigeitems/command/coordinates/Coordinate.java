package pers.neige.neigeitems.command.coordinates;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class Coordinate {
    private final @NonNull LocationType type;
    private final double value;

    public Coordinate(
            @NonNull LocationType type,
            @Nullable Double value
    ) {
        this.type = type;
        this.value = value == null ? 0 : value;
    }

    public double get(double offset) {
        switch (type) {
            case ABSOLUTE:
                return value;
            case RELATIVE:
            case LOCAL:
                return value + offset;
            default:
                return 0;
        }
    }

    public @NonNull LocationType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }
}
