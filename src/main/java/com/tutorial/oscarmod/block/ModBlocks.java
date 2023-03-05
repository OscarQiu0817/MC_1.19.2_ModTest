package com.tutorial.oscarmod.block;

import com.tutorial.oscarmod.OscarMod;
import com.tutorial.oscarmod.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, OscarMod.MOD_ID);

    public static final RegistryObject<Block> BLACK_OPAL_BLOCK = registryBlock("black_opal_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(6f)
                    .requiresCorrectToolForDrops()));
    // 以下寫法可改為上面 lambda
//            new Supplier<Block>() {
//                @Override
//                public Block get() {
//                    new Block(BlockBehaviour.Properties.of(Material.METAL)
//                    .strength(6f)
//                    .requiresCorrectToolForDrops()));
//                }
//            });
    public static final RegistryObject<Block> BLACK_OPAL_ORE = registryBlock("black_opal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(3f)
                    .requiresCorrectToolForDrops(), UniformInt.of(10,20)));
    public static final RegistryObject<Block> DEEPSLATE_BLACK_OPAL_ORE = registryBlock("deepslate_black_opal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(4f)
                    .requiresCorrectToolForDrops(), UniformInt.of(10,20)));
    public static final RegistryObject<Block> NETHERRACK_BLACK_OPAL_ORE = registryBlock("netherrack_black_opal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(5f)
                    .requiresCorrectToolForDrops(), UniformInt.of(10,20)));
    public static final RegistryObject<Block> ENDSTONE_BLACK_OPAL_ORE = registryBlock("endstone_black_opal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f)
                    .requiresCorrectToolForDrops(), UniformInt.of(10,20)));


    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
