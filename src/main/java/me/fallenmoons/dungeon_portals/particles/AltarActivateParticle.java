package me.fallenmoons.dungeon_portals.particles;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;

public class AltarActivateParticle extends SpawnableParticle {
    @Override
    public void spawnParticle(Level level, Vec3 pos) {
//        System.out.println("Spawned particle");
        Color startingColor = new Color(100, 0, 100);
        Color endingColor = new Color(0, 100, 200);
        WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                .setScaleData(GenericParticleData.create(0.5f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                .setLifetime(60)
                .addMotion(0, 0.01f, 0)
                .enableNoClip()
                .spawn(level, pos.x, pos.y, pos.z);
    }

    public void spawnLightBeam(Level level, Vec3 pos) {
        WorldParticleBuilder.create(LodestoneParticleRegistry.EXTRUDING_SPARK_PARTICLE)
                .setScaleData(GenericParticleData.create(0.5f,0.8f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(Color.WHITE, Color.WHITE).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setLifetime(60)
                .enableNoClip()
                .spawn(level, pos.x, pos.y, pos.z);
    }

    public void spawnNumber(Level level, Vec3 pos, int number) {
        for(int i = 0; i < number; i++) {
            spawnParticle(level, pos);
        }
    }

    public void spawnRing(Level level, Vec3 pos, int number, double offset, double radius) {
        for(int i = 0; i < number; i++) {
            double angle = (i * 2 * Math.PI / number) + offset;
            spawnParticle(level, pos.add(Math.cos(angle) * radius, 0, Math.sin(angle) * radius));
        }
        spawnLightBeam(level, pos);
    }


    public String toString() {
        return "AltarActivateParticle";
    }
}
