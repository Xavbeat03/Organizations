package io.github.Xavbeat03.Organizations.utility;


import io.github.Xavbeat03.Organizations.Organizations;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

/**
 * A class that provides shorthand access to {@link Organizations#getComponentLogger}.
 */
public class Logger {
    /**
     * Get component logger. Shorthand for:
     *
     * @return the component logger {@link Organizations#getComponentLogger}.
     */
    @NotNull
    public static ComponentLogger get() {
        return Organizations.getInstance().getComponentLogger();
    }
}
