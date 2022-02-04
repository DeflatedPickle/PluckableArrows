/* Copyright (c) 2021-2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import com.deflatedpickle.pluckablearrows.PluckableArrows;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.IntStream;

@SuppressWarnings({"UnusedMixin", "unused"})
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
  private static String NBT_TAG = "ArrowCount";

  public MixinLivingEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  @Shadow
  public abstract int getStuckArrowCount();

  @Shadow
  public abstract void setStuckArrowCount(int stuckArrowCount);

  @Shadow
  public abstract boolean damage(DamageSource source, float amount);

  @Override
  public ActionResult interact(PlayerEntity player, Hand hand) {
    super.interact(player, hand);

    if (PluckableArrows.INSTANCE.removeArrow((LivingEntity) (Object) this, player)) {
      return ActionResult.SUCCESS;
    } else {
      return ActionResult.PASS;
    }
  }

  @Inject(method = "drop", at = @At("TAIL"))
  public void dropArrows(DamageSource source, CallbackInfo ci) {
    if (!world.isClient && getStuckArrowCount() > 0) {
      IntStream.rangeClosed(0, random.nextInt(getStuckArrowCount() + 1))
          .forEach(
              i ->
                  world.spawnEntity(
                      new ItemEntity(world, getX(), getY(), getZ(), new ItemStack(Items.ARROW))));
    }
  }

  @Redirect(
      method = "tick",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/entity/LivingEntity;setStuckArrowCount(I)V"))
  public void tickSetStuckArrowCount(LivingEntity instance, int stuckArrowCount) {}

  @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
  public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    setStuckArrowCount(nbt.getInt(NBT_TAG));
  }

  @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
  public void writeCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
    nbt.putInt(NBT_TAG, getStuckArrowCount());
  }
}
