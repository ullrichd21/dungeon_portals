package me.fallenmoons.dungeon_portals.commands;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dungeon_portals.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommandsEventHandler {
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ReturnPlayersCommand.register(event.getServer().getCommands().getDispatcher());
    }
}
