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

public class WorldVoidCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;

            player.sendMessage(Text.of(TextColors.YELLOW, "[PPW] Deine Void Welt wird geladen..."));

            if (!Sponge.getServer().getWorld(player.getUniqueId().toString() + "-void").isPresent()) {
                new PerPlayerWorld().createAndLoadWorld(player.getUniqueId().toString() + "-void", WorldArchetypes.THE_VOID, player);
            }

            Location<World> spawn = Sponge.getServer().getWorld(player.getUniqueId().toString() + "-void").get().getSpawnLocation();
            Location<World> safeLoc = Sponge.getTeleportHelper().getSafeLocation(spawn).orElse(spawn);
            teleportVoid(safeLoc.getX(), safeLoc.getY(), safeLoc.getZ(), player);

            player.sendMessage(Text.of(TextColors.YELLOW, "[PPW] Du wurdest in deine eigene Void Welt teleportiert"));
        }
        return CommandResult.success();
    }

    public void teleportVoid(double x, double y, double z, Player player) {
        World world = Sponge.getServer().getWorld(player.getUniqueId().toString() + "-void").get();
        Location<World> blockLoc = new Location<>(world, x, y, z);
        Location<World> safeLoc = Sponge.getTeleportHelper().getSafeLocation(blockLoc).orElse(blockLoc);

        player.transferToWorld(Sponge.getServer().getWorld(player.getUniqueId().toString() + "-void").get(), new Vector3d(safeLoc.getX(), safeLoc.getY() + 0.2, safeLoc.getZ()));
        world.getProperties().setSpawnPosition(safeLoc.getBlockPosition());
    }
}
