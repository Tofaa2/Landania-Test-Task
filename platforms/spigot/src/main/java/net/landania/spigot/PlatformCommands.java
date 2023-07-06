package net.landania.spigot;

import net.landania.api.HomeAPI;
import net.landania.api.HomePlayer;
import net.landania.commons.commands.AbstractCommand;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class PlatformCommands {


    static void init(@NotNull CommandMap commandMap) {
        for (AbstractCommand command : AbstractCommand.commands) {
            commandMap.register(command.getName(), new WrappedCommand(command));
        }
    }

    private static class WrappedCommand extends BukkitCommand {

        private final AbstractCommand command;

        WrappedCommand(@NotNull AbstractCommand command) {
            super(command.getName());
            this.setAliases(command.getAliases());
            this.command = command;
        }


        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
            if (!(sender instanceof Player p)) {
                return false;
            }
            HomePlayer player = HomeAPI.getPlatform().getPlayer(p.getUniqueId());
            command.execute(player, args);
            return true;
        }
    }

}
