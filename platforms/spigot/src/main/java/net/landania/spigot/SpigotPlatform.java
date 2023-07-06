package net.landania.spigot;

import net.landania.api.Home;
import net.landania.api.HomeAPI;
import net.landania.api.HomePlayer;
import net.landania.commons.AbstractPlatform;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

final class SpigotPlatform extends AbstractPlatform {

    /* Cache pepe to avoid slow a mojang api calls */
    public static final ItemStack DELETE_ALL_ITEM = new ItemStack(Material.PLAYER_HEAD);
    static {
        SkullMeta meta = (SkullMeta) DELETE_ALL_ITEM.getItemMeta();
        meta.setPlayerProfile(Bukkit.createProfile("Shoot"));
    }

    private final JavaPlugin plugin;
    private final HashMap<UUID, HomePlayer> players = new HashMap<>();

    SpigotPlatform(@NotNull JavaPlugin plugin) {
        super(plugin.getDataFolder().toPath());
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onJoin(@NotNull PlayerJoinEvent event) {
                getDatabase().load(event.getPlayer().getUniqueId());
            }

            @EventHandler
            public void onQuit(@NotNull PlayerQuitEvent event) {
                getDatabase().save(getPlayer(event.getPlayer().getUniqueId()));
                players.remove(event.getPlayer().getUniqueId());
            }

            @EventHandler
            public void onInventoryClick(@NotNull InventoryClickEvent event) {
                if (!(event.getView().getTopInventory().getHolder() instanceof HomeGui gui)) {
                    return;
                }
                event.setCancelled(true);
                int slot = event.getSlot();
                Home home = gui.homeMap.get(slot);
                if (home != null) {
                    gui.player.teleport(home);
                }
                if (slot == 49) {
                    gui.player.deleteAllHomes();
                }
            }

        }, plugin);
        PlatformCommands.init(Bukkit.getCommandMap());
        plugin.getLogger().info("Loaded in platform " + platformName() + ":" + platformVersion());
    }

    @Override
    public @NotNull HomePlayer getPlayer(@NotNull UUID uuid) {

        return players.get(uuid);
    }

    @Override
    public @NotNull HomePlayer createPlayerUnsafe(@NotNull UUID uuid, @NotNull List<Home> homes) {
        SpigotPlayer player = new SpigotPlayer(uuid, homes);
        players.put(uuid, player);
        return player;
    }

    @Override
    public void runAsync(@NotNull Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public @NotNull String platformName() {
        return Bukkit.getName();
    }

    @Override
    public @NotNull String platformVersion() {
        return Bukkit.getVersion();
    }
}
