package de.nomexplayz.perplayerworld.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class WorldCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext context) {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("[PPW] Nur Spieler k\u00f6nnen diesen Befehl benutzen!"));
            return CommandResult.empty();
        } else {
            Player player = (Player) src;
            player.sendMessage(Text.of(TextColors.GRAY, "Alle ", TextColors.DARK_GREEN, "Welten ", TextColors.GRAY, "Befehle:"));
            player.sendMessage(Text.of(" "));
            player.sendMessage(Text.of(TextColors.GRAY, " - ", TextColors.DARK_GREEN, "/welt portal"));
            player.sendMessage(Text.of(TextColors.GRAY, "Benutze diesen Befehl um dich zur ", TextColors.DARK_GREEN, "Community Welt ", TextColors.GRAY, "zu teleportieren."));
            player.sendMessage(Text.of(" "));
            player.sendMessage(Text.of(TextColors.GRAY, " - ", TextColors.DARK_GREEN, "/welt community"));
            player.sendMessage(Text.of(TextColors.GRAY, "Benutze diesen Befehl um dich zur ", TextColors.DARK_GREEN, "Community Welt ", TextColors.GRAY, "zu teleportieren."));
            player.sendMessage(Text.of(" "));
            player.sendMessage(Text.of(TextColors.GRAY, " - ", TextColors.DARK_GREEN, "/welt farmwelt"));
            player.sendMessage(Text.of(TextColors.GRAY, "Benutze diesen Befehl um dich zur ", TextColors.DARK_GREEN, "Farmwelt ", TextColors.GRAY, "zu teleportieren."));
            player.sendMessage(Text.of(" "));
            player.sendMessage(Text.of(TextColors.GRAY, " - ", TextColors.DARK_GREEN, "/welt sethome"));
            player.sendMessage(Text.of(TextColors.GRAY, "Benutze diesen Befehl um die ", TextColors.DARK_GREEN, "Home Position ", TextColors.GRAY, "deiner aktuellen Position zu bewegen."));
            player.sendMessage(Text.of(" "));
            player.sendMessage(Text.of(TextColors.GRAY, " - ", TextColors.DARK_GREEN, "/welt besuchen <name>"));
            player.sendMessage(Text.of(TextColors.GRAY, "Benutze diesen Befehl um die Welt von einem anderen ", TextColors.DARK_GREEN, "Spieler ", TextColors.GRAY, "zu besuchen."));
        }
        return CommandResult.success();
    }
}
