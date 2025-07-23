package pers.neige.neigeitems.utils.lazy;

import lombok.NonNull;

import java.util.function.DoubleSupplier;

public class ThreadSafeLazyDouble implements DoubleSupplier {
    private volatile double value;
    private volatile boolean initialized;
    private DoubleSupplier supplier;

    public ThreadSafeLazyDouble(@NonNull DoubleSupplier supplier) {
        this.supplier = supplier;
    }

    public ThreadSafeLazyDouble(double value) {
        this.value = value;
        this.initialized = true;
        this.supplier = null;
    }

    public double get() {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    this.value = supplier.getAsDouble();
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
    public double getAsDouble() {
        return get();
    }
}
