package com.tutorial.oscarmod.datagen;

import com.tutorial.oscarmod.OscarMod;
import com.tutorial.oscarmod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, OscarMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.BLACK_OPAL_BLOCK);
        blockWithItem(ModBlocks.BLACK_OPAL_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_BLACK_OPAL_ORE);
        blockWithItem(ModBlocks.ENDSTONE_BLACK_OPAL_ORE);
        blockWithItem(ModBlocks.NETHERRACK_BLACK_OPAL_ORE);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
