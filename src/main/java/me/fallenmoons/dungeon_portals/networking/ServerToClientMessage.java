package me.fallenmoons.dungeon_portals.networking;

import me.fallenmoons.dungeon_portals.particles.ParticleRegistry;
import me.fallenmoons.dungeon_portals.particles.SpawnableParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class ServerToClientMessage {
    private final Vec3 position;
    private final int effectId;

    public ServerToClientMessage(Vec3 position, int effectId) {
        this.position = position;
        this.effectId = effectId;
    }

    public static void encode(ServerToClientMessage msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.position.x);
        buffer.writeDouble(msg.position.y);
        buffer.writeDouble(msg.position.z);
        buffer.writeInt(msg.effectId);
    }

    public static ServerToClientMessage decode(FriendlyByteBuf buffer) {
        return new ServerToClientMessage(new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()), buffer.readInt());
    }

    public static void handle(ServerToClientMessage msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
//            System.out.println("Received message from server");

            // Get the client-side player and world
            LocalPlayer player = Minecraft.getInstance().player;
            ClientLevel level = Minecraft.getInstance().level;

            // Print statements for debugging
//            System.out.println("Context: " + context.toString());
//            System.out.println("Player: " + (player != null ? player.toString() : "Player is null"));
//            System.out.println("Level: " + (level != null ? level.toString() : "Level is null"));
//            System.out.println("msg: " + msg.toString());
//            System.out.println("Position: " + msg.position.toString());
//            System.out.println("Effect ID: " + msg.effectId);

            // Find the particle using the effect ID
            SpawnableParticle part = ParticleRegistry.getFromId(msg.effectId);
//            System.out.println("Particle: " + (part != null ? part.toString() : "Particle is null"));

            // Spawn the particle if everything is valid
            if (part != null && level != null) {
                part.spawnRing(level, msg.position, 30, 0.0, 0.5);
            }

            context.setPacketHandled(true);
        });
    }
}
