package com.github.atomishere.atomutils.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation containing the permissions of a command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandPermissions {
    /**
     * Permission of the command.
     *
     * @return command permission.
     */
    String permission();

    /**
     * Source of the command.
     *
     * @return the required command source.
     */
    CommandSource source();
}
