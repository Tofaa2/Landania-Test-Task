package net.landania.spigot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.landania.api.Home;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class HomeGui implements InventoryHolder {

    final SpigotPlayer player;
    final Inventory inventory;
    final HashMap<Integer, Home> homeMap = new HashMap<>();

    HomeGui(@NotNull SpigotPlayer player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 9 * 6, Component.text("Your Homes", NamedTextColor.GREEN));
        List<Home> homes = player.getHomes();
        int slot = 0;
        for (var home : homes) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta =  item.getItemMeta();
            meta.displayName(Component.text("Home: " + home.getName(), NamedTextColor.YELLOW));
            meta.lore(List.of(
                    Component.empty(),
                    Component.text("Click to teleport", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.empty()
                    )
            );
            item.setItemMeta(meta);
            inventory.addItem(item);
            homeMap.put(slot, home);
            slot++;
        }
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        filler.editMeta(meta -> {
            meta.displayName(Component.text(" ", NamedTextColor.GRAY));
            meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        });

        for (int i = 45; i < 54; i++) {
            if (i == 49) {
                inventory.setItem(i, SpigotPlatform.DELETE_ALL_ITEM);
                continue;
            }
            inventory.setItem(i, filler);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
