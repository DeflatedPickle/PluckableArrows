/* Copyright (c) 2022 DeflatedPickle under the MIT license */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.deflatedpickle.pluckablearrows.mojank

import com.deflatedpickle.pluckablearrows.api.HasPieces
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper
import java.util.Random
import kotlin.random.asKotlinRandom

abstract class StuckObjectsFeatureRenderer<T : LivingEntity, M : EntityModel<T>>(entityRenderer: LivingEntityRenderer<T, M>) :
    FeatureRenderer<T, M>(entityRenderer) {
    protected abstract fun getObjectCount(entity: T): Int
    protected abstract fun renderObject(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: Entity,
        directionX: Float,
        directionY: Float,
        directionZ: Float,
        tickDelta: Float
    )

    override fun render(
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        i: Int,
        livingEntity: T,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val random = Random((livingEntity as Entity).id.toLong())

        for (n in 0 until getObjectCount(livingEntity)) {
            matrixStack.push()

            val partsList = recurseModelPart((contextModel as HasPieces).getPieces(), mutableListOf())

            if (partsList.isNotEmpty()) {
                val modelPart = partsList.random(random.asKotlinRandom())
                val cuboid = modelPart.getRandomCuboid(random)
                modelPart.rotate(matrixStack)

                val o = random.nextFloat()
                val p = random.nextFloat()
                val q = random.nextFloat()

                val r = MathHelper.lerp(o, cuboid.minX, cuboid.maxX) / 16.0f
                val s = MathHelper.lerp(p, cuboid.minY, cuboid.maxY) / 16.0f
                val t = MathHelper.lerp(q, cuboid.minZ, cuboid.maxZ) / 16.0f

                matrixStack.translate(r.toDouble(), s.toDouble(), t.toDouble())

                renderObject(
                    matrixStack,
                    vertexConsumerProvider,
                    i,
                    livingEntity as Entity,
                    -1.0f * (o * 2.0f - 1.0f),
                    -1.0f * (p * 2.0f - 1.0f),
                    -1.0f * (q * 2.0f - 1.0f),
                    h
                )
            }

            matrixStack.pop()
        }
    }

    fun recurseModelPart(list: List<ModelPart>, possibleParts: MutableList<ModelPart>): List<ModelPart> {
        for (i in list) {
            if (i.cuboids.isNotEmpty()) possibleParts.add(i)
            recurseModelPart(i.children.values.toList(), possibleParts)
            continue
        }

        return possibleParts
    }
}
