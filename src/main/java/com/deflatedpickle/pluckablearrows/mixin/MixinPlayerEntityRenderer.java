/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {
  @Redirect(
      method = "getArmPose",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
  private static boolean isEmpty(ItemStack instance) {
    return false;
  }
}
