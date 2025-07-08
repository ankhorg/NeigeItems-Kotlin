package pers.neige.neigeitems.utils.lazy;

import lombok.NonNull;

import java.util.function.LongSupplier;

public class ThreadSafeLazyLong {
    private volatile long value;
    private volatile boolean initialized;
    private LongSupplier supplier;

    public ThreadSafeLazyLong(@NonNull LongSupplier supplier) {
        this.supplier = supplier;
    }

    public ThreadSafeLazyLong(long value) {
        this.value = value;
        this.initialized = true;
        this.supplier = null;
    }

    public long get() {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    this.value = supplier.getAsLong();
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
}
