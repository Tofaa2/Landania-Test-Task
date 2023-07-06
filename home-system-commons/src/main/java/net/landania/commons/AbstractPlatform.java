package net.landania.commons;

import net.landania.api.HomeAPI;
import net.landania.api.Platform;
import net.landania.api.database.HomeDatabase;
import net.landania.api.database.HomeDatabaseCredentials;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;

/**
 * A simple wrapper for stuff that should not be available on the api module. Such as the configuration system.
 * This should be used by all implementing platforms to provide a common interface for the api module.
 */
public abstract class AbstractPlatform implements Platform {

    private final HomeDatabase database;
    private final JsonConfig<Messages> messagesConfig;
    private final Path dataDirectory;

    public AbstractPlatform(Path dataDirectory) {
        HomeAPI.init(this);
        this.dataDirectory = dataDirectory;
        messagesConfig = new JsonConfig<>(dataDirectory.resolve("messages.json"), Messages.class);
        messagesConfig.load(Messages.DEFAULT);
        JsonConfig<HomeDatabaseCredentials> credentialsConfig = new JsonConfig<>(dataDirectory.resolve("database.json"), HomeDatabaseCredentials.class);
        credentialsConfig.load(new HomeDatabaseCredentials("url", "homes"));
        try {
            this.database = new HomeDatabase(this, credentialsConfig.access());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull @Override
    public HomeDatabase getDatabase() {
        return database;
    }

    @Override
    public @NotNull Path getDataDirectory() {
        return dataDirectory;
    }

    @Override
    public @NotNull String getMessageRaw(@NotNull String key) {
        Map<String, String> messages = messagesConfig.access().messages();
        if (!messages.containsKey(key)) {
            throw new IllegalArgumentException("No message with key " + key + " found.");
        }
        return messages.get("prefix") + messages.get(key);
    }
}
