/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import com.deflatedpickle.pluckablearrows.PluckableArrows;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"UnusedMixin", "ConstantConditions"})
@Mixin(Entity.class)
public class MixinEntity {
  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    if ((Object) this instanceof LivingEntity
        && PluckableArrows.INSTANCE.removeArrow((LivingEntity) (Object) this, player)) {
      cir.setReturnValue(ActionResult.SUCCESS);
    }
  }
}
