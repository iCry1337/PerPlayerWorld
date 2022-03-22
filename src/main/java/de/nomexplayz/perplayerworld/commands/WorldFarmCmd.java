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

public class WorldFarmCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;

            if (!Sponge.getServer().getWorld("farmwelt").isPresent()) {
                new PerPlayerWorld().createAndLoadWorld("farmwelt", WorldArchetypes.OVERWORLD, player);
            }

            teleportFarmWorld(player);
            player.sendMessage(Text.of(TextColors.GRAY, "[PPW] Du wurdest in die ", TextColors.DARK_GREEN, "Farmwelt ", TextColors.GRAY, "teleportiert"));
        }
        return CommandResult.success();
    }

    public void teleportFarmWorld(Player p) {
        Location<World> world = Sponge.getServer().getWorld("farmwelt").get().getSpawnLocation();
        Location<World> safeLoc = Sponge.getTeleportHelper().getSafeLocation(world).orElse(world);
        p.transferToWorld(Sponge.getServer().getWorld("farmwelt").get(), new Vector3d(safeLoc.getX(), safeLoc.getY() + 0.2, safeLoc.getZ()));
    }
}
