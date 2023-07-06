package net.landania.spigot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.landania.api.Home;
import net.landania.api.HomeAPI;
import net.landania.api.HomePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class SpigotPlayer extends HomePlayer {


    public SpigotPlayer(@NotNull UUID uuid, @NotNull List<Home> homes) {
        super(uuid, homes);
    }

    @Override
    public void openHomesGui() {
        bukkitPlayer().openInventory(new HomeGui(this).getInventory());
    }

    @Override
    public void setCurrentPositionHome(String name) {
        Player player = bukkitPlayer();
        Location location = player.getLocation();
        createHome(name, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void teleport(@NotNull Home home) {
        World world = Bukkit.getWorld(home.getWorld());
        if (world == null) {
            throw new IllegalStateException("World " + home.getWorld() + " does not exist");
        }
        Location location = new Location(world, home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch());
        bukkitPlayer().teleport(location);
        String msg = HomeAPI.getPlatform().getMessageRaw("home-teleported").replace("%home%", home.getName());
        sendMessage(MiniMessage.miniMessage().deserialize(msg));
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        bukkitPlayer().sendMessage(message);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return bukkitPlayer().hasPermission(permission);
    }

    private Player bukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
