/* Copyright (c) 2021-2022 DeflatedPickle under the MIT license */

@file:Suppress("SpellCheckingInspection")

package com.deflatedpickle.pluckablearrows

import net.minecraft.block.RedstoneOreBlock
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

@Suppress("UNUSED")
object PluckableArrows : ModInitializer {
    private const val MOD_ID = "$[id]"
    private const val NAME = "$[name]"
    private const val GROUP = "$[group]"
    private const val AUTHOR = "$[author]"
    private const val VERSION = "$[version]"

    override fun onInitialize(mod: ModContainer) {
        println(listOf(MOD_ID, NAME, GROUP, AUTHOR, VERSION))
    }

    fun removeArrow(entity: LivingEntity, player: PlayerEntity): Boolean =
        if (
            player.getStackInHand(player.activeHand).isEmpty &&
            entity.stuckArrowCount - 1 >= 0
        ) {
            if (!entity.world.isClient) {
                player.giveItemStack(ItemStack(Items.ARROW))
                entity.stuckArrowCount -= 1
                entity.damage(DamageSource.player(player), 1f)
            }
            player.world.playSound(
                entity.x,
                entity.y,
                entity.z,
                SoundEvents.BLOCK_WET_GRASS_STEP,
                SoundCategory.PLAYERS,
                1.0f,
                1.0f,
                false
            )
            RedstoneOreBlock.spawnParticles(player.world, entity.blockPos)

            true
        } else {
            false
        }
}
