package de.nomexplayz.perplayerworld.listener;

import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

public class RespawnListener {
    @Listener
    public void onSomeEvent(RespawnPlayerEvent event) {
        World world2 = event.getOriginalPlayer().getWorld();
        Transform<World> to = new Transform<>(world2.getSpawnLocation());
        event.setToTransform(to);
        event.getOriginalPlayer().sendMessage(Text.of(TextColors.YELLOW, "[PPW] Respawned YaY!"));
    }
}
