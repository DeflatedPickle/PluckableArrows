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
        light: Int,
        livingEntity: T,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val model = contextModel
        if (model !is HasPieces) return

        val random = Random((livingEntity as Entity).id.toLong())

        for (n in 0 until getObjectCount(livingEntity)) {
            matrixStack.push()

            val partsList = recurseModelPart(model.getPieces(), mutableListOf())

            if (partsList.isNotEmpty()) {
                val modelPart = partsList.random(random.asKotlinRandom())
                val cuboid = modelPart.getRandomCuboid(random)

                modelPart.rotate(matrixStack)

                val randX = random.nextFloat()
                val randY = random.nextFloat()
                val randZ = random.nextFloat()

                val x = MathHelper.lerp(randX, cuboid.minX, cuboid.maxX) / 16.0f
                val y = MathHelper.lerp(randY, cuboid.minY, cuboid.maxY) / 16.0f
                val z = MathHelper.lerp(randZ, cuboid.minZ, cuboid.maxZ) / 16.0f

                matrixStack.translate(x.toDouble(), y.toDouble(), z.toDouble())

                renderObject(
                    matrixStack,
                    vertexConsumerProvider,
                    light,
                    livingEntity as Entity,
                    -1.0f * (randX * 2.0f - 1.0f),
                    -1.0f * (randY * 2.0f - 1.0f),
                    -1.0f * (randZ * 2.0f - 1.0f),
                    tickDelta
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
