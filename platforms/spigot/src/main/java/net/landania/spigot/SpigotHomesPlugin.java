package net.landania.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public class SpigotHomesPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        new SpigotPlatform(this);
    }
}
