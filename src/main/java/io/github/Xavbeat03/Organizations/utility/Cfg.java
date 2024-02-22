package io.github.Xavbeat03.Organizations.utility;

import io.github.Xavbeat03.Organizations.Organizations;
import io.github.Xavbeat03.Organizations.config.ConfigHandler;
import com.github.milkdrinkers.Crate.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Convenience class for accessing {@link ConfigHandler#getConfig}
 */
public abstract class Cfg {
    /**
     * Convenience method for {@link ConfigHandler#getConfig} to getConnection {@link Config}
     *
     * @return the config
     */
    @NotNull
    public static Config get() {
        return Organizations.getInstance().getConfigHandler().getConfig();
    }
}
