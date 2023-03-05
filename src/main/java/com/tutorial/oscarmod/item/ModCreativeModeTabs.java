package com.tutorial.oscarmod.item;

import com.tutorial.oscarmod.OscarMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OscarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab OSCAR_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event){
        // 建立客製化標籤頁物件
        OSCAR_TAB = event.registerCreativeModeTab(new ResourceLocation(OscarMod.MOD_ID, "oscar_tab"),
                // 選定 圖標 與 顯示的標題符號
                builder -> builder.icon(() -> new ItemStack(ModItems.BLACK_OPAL.get()))
                        .title(Component.translatable("creativemodetab.oscar_tab")));
    }
}
