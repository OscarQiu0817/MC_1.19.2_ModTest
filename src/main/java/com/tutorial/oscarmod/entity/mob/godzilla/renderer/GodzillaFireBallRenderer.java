package com.tutorial.oscarmod.entity.mob.godzilla.renderer;

import com.tutorial.oscarmod.entity.mob.godzilla.GodzillaModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class GodzillaFireBallRenderer extends ThrownItemRenderer{

    public GodzillaFireBallRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, 10.0F, true);
        System.out.println("GodzillaFireBallRenderer ball render ! ");
    }

}
