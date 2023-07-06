package net.landania.api;

import net.landania.api.database.HomeDatabase;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface Platform {

    @NotNull HomePlayer getPlayer(@NotNull UUID uuid);

    @NotNull HomePlayer createPlayerUnsafe(@NotNull UUID uuid, @NotNull List<Home> homes);

    @NotNull HomeDatabase getDatabase();

    void runAsync(@NotNull Runnable runnable);

    @NotNull String platformName();

    @NotNull String platformVersion();

    @NotNull String getMessageRaw(@NotNull String key);

    @NotNull Path getDataDirectory();

}
