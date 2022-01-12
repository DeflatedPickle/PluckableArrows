/* Copyright (c) 2021 Example under the CC0 license */

package com.example.examplemod

import net.fabricmc.api.ModInitializer

@Suppress("UNUSED")
object ExampleMod : ModInitializer {
    private const val MOD_ID = "$[id]"
    private const val NAME = "$[name]"
    private const val GROUP = "$[group]"
    private const val AUTHOR = "$[author]"
    private const val VERSION = "$[version]"

    override fun onInitialize() {
        println(listOf(MOD_ID, NAME, GROUP, AUTHOR, VERSION))
    }

    fun init() {
        println("ow!")
    }
}
