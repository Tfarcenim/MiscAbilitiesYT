package tfar.miscabilitiesyt;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        waterwalk(dispatcher);
    }

    public static void waterwalk(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("waterwalk")
                        .then(Commands.argument("player", EntityArgument.entities())
                                .then(Commands.argument("active", BoolArgumentType.bool())
                                        .executes(ModCommands::handleWater))));

        dispatcher.register(
                Commands.literal("lavawalk")
                        .then(Commands.argument("player", EntityArgument.entities())
                                .then(Commands.argument("active", BoolArgumentType.bool())
                                        .executes(ModCommands::handleLava))));

        dispatcher.register(
                Commands.literal("vision")
                        .then(Commands.argument("player", EntityArgument.entities())
                                .then(Commands.argument("active", BoolArgumentType.bool())
                                        .executes(ModCommands::handleVision))));
        dispatcher.register(
                Commands.literal("disableHostiles")
                        .then(Commands.argument("player", EntityArgument.entities())
                                .then(Commands.argument("active", BoolArgumentType.bool())
                                        .executes(ModCommands::disableHostiles))));
    }

    public static int handleWater(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(ctx,"player");
        boolean active = BoolArgumentType.getBool(ctx,"active");

        Player player = (Player) entity;

        ModAbilities modAbilities = ModAbilities.getFromPlayerSideSafe(player);
        modAbilities.waterwalk = active;
        modAbilities.saveToPlayer(player);

        return 1;
    }

    public static int handleLava(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(ctx,"player");
        boolean active = BoolArgumentType.getBool(ctx,"active");

        Player player = (Player) entity;

        ModAbilities modAbilities = ModAbilities.getFromPlayerSideSafe(player);
        modAbilities.lavawalk = active;
        modAbilities.saveToPlayer(player);

        return 1;
    }

    public static int handleVision(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(ctx,"player");
        boolean active = BoolArgumentType.getBool(ctx,"active");

        Player player = (Player) entity;

        ModAbilities modAbilities = ModAbilities.getFromPlayerSideSafe(player);
        modAbilities.vision = active;
        modAbilities.saveToPlayer(player);

        return 1;
    }

    public static int disableHostiles(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(ctx,"player");
        boolean active = BoolArgumentType.getBool(ctx,"active");

        Player player = (Player) entity;

        ModAbilities modAbilities = ModAbilities.getFromPlayerSideSafe(player);
        modAbilities.disableHostiles = active;
        modAbilities.saveToPlayer(player);

        return 1;
    }
}
