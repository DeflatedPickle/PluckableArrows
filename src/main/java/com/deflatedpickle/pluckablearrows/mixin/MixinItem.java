/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import com.deflatedpickle.pluckablearrows.PluckableArrows;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"UnusedMixin", "ConstantConditions"})
@Mixin(Item.class)
public class MixinItem {
  @Inject(method = "use", at = @At("HEAD"), cancellable = true)
  public void use(
      World world,
      PlayerEntity user,
      Hand hand,
      CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
    var stack = user.getStackInHand(hand);
    if (stack.isEmpty() && user.isSneaking()) {
      if (PluckableArrows.INSTANCE.removeArrow(user, (PlayerEntity) user)) {
        cir.setReturnValue(TypedActionResult.success(stack));
      } else {
        cir.setReturnValue(TypedActionResult.pass(stack));
      }
    }
  }
}
