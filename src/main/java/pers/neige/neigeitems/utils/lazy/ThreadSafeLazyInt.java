package pers.neige.neigeitems.utils.lazy;

import lombok.NonNull;

import java.util.function.IntSupplier;

public class ThreadSafeLazyInt implements IntSupplier {
    private volatile int value;
    private volatile boolean initialized;
    private IntSupplier supplier;

    public ThreadSafeLazyInt(@NonNull IntSupplier supplier) {
        this.supplier = supplier;
    }

    public ThreadSafeLazyInt(int value) {
        this.value = value;
        this.initialized = true;
        this.supplier = null;
    }

    public int get() {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    this.value = supplier.getAsInt();
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
    public int getAsInt() {
        return get();
    }
}
