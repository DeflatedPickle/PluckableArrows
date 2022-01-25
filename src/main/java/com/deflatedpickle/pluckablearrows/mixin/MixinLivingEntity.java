/* Copyright (c) 2021-2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import java.util.stream.IntStream;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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

    if (getStuckArrowCount() - 1 >= 0 && !player.isCreative()) {
      if (!world.isClient) {
        player.giveItemStack(new ItemStack(Items.ARROW));
        setStuckArrowCount(getStuckArrowCount() - 1);

        damage(DamageSource.player(player), 1f);
      }

      player.world.playSound(
          getX(),
          getY(),
          getZ(),
          SoundEvents.BLOCK_WET_GRASS_STEP,
          SoundCategory.PLAYERS,
          1.0f,
          1.0f,
          false);

      RedstoneOreBlock.spawnParticles(player.world, getBlockPos());

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
}
