package com.tutorial.oscarmod.entity.mob.godzilla;

import com.tutorial.oscarmod.OscarMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GodzillaModel extends GeoModel<GodzillaEntity> {
    @Override
    public ResourceLocation getModelResource(GodzillaEntity animatable) {
        return new ResourceLocation(OscarMod.MOD_ID, "geo/godzilla.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GodzillaEntity animatable) {
        return new ResourceLocation(OscarMod.MOD_ID, "textures/entity/godzilla_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GodzillaEntity animatable) {
        return new ResourceLocation(OscarMod.MOD_ID, "animations/entity/godzilla.animation.json");
    }
}
