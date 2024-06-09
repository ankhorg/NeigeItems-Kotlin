package pers.neige.neigeitems.annotation;

import org.bukkit.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Awake {
    LifeCycle lifeCycle() default LifeCycle.ENABLE;

    EventPriority priority() default EventPriority.NORMAL;

    enum LifeCycle {
        ENABLE,
        ACTIVE,
        DISABLE
    }
}
