package com.tutorial.oscarmod.item;

import com.tutorial.oscarmod.OscarMod;

import com.tutorial.oscarmod.entity.ModEntityTypes;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

	// 註冊到 Mod 事件總表
	public static final DeferredRegister<Item> ITEMS = 
			DeferredRegister.create(ForgeRegistries.ITEMS, OscarMod.MOD_ID);

	// 建立一個 BLACK_OPAL 物品，指定 id 並註冊.
	public static final RegistryObject<Item> BLACK_OPAL = ITEMS.register("black_opal", 
			()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> RAW_BLACK_OPAL = ITEMS.register("raw_black_opal",
			()-> new Item(new Item.Properties()));

	public static final RegistryObject<Item> GODZILLA_EGG = ITEMS.register("godzilla_egg",
	() -> new ForgeSpawnEggItem(ModEntityTypes.GODZILLA,0x22b341, 0x19732e, new Item.Properties()));

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
