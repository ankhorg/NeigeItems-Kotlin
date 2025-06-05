package pers.neige.neigeitems.manager.logger;

import lombok.NonNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaLogger implements ILogger {
    protected final @NonNull Logger logger;

    public JavaLogger(@NonNull Logger logger) {
        this.logger = logger;
    }

    public void warn(@NonNull String message, @NonNull Throwable thrown) {
        logger.log(Level.WARNING, thrown, () -> message);
    }
}
