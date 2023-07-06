package net.landania.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class HomePlayer {

    protected final UUID uuid;
    private final ArrayList<Home> homes;

    public HomePlayer(@NotNull UUID uuid, @NotNull List<Home> homes) {
        this.uuid = uuid;
        this.homes = new ArrayList<>(homes);
    }

    public void save() {
        HomeAPI.getPlatform().getDatabase().save(this);
    }

    public void deleteAllHomes() {
        this.homes.clear();
        save();
        String msg = HomeAPI.getPlatform().getMessageRaw("all-homes-deleted");
        sendMessage(MiniMessage.miniMessage().deserialize(msg));
    }

    public boolean createHome(@NotNull String name, @NotNull String worldName, double x, double y, double z, float yaw, float pitch) {
        if (this.homes.size() >= 40) {
            String msg = HomeAPI.getPlatform().getMessageRaw("too-many-homes");
            sendMessage(MiniMessage.miniMessage().deserialize(msg));
            return false;
        }
        if (hasHome(name)) {
            String msg = HomeAPI.getPlatform().getMessageRaw("home-already-exists").replace("%home%", name);
            sendMessage(MiniMessage.miniMessage().deserialize(msg));
            return false;
        }
        String msg = HomeAPI.getPlatform().getMessageRaw("home-created").replace("%home%", name);
        sendMessage(MiniMessage.miniMessage().deserialize(msg));
        this.homes.add(new Home(name, worldName, x, y, z, yaw, pitch));
        save();
        return true;
    }

    public abstract void openHomesGui();

    public abstract void setCurrentPositionHome(String name);

    public boolean removeHome(@NotNull String name) {
        Home home = getHome(name);
        if (home == null) {
            String msg = HomeAPI.getPlatform().getMessageRaw("home-not-found").replace("%home%", name);
            sendMessage(MiniMessage.miniMessage().deserialize(msg));
            return false;
        }
        this.homes.remove(home);
        String msg = HomeAPI.getPlatform().getMessageRaw("home-deleted").replace("%home%", name);
        sendMessage(MiniMessage.miniMessage().deserialize(msg));
        return true;
    }

    public boolean hasHome(@NotNull String homeName) {
        return getHome(homeName) != null;
    }

    public boolean hasHome(@NotNull Home home) {
        return homes.contains(home);
    }

    public abstract void teleport(@NotNull Home home);

    public abstract void sendMessage(@NotNull Component message);

    public abstract boolean hasPermission(@NotNull String permission);

    public boolean teleport(@NotNull String homeName) {
        Home home = getHome(homeName);
        if (home == null) {
            String msg = HomeAPI.getPlatform().getMessageRaw("home-not-found").replace("%home%", homeName);
            sendMessage(MiniMessage.miniMessage().deserialize(msg));
            return false;
        }
        teleport(home);
        return true;
    }

    public @Nullable Home getHome(@NotNull String name) {
        return homes.stream().filter(home -> home.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public @NotNull UUID getUUID() {
        return uuid;
    }

    /* Returns an unmodifiable list of homes. */
    public @NotNull List<Home> getHomes() {
        return Collections.unmodifiableList(homes);
    }

}
