package de.nomexplayz.perplayerworld.commands;

import com.flowpowered.math.vector.Vector3d;

import de.nomexplayz.perplayerworld.PerPlayerWorld;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetypes;

public class WorldVisitCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Player plr = (Player) src;
            if (args.<Player>getOne("player").isPresent()) {
                Player player = args.<Player>getOne("player").get();
                if (player.isOnline()) {
                    if (player.getName().equalsIgnoreCase(plr.getName())) {
                        plr.sendMessage(Text.of(TextColors.GRAY, "[PPW] ", TextColors.RED, "Du kannst deine eigene Welt nicht besichtigen!" ));
                    } else {
                        plr.sendMessage(Text.of(TextColors.GRAY, "[PPW] Die Welt von ", TextColors.YELLOW, player.getName(), TextColors.GRAY, " wird geladen..."));

                        if (!Sponge.getServer().getWorld(player.getUniqueId().toString()).isPresent()) {
                            new PerPlayerWorld().createAndLoadWorld(player.getUniqueId().toString(), WorldArchetypes.OVERWORLD, plr);
                        }

                        Location<World> spawn = Sponge.getServer().getWorld(player.getUniqueId().toString()).get().getSpawnLocation();
                        Location<World> safeLoc = Sponge.getTeleportHelper().getSafeLocation(spawn).orElse(spawn);
                        teleport(safeLoc.getX(), safeLoc.getY(), safeLoc.getZ(), plr, player);
                        plr.sendMessage(Text.of(TextColors.GRAY, "[PPW] Du wurdest in die Welt von ", TextColors.YELLOW, player.getName(), TextColors.GRAY, " teleportiert..."));
                        plr.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
                    }
                } else {
                    plr.sendMessage(Text.of(TextColors.GRAY, "[PPW] Der Spieler ", TextColors.YELLOW, player.getName(), TextColors.GRAY, " ist nicht online!"));
                }
            } else {
                plr.sendMessage(Text.of(TextColors.GRAY, "[PPW] Bitte benutze ", TextColors.GREEN, "/welt besuchen <name>"));
            }
        }
        return CommandResult.success();
    }

    public void teleport(double x, double y, double z, Player player, Player target) {
        World world = Sponge.getServer().getWorld(target.getUniqueId().toString()).get();
        Location<World> blockLoc = new Location<>(world, x, y, z);
        Location<World> safeLoc = Sponge.getTeleportHelper().getSafeLocation(blockLoc).orElse(blockLoc);

        player.transferToWorld(Sponge.getServer().getWorld(target.getUniqueId().toString()).get(), new Vector3d(safeLoc.getX(), safeLoc.getY() + 0.2, safeLoc.getZ()));
        world.getProperties().setSpawnPosition(safeLoc.getBlockPosition());
    }
}
