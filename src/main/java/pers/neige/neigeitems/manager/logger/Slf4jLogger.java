package pers.neige.neigeitems.manager.logger;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class Slf4jLogger implements ILogger {
    @NotNull
    protected final Logger logger;

    public Slf4jLogger(@NotNull Logger logger) {
        this.logger = logger;
    }

    public void warn(String message, Throwable thrown) {
        logger.warn(message, thrown);
    }
}
