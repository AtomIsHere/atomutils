package com.github.atomishere.atomutils.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation containing the parameters of a command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandParameters {
    String commandName();

    /**
     * Description of the command.
     *
     * @return command description.
     */
    String description();

    /**
     * Usage of the command.
     *
     * @return command usage.
     */
    String usage();

    /**
     * Aliases of the command, separated by ','.
     *
     * @return command aliases.
     */
    String aliases() default "";
}
