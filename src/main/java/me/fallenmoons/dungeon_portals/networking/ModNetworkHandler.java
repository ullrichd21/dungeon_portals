package me.fallenmoons.dungeon_portals.networking;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(
            new ResourceLocation(Dungeon_portals.MODID, "main"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void registerMessages() {
        int id = 0;
        CHANNEL.messageBuilder(ServerToClientMessage.class, id++)
                .encoder(ServerToClientMessage::encode)
                .decoder(ServerToClientMessage::decode)
                .consumerMainThread(ServerToClientMessage::handle)
                .add();
    }
}
