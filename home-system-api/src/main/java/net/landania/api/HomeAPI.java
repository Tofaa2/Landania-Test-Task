package net.landania.api;

import org.jetbrains.annotations.NotNull;

public final class HomeAPI {

    private HomeAPI() {}

    private static Platform platform;

    public static void init(@NotNull Platform platform) {
        HomeAPI.platform = platform;
    }

    public static @NotNull Platform getPlatform() {
        return platform;
    }

}
