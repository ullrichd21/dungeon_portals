package me.fallenmoons.dungeon_portals.particles;

import java.util.HashMap;
import java.util.Map;

public class ParticleRegistry {
    private static final Map<Integer, SpawnableParticle> PARTICLES;

    static {
        PARTICLES = new HashMap<>();
        PARTICLES.put(0, new AltarActivateParticle());
    }

    public static SpawnableParticle getFromId(int id) {
//        System.out.println("Getting particle from id: " + id);
//        System.out.println(PARTICLES.get(id).toString());
        return PARTICLES.get(id);
    }
}
