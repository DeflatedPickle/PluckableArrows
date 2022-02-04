/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import com.deflatedpickle.pluckablearrows.mojank.StuckArrowsFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"unused", "UnusedMixin", "rawtypes", "unchecked"})
@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer {
  @Shadow
  protected abstract boolean addFeature(FeatureRenderer feature);

  @Inject(method = "<init>", at = @At("TAIL"))
  public void init(
      EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius, CallbackInfo ci) {
    if ((Object) this instanceof PlayerEntityRenderer) return;

    addFeature(
        new StuckArrowsFeatureRenderer<>(
            ctx, (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) (Object) this));
  }
}
