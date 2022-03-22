package de.nomexplayz.perplayerworld.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class WorldSetHomeCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;
            if (player.getWorld().getName().equalsIgnoreCase(player.getUniqueId().toString())) {
                Location<World> loc = player.getLocation();

                World world = Sponge.getServer().getWorld(player.getUniqueId().toString()).get();
                world.getProperties().setSpawnPosition(loc.getBlockPosition());

                player.sendMessage(Text.of(TextColors.YELLOW, "[PPW] Du hast deine Home Position neu gesetzt."));

            } else {
                player.sendMessage(Text.of(TextColors.RED, "[PPW] Du kannst diesen Befehl nur in deiner Welt benutzen."));
            }
        }
        return CommandResult.success();
    }
}
