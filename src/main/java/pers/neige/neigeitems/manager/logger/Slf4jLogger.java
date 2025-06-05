package pers.neige.neigeitems.manager.logger;

import lombok.NonNull;
import org.slf4j.Logger;

public class Slf4jLogger implements ILogger {
    protected final @NonNull Logger logger;

    public Slf4jLogger(@NonNull Logger logger) {
        this.logger = logger;
    }

    public void warn(@NonNull String message, @NonNull Throwable thrown) {
        logger.warn(message, thrown);
    }
}
