package net.landania.commons.commands.impl;

import net.landania.api.HomePlayer;
import net.landania.commons.commands.AbstractCommand;
import org.jetbrains.annotations.NotNull;

public final class HomesCommand extends AbstractCommand {

    public HomesCommand() {
        super("homes");
    }

    @Override
    public void handle(@NotNull HomePlayer player, @NotNull String[] args) {
        player.openHomesGui();
    }
}
