package com.tutorial.oscarmod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(),
                // 可註冊多個 , 目前是註冊了自己寫的 ModBlockLootTables
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK)));
    }
}
