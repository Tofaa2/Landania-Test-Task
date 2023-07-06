package net.landania.commons.commands.impl;

import net.landania.api.HomePlayer;
import net.landania.commons.commands.AbstractCommand;
import org.jetbrains.annotations.NotNull;


public class SetHomeCommand extends AbstractCommand {
    public SetHomeCommand() {
        super("sethome");
    }

    @Override
    public void handle(@NotNull HomePlayer player, @NotNull String[] args) {
        if (args.length != 1) {
            sendUsage(player);
        }
        player.setCurrentPositionHome(args[0]);
    }
}
