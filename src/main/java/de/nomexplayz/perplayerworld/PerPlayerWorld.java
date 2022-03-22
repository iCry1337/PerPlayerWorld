package de.nomexplayz.perplayerworld;

import com.google.inject.Inject;

import de.nomexplayz.perplayerworld.commands.*;
import de.nomexplayz.perplayerworld.database.SqlManager;

import de.nomexplayz.perplayerworld.listener.*;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;

import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
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
    private PluginContainer pluginContainer;

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

    private ConfigurationNode config;

    private SqlManager sqlManager;

    // Database Variables
    private boolean databaseEnabled = false;
    private String databaseUrl;
    private String databaseUser;
    private String databasePassword;

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        loadConfig();
        setFeaturesEnabledStatus();

        if (databaseEnabled) {
            databaseUrl = config.getNode("database", "url").getString();
            databaseUser = config.getNode("database", "username").getString();
            databasePassword = config.getNode("database", "password").getString();
            sqlManager = new SqlManager(this, logger);
        }
    }

    @Listener
    public void init(GameInitializationEvent event) {
        createAndRegisterCommands();
        registerListeners();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
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
        CommandSpec worldHome = CommandSpec.builder()
                .description(Text.of("Gehe in deine eigene Welt oder erstelle dir deine eigene Welt wenn du noch keine eigene Welt hast"))
                .executor(new WorldHomeCmd())
                .build();
        game.getCommandManager().register(this, worldHome);

        CommandSpec worldVoid = CommandSpec.builder()
                .description(Text.of("Gehe in deine eigene Void Welt oder erstelle dir deine eigene Void Welt wenn du noch keine eigene Welt hast"))
                .executor(new WorldVoidCmd())
                .build();
        game.getCommandManager().register(this, worldVoid);

        CommandSpec worldVisit = CommandSpec.builder()
                .description(Text.of("Besuche die Welt von einem anderen Spieler"))
                .executor(new WorldVisitCmd())
                .arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
                .build();
        game.getCommandManager().register(this, worldVisit);

        CommandSpec worldSetHome = CommandSpec.builder()
                .description(Text.of("Setze den Homepunkt deiner Welt neu"))
                .executor(new WorldSetHomeCmd())
                .build();
        game.getCommandManager().register(this, worldSetHome);

        //CommandSpec weltportal = CommandSpec.builder()
        //        .description(Text.of("weltportal"))
        //        .executor(new WeltPortalCMD())
        //        .build();
        //game.getCommandManager().register(this, weltportal, "weltportal", "weltportal");

        CommandSpec worldCommunity = CommandSpec.builder()
                .description(Text.of("Teleportiert dich in die Community Welt"))
                .executor(new WorldCommunityCmd())
                .build();
        game.getCommandManager().register(this, worldCommunity);

        CommandSpec worldFarm = CommandSpec.builder()
                .description(Text.of("Teleportiert dich in die Farmwelt"))
                .executor(new WorldFarmCmd())
                .build();
        game.getCommandManager().register(this, worldFarm);

        CommandSpec worldInfo = CommandSpec.builder()
                .description(Text.of("Zeigt Informationen zu deiner Welt an"))
                .executor(new WorldInfoCmd())
                .build();
        game.getCommandManager().register(this, worldInfo);

        CommandSpec worldCommandSpec = CommandSpec.builder()
                .description(Text.of("Listet alle Weltenbefehle auf"))
                .executor(new WorldCmd())
                .child(worldHome, "home")
                .child(worldVoid, "void")
                //.child(weltportal, "portal")
                .child(worldCommunity, "community")
                .child(worldFarm, "farmwelt")
                .child(worldInfo, "info")
                .child(worldVisit, "besuchen")
                .child(worldSetHome, "sethome")
                .build();
        game.getCommandManager().register(this, worldCommandSpec, "welt");
    }

    /**
     * Create and register listener.
     */
    private void registerListeners() {
        EventManager eventManager = game.getEventManager();

        eventManager.registerListeners(this, new DeathListener());
        eventManager.registerListeners(this, new RespawnListener());
    }

    private void setFeaturesEnabledStatus() {
        databaseEnabled = config.getNode("database", "enable").getBoolean(false);
    }

    public void createAndLoadWorld(String folderName, WorldArchetype settings, Player player) {

        try {
            final WorldProperties properties = Sponge.getServer().createWorldProperties(folderName, settings);
            Sponge.getServer().loadWorld(properties);
        } catch (IOException ex) {
            this.logger.error("[PPW] Failed to create world data for [" + folderName + "]!", ex);
            player.sendMessage(Text.of(TextColors.GRAY, "[PPW] The requested world : ", TextColors.YELLOW, folderName, TextColors.GRAY," can\'t be loaded!"));
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

    public boolean isDatabaseEnabled() {
        return databaseEnabled;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public SqlManager getSqlManager() {
        return sqlManager;
    }
}
