package me.fallenmoons.dungeon_portals.init;

import me.fallenmoons.dungeon_portals.Dungeon_portals;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Dungeon_portals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CreativeTabInit {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dungeon_portals.MODID);

    public static final List<Supplier<? extends ItemLike>> DUNGEON_PORTALS_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> DUNGEON_PORTALS_TAB = TABS.register("dungeon_portals_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.dungeon_portals_tab"))
                    .icon(ItemInit.DUNGEON_PORTAL_ITEM.get()::getDefaultInstance)
                    .build());

    public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> itemLike) {
        DUNGEON_PORTALS_TAB_ITEMS.add(itemLike);
        return itemLike;
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == DUNGEON_PORTALS_TAB.get()) {
            DUNGEON_PORTALS_TAB_ITEMS.forEach(itemLike -> event.accept(itemLike.get()));
        }
    }
}
