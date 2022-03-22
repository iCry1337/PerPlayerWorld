package de.nomexplayz.perplayerworld.commands;

import com.flowpowered.math.vector.Vector3d;

import de.nomexplayz.perplayerworld.PerPlayerWorld;

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
import org.spongepowered.api.world.WorldArchetypes;

public class WorldHomeCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;

            player.sendMessage(Text.of(TextColors.YELLOW, "[PPW] Deine Welt wird geladen..."));

            if (!Sponge.getServer().getWorld(player.getUniqueId().toString()).isPresent()) {
                new PerPlayerWorld().createAndLoadWorld(player.getUniqueId().toString(), WorldArchetypes.OVERWORLD, player);
            }

            Location<World> spawn = Sponge.getServer().getWorld(player.getUniqueId().toString()).get().getSpawnLocation();
            Location<World> safeLoc = Sponge.getTeleportHelper().getSafeLocation(spawn).orElse(spawn);
            teleport(safeLoc.getX(), safeLoc.getY(), safeLoc.getZ(), player);

            player.sendMessage(Text.of(TextColors.YELLOW, "[PPW] Du wurdest in deine eigene Welt teleportiert"));
        }
        return CommandResult.success();
    }

    public void teleport(double x, double y, double z, Player player) {
        World world = Sponge.getServer().getWorld(player.getUniqueId().toString()).get();
        Location<World> blockLoc = new Location<>(world, x, y, z);
        Location<World> safeLoc = Sponge.getTeleportHelper().getSafeLocation(blockLoc).orElse(blockLoc);

        player.transferToWorld(Sponge.getServer().getWorld(player.getUniqueId().toString()).get(), new Vector3d(safeLoc.getX(), safeLoc.getY() + 0.2, safeLoc.getZ()));
        world.getProperties().setSpawnPosition(safeLoc.getBlockPosition());
    }
}
