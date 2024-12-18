package pers.neige.neigeitems.manager.logger;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaLogger implements ILogger {
    @NotNull
    protected final Logger logger;

    public JavaLogger(@NotNull Logger logger) {
        this.logger = logger;
    }

    public void warn(String message, Throwable thrown) {
        logger.log(Level.WARNING, thrown, () -> message);
    }
}
