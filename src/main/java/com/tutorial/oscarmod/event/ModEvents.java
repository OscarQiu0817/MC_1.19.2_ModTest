package com.tutorial.oscarmod.event;

import com.tutorial.oscarmod.OscarMod;
import com.tutorial.oscarmod.entity.ModEntityTypes;
import com.tutorial.oscarmod.entity.mob.godzilla.GodzillaEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = OscarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvensBusEvents{

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event){
            event.put(ModEntityTypes.GODZILLA.get(), GodzillaEntity.createAttributes().build());
        }

    }



}
