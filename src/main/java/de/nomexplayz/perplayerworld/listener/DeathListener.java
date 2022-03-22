package de.nomexplayz.perplayerworld.listener;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class DeathListener {
    @Listener
    public void onEntityDeath(DestructEntityEvent.Death event) {
        event.setKeepInventory(true);
        if(event.getSource() instanceof Player) {
            Player player = (Player)event.getSource();
            player.sendMessage(Text.of(TextColors.YELLOW, "[PPW] Du bist gestorben."));
        }
    }
}
