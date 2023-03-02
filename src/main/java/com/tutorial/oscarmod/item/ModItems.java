package com.tutorial.oscarmod.item;

import com.tutorial.oscarmod.OscarMod;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

	// 註冊到 Mod 事件總表
	public static final DeferredRegister<Item> ITEMS = 
			DeferredRegister.create(ForgeRegistries.ITEMS, OscarMod.MODID);

	// 建立一個 BLACK_OPAL 物品，指定 id 並註冊.
	public static final RegistryObject<Item> BLACK_OPAL = ITEMS.register("black_opal", 
			()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> RAW_BLACK_OPAL = ITEMS.register("raw_black_opal",
			()-> new Item(new Item.Properties()));
	
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
