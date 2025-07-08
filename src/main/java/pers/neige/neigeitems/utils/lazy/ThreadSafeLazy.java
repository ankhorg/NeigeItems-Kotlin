package pers.neige.neigeitems.utils.lazy;

import lombok.NonNull;

import java.util.function.Supplier;

public class ThreadSafeLazy<T> {
    private volatile T value;
    private volatile boolean initialized;
    private Supplier<T> supplier;

    public ThreadSafeLazy(@NonNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public ThreadSafeLazy(@NonNull T value) {
        this.value = value;
        this.initialized = true;
        this.supplier = null;
    }

    public T get() {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    this.value = supplier.get();
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
