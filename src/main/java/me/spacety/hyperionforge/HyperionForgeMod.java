package me.spacety.hyperionforge;

import me.spacety.hyperionforge.init.PacketHandler;
import me.spacety.hyperionforge.item.ModItems;
import me.spacety.hyperionforge.item.custom.HyperionUpgradeHandler;
import me.spacety.hyperionforge.item.custom.HyperionUseListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(HyperionForgeMod.MODID)
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class HyperionForgeMod {
    public static final String MODID = "hyperionforge";

    public HyperionForgeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new HyperionUseListener());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new HyperionUpgradeHandler());
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        PacketHandler.init();
    }

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
    }

}
