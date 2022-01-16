/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import com.deflatedpickle.pluckablearrows.api.HasPieces;
import java.util.List;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings({"unused", "UnusedMixin"})
@Mixin(SinglePartEntityModel.class)
public abstract class MixinSinglePartEntityModel implements HasPieces {
  @Shadow
  public abstract ModelPart getPart();

  @NotNull
  @Override
  public List<ModelPart> getPieces() {
    return List.of(getPart());
  }
}
