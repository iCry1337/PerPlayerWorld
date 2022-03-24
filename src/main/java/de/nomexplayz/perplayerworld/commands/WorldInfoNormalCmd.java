package de.nomexplayz.perplayerworld.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class WorldInfoNormalCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {

        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("[PPW] Nur Spieler k\u00f6nnen diesen Befehl benutzen!"));
            return CommandResult.empty();
        }

            Player player = (Player) src;
            Optional<World> world = Sponge.getServer().getWorld(player.getUniqueId().toString());
            if (!world.isPresent()) {
                player.sendMessage(Text.of(TextColors.RED, "[PPW] Du hast noch keine eigene Welt erstellt!"));
                return CommandResult.empty();
            }

            Optional<Integer> rawDimensionID = world.get().getProperties().getAdditionalProperties().getInt(DataQuery.of("SpongeData", "dimensionId"));
            if (!rawDimensionID.isPresent()) {
                player.sendMessage(Text.join(Text.of(TextColors.RED, "[PPW] Du hast noch keine eigene Welt erstellt!")));
                return CommandResult.empty();
            }

            Text worldMessage = Text.join(Text.of(TextColors.GRAY, "[PPW] Deine Welten-Nummer lautet ", TextColors.GREEN), Text.of(String.valueOf(rawDimensionID.get())));
            player.sendMessage(worldMessage);

        return CommandResult.success();
    }
}
