/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.pluckablearrows.mojank

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.atan2

class StuckArrowsFeatureRenderer<T : LivingEntity, M : EntityModel<T>>(
    context: EntityRendererFactory.Context,
    entityRenderer: LivingEntityRenderer<T, M>
) : StuckObjectsFeatureRenderer<T, M>(entityRenderer) {
    companion object {
        const val multiplier = 57.2957763671875
    }

    private val dispatcher = context.renderDispatcher

    override fun getObjectCount(entity: T) = (entity as LivingEntity).stuckArrowCount

    override fun renderObject(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: Entity,
        directionX: Float,
        directionY: Float,
        directionZ: Float,
        tickDelta: Float
    ) {
        val f = MathHelper.sqrt(directionX * directionX + directionZ * directionZ)

        val arrowEntity = ArrowEntity(entity.world, entity.x, entity.y, entity.z).apply {
            yaw = (atan2(directionX.toDouble(), directionZ.toDouble()) * multiplier).toFloat()
            pitch = (atan2(directionY.toDouble(), f.toDouble()) * multiplier).toFloat()
            prevYaw = this.yaw
            prevPitch = this.pitch
        }

        dispatcher.render(arrowEntity, 0.0, 0.0, 0.0, 0.0f, tickDelta, matrices, vertexConsumers, light)
    }
}
