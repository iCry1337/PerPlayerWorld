package de.nomexplayz.perplayerworld.commands;

import com.flowpowered.math.vector.Vector3d;

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

public class WorldCommunityCmd implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;
            teleportCommunity(player);
            player.sendMessage(Text.of(TextColors.GRAY, "[PPW] Du wurdest in die ", TextColors.DARK_GREEN, "Communitywelt ", TextColors.GRAY, "teleportiert"));
        }
        return CommandResult.success();
    }

    public void teleportCommunity(Player p) {
        Location<World> world = Sponge.getServer().getWorld("world").get().getSpawnLocation();
        Location<World> safeLoc = Sponge.getTeleportHelper().getSafeLocation(world).orElse(world);
        p.transferToWorld(Sponge.getServer().getWorld("world").get(), new Vector3d(safeLoc.getX(), safeLoc.getY() + 0.2, safeLoc.getZ()));
    }
}
