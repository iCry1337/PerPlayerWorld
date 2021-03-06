package de.nomexplayz.perplayerworld;

import com.google.inject.Inject;

import de.nomexplayz.perplayerworld.commands.*;
import de.nomexplayz.perplayerworld.listener.*;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;

import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.TickBlockEvent;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.File;
import java.io.IOException;

@Plugin(id = "perplayerworld", name = "PerPlayerWorld", description = "Per Player World - Each Player can have their own world.", authors = { "NomexPlayZ" })
public class PerPlayerWorld {

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConf;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    private Game game;

    @Inject
    private PluginContainer pluginContainer;

    private UserStorageService userStorageService;

    private ConfigurationNode config;

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        loadConfig();
        setFeaturesEnabledStatus();
    }

    @Listener
    public void init(GameInitializationEvent event) {
        createAndRegisterCommands();
        registerListeners();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        userStorageService = game.getServiceManager().provideUnchecked(UserStorageService.class);
        logger.info("PerPlayerWorld Started");
    }

    @Listener(order = Order.LAST)
    public void onServerStopping(GameStoppingServerEvent event) {
        logger.info("PerPlayerWorld Stopping");
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {
        logger.info("PerPlayerWorld Stopped");
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
    }

    /**
     * Reloads configuration files.
     * @param event GameReloadEvent
     */
    @Listener
    public void onGameReload(GameReloadEvent event) {

    }

    /**
     * Load the default config file, perplayerworld.conf.
     */
    private void loadConfig() {
        try {
            if (!defaultConf.exists()) {
                pluginContainer.getAsset("perplayerworld.conf").get().copyToFile(defaultConf.toPath());
            }
            config = loader.load();
        } catch (IOException e) {
            logger.warn("[PPW] Main configuration file could not be loaded/created/changed!");
        }
    }

    /**
     * Create and register custom data.
     */
    private void createAndRegisterData() {
        DataManager dm = Sponge.getDataManager();
    }

    /**
     * Create and register commands.
     */
    public void createAndRegisterCommands() {
        Sponge.getCommandManager().register(this, new CommandManager().worldCommandSpec, "world");
    }

    /**
     * Create and register listener.
     */
    private void registerListeners() {
        EventManager eventManager = game.getEventManager();

        eventManager.registerListeners(this, new DeathListener());
        eventManager.registerListeners(this, new RespawnListener());
        eventManager.registerListeners(this, new QuitListener());
    }

    private void setFeaturesEnabledStatus() {

    }

    public void createAndLoadWorld(String folderName, WorldArchetype settings, Player player) {

        try {
            final WorldProperties properties = Sponge.getServer().createWorldProperties(folderName, settings);
            Sponge.getServer().createWorldProperties(folderName, settings);
            Sponge.getServer().loadWorld(properties);
        } catch (IOException ex) {
            this.logger.error("[PPW] Failed to create world data for [" + folderName + "]!", ex);
            player.sendMessage(Text.of(TextColors.GRAY, "[PPW] The requested world : ", TextColors.YELLOW, folderName, TextColors.GRAY," can't be loaded!"));
        }
    }

    public File getConfigDir() {
        return configDir;
    }

    public Server getServer() {
        return game.getServer();
    }

    public Game getGame() {
        return game;
    }

    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    public UserStorageService getUserStorageService() {
        return userStorageService;
    }


}
