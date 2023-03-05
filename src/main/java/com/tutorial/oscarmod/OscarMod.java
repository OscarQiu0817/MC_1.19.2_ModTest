package com.tutorial.oscarmod;

import com.tutorial.oscarmod.block.ModBlocks;
import com.tutorial.oscarmod.item.ModCreativeModeTabs;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.tutorial.oscarmod.item.ModItems;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OscarMod.MOD_ID)
public class OscarMod {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "oscarmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
	
    public OscarMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        LOGGER.info("中文測試");
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
    	
    }

    // 新增標籤頁
    private void addCreative(CreativeModeTabEvent.BuildContents event)
    {
        if(event.getTab() == ModCreativeModeTabs.OSCAR_TAB) {
            event.accept(ModItems.BLACK_OPAL);
            event.accept(ModItems.RAW_BLACK_OPAL);
            // 黑蛋白石礦
            event.accept(ModBlocks.BLACK_OPAL_BLOCK);
            event.accept(ModBlocks.BLACK_OPAL_ORE);
            event.accept(ModBlocks.NETHERRACK_BLACK_OPAL_ORE);
            event.accept(ModBlocks.ENDSTONE_BLACK_OPAL_ORE);
            event.accept(ModBlocks.DEEPSLATE_BLACK_OPAL_ORE);
        }
    }
    
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        	
        }
    }

}
