/* Copyright (c) 2021 Example under the CC0 license */

package com.example.examplemod.mixin;

import com.example.examplemod.ExampleMod;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedMixin")
@Mixin(Block.class)
public abstract class ExampleMixin {
  @Inject(method = "onSteppedOn", at = @At("HEAD"))
  public void init(CallbackInfo info) {
    ExampleMod.INSTANCE.init();
  }
}
