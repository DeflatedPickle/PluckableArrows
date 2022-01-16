/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.api

import net.minecraft.client.model.ModelPart

interface HasPieces {
    fun getPieces(): List<ModelPart>
}
