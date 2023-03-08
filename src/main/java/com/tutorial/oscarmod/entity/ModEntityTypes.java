package com.tutorial.oscarmod.entity;

import com.tutorial.oscarmod.OscarMod;
import com.tutorial.oscarmod.entity.mob.godzilla.GodzillaEntity;
import com.tutorial.oscarmod.entity.mob.godzilla.entity.GodzillaFireBall;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 對應原版 EntityType，自製的 Entity 會在這裡註冊.
 */
public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OscarMod.MOD_ID);

    public static final RegistryObject<EntityType<GodzillaEntity>> GODZILLA =
            ENTITY_TYPES.register("godzilla",
                    () -> EntityType.Builder.of(GodzillaEntity::new, MobCategory.MONSTER)
                            // width / height
                            .sized(8f, 14.5f)
                            .build(new ResourceLocation(OscarMod.MOD_ID, "godzilla").toString()));
    public static final RegistryObject<EntityType<GodzillaFireBall>> GODZILLA_FIREBALL =
            ENTITY_TYPES.register("godzilla_fireball",
                    () -> EntityType.Builder.<GodzillaFireBall>of(GodzillaFireBall::new, MobCategory.MISC)
                            .sized(3.5f, 3.5f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(new ResourceLocation(OscarMod.MOD_ID, "godzilla_fireball").toString()));


    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
