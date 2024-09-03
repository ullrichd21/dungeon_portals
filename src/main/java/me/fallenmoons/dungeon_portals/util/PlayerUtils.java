package me.fallenmoons.dungeon_portals.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import static me.fallenmoons.dungeon_portals.util.VectorUtils.toVec3i;

public class PlayerUtils {

    /**
     * Gets the player that is closest to the center of the specified BlockPos.
     *
     * @param level The world level (ServerLevel).
     * @param pos   The BlockPos to check.
     * @return The closest Player standing on or near the BlockPos, or null if no player is found.
     */
    public static Player getClosestPlayerStandingOnPos(ServerLevel level, BlockPos pos) {
        Player closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        // Calculate the center of the block
        Vec3 blockCenter = Vec3.upFromBottomCenterOf(pos, 1.0D);

        for (Player player : level.players()) {
            // Get the player's current position (their feet position)
            Vec3 playerPos = player.position();

            // Check if the player is standing on the block
            if (new BlockPos(toVec3i(playerPos)).distToCenterSqr(pos.getX(), pos.above().getY(), pos.getZ()) <= 1.0D) {
                // Calculate the distance from the player's feet to the center of the block
                double distance = playerPos.distanceTo(blockCenter);

                // Check if this player is the closest one
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestPlayer = player;
                }
            } else {
                System.out.println("Player is not standing on the block");
                System.out.println("Player position: " + playerPos + " Block position: " + pos);
            }
        }

        return closestPlayer;
    }
}
