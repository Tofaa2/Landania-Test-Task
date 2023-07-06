package net.landania.commons.commands.impl;

import net.landania.api.HomePlayer;
import net.landania.commons.commands.AbstractCommand;
import org.jetbrains.annotations.NotNull;

public final class DelHomeCommand extends AbstractCommand {

    public DelHomeCommand() {
        super("delhome");
    }

    @Override
    public void handle(@NotNull HomePlayer player, @NotNull String[] args) {
        if (args.length != 1) {
            sendUsage(player);
            return;
        }
        player.removeHome(args[0]);
    }
}
