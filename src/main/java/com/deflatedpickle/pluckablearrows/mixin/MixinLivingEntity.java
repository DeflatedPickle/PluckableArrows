/* Copyright (c) 2021-2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import java.util.stream.IntStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"UnusedMixin", "unused"})
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
  private static final String NBT_TAG = "ArrowCount";

  public MixinLivingEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  @Shadow
  public abstract int getStuckArrowCount();

  @Shadow
  public abstract void setStuckArrowCount(int stuckArrowCount);

  @Shadow
  public abstract boolean damage(DamageSource source, float amount);

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
