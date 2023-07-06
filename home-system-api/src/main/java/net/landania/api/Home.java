package net.landania.api;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * I would have much preferred using a normal database such as MongoDB for this, however i cannot ;-;
 */
public final class Home {

    private final double x, y, z;
    private final float yaw, pitch;
    private final String name, world;

    /* No need to give the users access to this constructor */
    Home(@NotNull String name, @NotNull String world, double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.name = name;
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getName() {
        return name;
    }

    public String getWorld() {
        return world;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Home home)) {
            return false;
        }
        // Long if statement to check if all the values are the same
        return home.name.equals(this.name) && home.world.equals(this.world) && home.x == this.x && home.y == this.y && home.z == this.z && home.yaw == this.yaw && home.pitch == this.pitch;
    }
}
