package de.nomexplayz.perplayerworld.listener;

import de.nomexplayz.perplayerworld.PerPlayerWorld;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.spongepowered.api.Sponge.getServer;


public class QuitListener {

    public PerPlayerWorld perPlayerWorld;

    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event) {

        /*Get The Name*/
        String playerName = event.getTargetEntity().getName();

        /*Get The UUID*/
        String playerUUID = event.getTargetEntity().getUniqueId().toString();

        /*Define the Worldname the player owns*/
        Optional<World> worldNameNormal = getServer().getWorld(playerUUID);
        Optional<World> worldNameVoid = getServer().getWorld(playerUUID + "-void");


        if(worldNameNormal.isPresent()) {
            World normalWorld = Sponge.getServer().getWorld(playerUUID).get();
            Sponge.getServer().unloadWorld(normalWorld);

            //Sponge.getScheduler().createTaskBuilder()
            //        .execute((onPlayerDisconnect) -> Sponge.getServer().unloadWorld(normalWorld))
            //        .delay(5, TimeUnit.SECONDS)
            //        .submit(perPlayerWorld);
        }

        if(worldNameVoid.isPresent()) {
            World voidWorld = Sponge.getServer().getWorld(playerUUID + "-void").get();
            Sponge.getServer().unloadWorld(voidWorld);

            //Sponge.getScheduler().createTaskBuilder()
            //        .execute((onPlayerDisconnect) -> Sponge.getServer().unloadWorld(voidWorld))
            //        .delay(5, TimeUnit.SECONDS)
            //        .submit(perPlayerWorld);
        }
    }
}
