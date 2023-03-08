package com.tutorial.oscarmod.entity.mob.godzilla;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GodzillaRenderer extends GeoEntityRenderer<GodzillaEntity> {
    public GodzillaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GodzillaModel());
        this.shadowRadius = 4.5f;
        this.scaleHeight = 8f;
        this.scaleWidth = 7f;
    }

}
