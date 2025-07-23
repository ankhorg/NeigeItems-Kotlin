package pers.neige.neigeitems.utils.lazy;

import lombok.NonNull;

import java.util.function.BooleanSupplier;

public class ThreadSafeLazyBoolean implements BooleanSupplier {
    private volatile boolean value;
    private volatile boolean initialized;
    private BooleanSupplier supplier;

    public ThreadSafeLazyBoolean(@NonNull BooleanSupplier supplier) {
        this.supplier = supplier;
    }

    public ThreadSafeLazyBoolean(boolean value) {
        this.value = value;
        this.initialized = true;
        this.supplier = null;
    }

    public boolean get() {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    this.value = supplier.getAsBoolean();
                    this.initialized = true;
                    this.supplier = null;
                }
            }
        }
        return value;
    }

    public boolean isInitialized() {
        synchronized (this) {
            return this.initialized;
        }
    }

    @Override
    public boolean getAsBoolean() {
        return get();
    }
}
