package me.fallenmoons.dungeon_portals.util;

import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Vec3i;

public class VectorUtils {

    /**
     * Converts a Vec3 (double precision) to Vec3i (integer precision).
     *
     * @param vec The Vec3 to convert.
     * @return A new Vec3i with the rounded integer components of the input Vec3.
     */
    public static Vec3i toVec3i(Vec3 vec) {
        return new Vec3i((int)vec.x(),(int) vec.y(), (int) vec.z());
    }
}

