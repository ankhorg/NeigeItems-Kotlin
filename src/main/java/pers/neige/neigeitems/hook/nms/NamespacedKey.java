package pers.neige.neigeitems.hook.nms;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
public final class NamespacedKey {
    private final String namespace;
    private final String key;

    public NamespacedKey(@NonNull String namespace, @NonNull String key) {
        this.namespace = namespace;
        this.key = key;
    }

    public @NonNull String getNamespace() {
        return namespace;
    }

    public @NonNull String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return this.namespace + ":" + this.key;
    }
}
