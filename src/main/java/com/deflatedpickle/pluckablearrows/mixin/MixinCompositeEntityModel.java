/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mixin;

import com.deflatedpickle.pluckablearrows.api.HasPieces;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings({"unused", "UnusedMixin"})
@Mixin(CompositeEntityModel.class)
public abstract class MixinCompositeEntityModel implements HasPieces {
  @Shadow
  public abstract Iterable<ModelPart> getParts();

  @NotNull
  @Override
  public List<ModelPart> getPieces() {
    return Lists.newLinkedList(getParts());
  }
}
