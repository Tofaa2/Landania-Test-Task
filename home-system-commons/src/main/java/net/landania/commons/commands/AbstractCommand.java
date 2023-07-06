package net.landania.commons.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.landania.api.HomeAPI;
import net.landania.api.HomePlayer;
import net.landania.commons.AbstractPlatform;
import net.landania.commons.JsonConfig;
import net.landania.commons.commands.impl.DelHomeCommand;
import net.landania.commons.commands.impl.HomeCommand;
import net.landania.commons.commands.impl.HomesCommand;
import net.landania.commons.commands.impl.SetHomeCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractCommand {

    protected static final MiniMessage mm = MiniMessage.miniMessage();
    public static final AbstractCommand[] commands;
    private static JsonConfig<ExtraCommandData> commandConfigs;
    static {
        List<ExtraCommandData.CommandData> defaults = List.of(
                new ExtraCommandData.CommandData("home", "Teleport to your specified home", "/home <home>", "home.teleport", "h"),
                new ExtraCommandData.CommandData("sethome", "Set your home", "/sethome <home>", "home.set", "sh"),
                new ExtraCommandData.CommandData("delhome", "Delete your home", "/delhome <home>", "home.delete", "dh"),
                new ExtraCommandData.CommandData("homes", "List your homes", "/homes", "home.list", "homes", "listhomes", "lh")
        );
        commandConfigs = new JsonConfig<>(HomeAPI.getPlatform().getDataDirectory().resolve("commands.json"), ExtraCommandData.class);
        commandConfigs.load(new ExtraCommandData(defaults));
        commands = new AbstractCommand[] {
                new HomeCommand(),
                new SetHomeCommand(),
                new DelHomeCommand(),
                new HomesCommand()
        };
    }

    private final String name;
    private final String permission;
    private final Component description;
    private final Component usage;
    private final String[] aliases;

    public AbstractCommand(@NotNull String name) {
        ExtraCommandData.CommandData data = commandConfigs.access().data().stream().filter(d -> d.name().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (data == null) {
            throw new IllegalArgumentException("Command " + name + " does not exist.");
        }
        this.name = name;
        this.permission = data.permission();
        this.description = mm.deserialize(data.description());
        this.usage = mm.deserialize(data.usage());
        this.aliases = data.aliases();
    }


    public void execute(@NotNull HomePlayer player, @NotNull String[] args) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(mm.deserialize(
                    HomeAPI.getPlatform().getMessageRaw("no-permission")
            ));
            return;
        }
        handle(player, args);
    }

    public abstract void handle(@NotNull HomePlayer player, @NotNull String[] args);

    protected void sendUsage(@NotNull HomePlayer player) {
        player.sendMessage(this.usage);
    }

    protected void sendDescription(@NotNull HomePlayer player) {
        player.sendMessage(this.description);
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return List.of(aliases);
    }
}
