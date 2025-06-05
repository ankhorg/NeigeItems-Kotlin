package pers.neige.neigeitems.manager.logger;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface ILogger {
    static @Nullable ILogger of(Object logger) {
        if (logger instanceof java.util.logging.Logger) {
            return new JavaLogger((java.util.logging.Logger) logger);
        } else if (logger instanceof org.slf4j.Logger) {
            return new Slf4jLogger((org.slf4j.Logger) logger);
        }
        return null;
    }

    void warn(@NonNull String message, @NonNull Throwable thrown);
}
