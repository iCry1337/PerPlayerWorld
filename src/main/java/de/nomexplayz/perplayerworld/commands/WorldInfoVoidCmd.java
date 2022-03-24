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

public class WorldInfoVoidCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {

        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("[PPW] Nur Spieler k\u00f6nnen diesen Befehl benutzen!"));
            return CommandResult.empty();
        }

            Player player_void = (Player) src;
            Optional<World> void_world = Sponge.getServer().getWorld(player_void.getUniqueId().toString() + "-void");
            if (!void_world.isPresent()) {
                player_void.sendMessage(Text.of(TextColors.RED, "[PPW] Du hast noch keine eigene Void Welt erstellt!"));
                return CommandResult.empty();
            }

            Optional<Integer> rawVoidDimensionID = void_world.get().getProperties().getAdditionalProperties().getInt(DataQuery.of("SpongeData", "dimensionId"));
            if (!rawVoidDimensionID.isPresent()) {
                player_void.sendMessage(Text.join(Text.of(TextColors.RED, "[PPW] Du hast noch keine eigene Void Welt erstellt!")));
                return CommandResult.empty();
            }

            Text voidWorldMessage = Text.join(Text.of(TextColors.GRAY, "[PPW] Deine Void Welten-Nummer lautet ", TextColors.GREEN), Text.of(String.valueOf(rawVoidDimensionID.get())));
            player_void.sendMessage(voidWorldMessage);

        return CommandResult.success();
    }
}
