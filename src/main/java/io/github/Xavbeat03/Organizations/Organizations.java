package io.github.Xavbeat03.Organizations;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Xavbeat03.Organizations.command.CommandHandler;
import io.github.Xavbeat03.Organizations.config.ConfigHandler;
import io.github.Xavbeat03.Organizations.db.DatabaseHandler;
import io.github.Xavbeat03.Organizations.hooks.VaultHook;
import io.github.Xavbeat03.Organizations.listener.ListenerHandler;
import io.github.Xavbeat03.Organizations.utility.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Main class.
 */
public class Organizations extends JavaPlugin {
    private static Organizations instance;
    private ConfigHandler configHandler;
    private DatabaseHandler databaseHandler;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private static VaultHook vaultHook;

    /**
     * Gets plugin instance.
     *
     * @return the plugin instance
     */
    public static Organizations getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        configHandler = new ConfigHandler(instance);
        databaseHandler = new DatabaseHandler(instance);
        commandHandler = new CommandHandler(instance);
        listenerHandler = new ListenerHandler(instance);
        vaultHook = new VaultHook(instance);

        configHandler.onLoad();
        databaseHandler.onLoad();
        commandHandler.onLoad();
        listenerHandler.onLoad();
        vaultHook.onLoad();
    }

    @Override
    public void onEnable() {
        configHandler.onEnable();
        databaseHandler.onEnable();
        commandHandler.onEnable();
        listenerHandler.onEnable();
        vaultHook.onEnable();

        if (vaultHook.isVaultLoaded()) {
            Logger.get().info(ColorParser.of("<green>Vault has been found on this server. Vault support enabled.").build());
        } else {
            Logger.get().warn(ColorParser.of("<yellow>Vault is not installed on this server. Vault support has been disabled.").build());
        }
    }

    @Override
    public void onDisable() {
        configHandler.onDisable();
        databaseHandler.onDisable();
        commandHandler.onDisable();
        listenerHandler.onDisable();
        vaultHook.onDisable();
    }

    /**
     * Gets data handler.
     *
     * @return the data handler
     */
    @NotNull
    public DatabaseHandler getDataHandler() {
        return databaseHandler;
    }

    /**
     * Gets config handler.
     *
     * @return the config handler
     */
    @NotNull
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    /**
     * Gets vault hook.
     *
     * @return the vault hook
     */
    @NotNull
    public static VaultHook getVaultHook() {
        return vaultHook;
    }
}
