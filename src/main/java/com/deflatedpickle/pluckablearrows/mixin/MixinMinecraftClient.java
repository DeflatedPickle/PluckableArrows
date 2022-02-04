/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
  @Redirect(
      method = "doItemUse",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
  public boolean isEmpty(ItemStack instance) {
    return false;
  }
}
