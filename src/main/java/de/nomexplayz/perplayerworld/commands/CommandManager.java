package de.nomexplayz.perplayerworld.commands;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {

    public CommandSpec worldHome = CommandSpec.builder()
            .description(Text.of("Gehe in deine eigene Welt oder erstelle dir deine eigene Welt wenn du noch keine eigene Welt hast"))
            .permission("perplayerworld.command.home")
            .executor(new WorldHomeCmd())
            .build();

    public CommandSpec worldVoid = CommandSpec.builder()
            .description(Text.of("Gehe in deine eigene Void Welt oder erstelle dir deine eigene Void Welt wenn du noch keine eigene Welt hast"))
            .permission("perplayerworld.command.void")
            .executor(new WorldVoidCmd())
            .build();

    public CommandSpec worldVisit = CommandSpec.builder()
            .description(Text.of("Besuche die Welt von einem anderen Spieler"))
            .permission("perplayerworld.command.visit")
            .executor(new WorldVisitCmd())
            .arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
            .build();

    public CommandSpec worldSetHome = CommandSpec.builder()
            .description(Text.of("Setze den Homepunkt deiner Welt neu"))
            .permission("perplayerworld.command.sethome")
            .executor(new WorldSetHomeCmd())
            .build();

    public CommandSpec worldCommunity = CommandSpec.builder()
            .description(Text.of("Teleportiert dich in die Community Welt"))
            .permission("perplayerworld.command.community")
            .executor(new WorldCommunityCmd())
            .build();

    public CommandSpec worldFarm = CommandSpec.builder()
            .description(Text.of("Teleportiert dich in die Farmwelt"))
            .permission("perplayerworld.command.farmworld")
            .executor(new WorldFarmCmd())
            .build();

    public CommandSpec worldInfoNormal = CommandSpec.builder()
            .description(Text.of("Zeigt Informationen zu deiner Welt an"))
            .permission("perplayerworld.command.info.normal")
            .executor(new WorldInfoNormalCmd())
            .build();

    public CommandSpec worldInfoVoid = CommandSpec.builder()
            .description(Text.of("Zeigt Informationen zu deiner Void Welt an"))
            .permission("perplayerworld.command.info.void")
            .executor(new WorldInfoVoidCmd())
            .build();

    public CommandSpec worldInfo = CommandSpec.builder()
            .description(Text.of("Zeigt Informationen zu deiner Welt an"))
            .permission("perplayerworld.command.info")
            .arguments(GenericArguments.optional(GenericArguments.string(Text.of("source"))))
            .child(worldInfoNormal, "world")
            .child(worldInfoVoid, "void")
            .executor(new WorldInfoNormalCmd())
            .build();

    public CommandSpec worldCommandSpec = CommandSpec.builder()
            .description(Text.of("Listet alle Befehle auf"))
            .executor(new WorldCmd())
            .child(worldHome, "home")
            .child(worldVoid, "void")
            .child(worldCommunity, "community")
            .child(worldFarm, "farmworld")
            .child(worldInfo, "info")
            .child(worldVisit, "visit")
            .child(worldSetHome, "sethome")
            .build();
}
