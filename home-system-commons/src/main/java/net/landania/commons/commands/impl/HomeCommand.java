package net.landania.commons.commands.impl;

import net.landania.api.HomePlayer;
import net.landania.commons.commands.AbstractCommand;
import org.jetbrains.annotations.NotNull;

public final class HomeCommand extends AbstractCommand {

    public HomeCommand() {
        super("home");
    }

    @Override
    public void handle(@NotNull HomePlayer player, @NotNull String[] args) {
        if (args.length != 1) {
            sendUsage(player);
            return;
        }
        player.teleport(args[0]);
    }
}
