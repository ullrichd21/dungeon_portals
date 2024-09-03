package me.fallenmoons.dungeon_portals.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.fallenmoons.dungeon_portals.dungeons.Dungeon;
import me.fallenmoons.dungeon_portals.dungeons.DungeonManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ReturnPlayersCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("returnPlayers")
                .then(Commands.argument("dungeonUUID", StringArgumentType.string())
                        .executes(ReturnPlayersCommand::executeWithUUID))
                .executes(ReturnPlayersCommand::executeWithoutUUID));
    }

    private static int executeWithUUID(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String dungeonUUIDString = StringArgumentType.getString(context, "dungeonUUID");
        UUID dungeonUUID = UUID.fromString(dungeonUUIDString);
        return executeCommand(context, dungeonUUID);
    }

    private static int executeWithoutUUID(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        UUID dungeonUUID = findDungeonUUIDForPlayer(player);
        if (dungeonUUID == null) {
            context.getSource().sendFailure(Component.literal("You are not in a dungeon."));
            return 0;
        }
        return executeCommand(context, dungeonUUID);
    }

    private static UUID findDungeonUUIDForPlayer(Player player) {
        DungeonManager dungeonManager = DungeonManager.getInstance(player.getServer().getLevel(Level.OVERWORLD));
        for (Dungeon dungeon : dungeonManager.getActiveDungeons().values()) {
            if (dungeon.getJoinedPlayers().contains(player.getUUID())) {
                return dungeon.getUUID();
            }
        }
        return null;
    }

    private static int executeCommand(CommandContext<CommandSourceStack> context, UUID dungeonUUID) {
        // Get the server and level
        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();

        // Get the DungeonManager instance
        DungeonManager dungeonManager = DungeonManager.getInstance(level);

        // Find the dungeon by UUID
        Dungeon dungeon = dungeonManager.getActiveDungeons().get(dungeonUUID);
        if (dungeon == null) {
            source.sendFailure(Component.literal("Dungeon not found."));
            return 0;
        }

        // Get the overworld level
        ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

        if (overworld == null) {
            source.sendFailure(Component.literal("Overworld not loaded."));
            return 0;
        }

        // Get the portal block position in the overworld linked to this dungeon
        BlockPos portalPos = dungeon.getPortalBlockPos();

        // Teleport all players in the dungeon to the portal position in the overworld
        for (UUID playerUUID : dungeon.getJoinedPlayers()) {
            Player player = level.getPlayerByUUID(playerUUID);
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                serverPlayer.teleportTo(overworld, portalPos.above().getX() + 0.5, portalPos.above().getY(), portalPos.above().getZ() + 0.5, serverPlayer.getYRot(), serverPlayer.getXRot());
                source.sendSuccess(() -> Component.literal("Teleported " + serverPlayer.getName().getString() + " to the overworld portal."), true);
            }
        }

        // Remove all players from the dungeon
        DungeonManager.getInstance(level).removeAllPlayersFromDungeon(dungeonUUID);


        return 1;
    }
}
