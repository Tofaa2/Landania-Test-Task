package net.landania.commons.commands;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ExtraCommandData(@NotNull List<CommandData> data) {
    public record CommandData(@NotNull String name, @NotNull String description, @NotNull String usage, @NotNull String permission, @NotNull String... aliases) {}

}
