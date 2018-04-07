package com.zurui.flotilla.global

import kotlin.experimental.or

object Constants {
    // Box2D masks
    // fixture filter category = "This is what I am"
    const val WALL_CATEGORY: Short = 0x0001
    const val ENTITY_CATEGORY: Short = 0x0002
    const val NO_CATEGORY: Short = 0x0008

    // fixture filter mask = "This is what I collide with"
    const val WALL_MASK: Short = ENTITY_CATEGORY
    val ENTITY_MASK: Short = (WALL_CATEGORY or ENTITY_CATEGORY)
    const val NO_MASK: Short = -2

    // Box2D dimensions conversion factor
    const val PIXEL_PER_METER: Float = 16f

    // Screen dimension units
    // Game runs at 270p (16:9, 480x270)
    const val EXACT_X: Float = 1280f
    const val EXACT_Y: Float = 720f
    const val SCREEN_X: Float = 480 / PIXEL_PER_METER
    const val SCREEN_Y: Float = 270 / PIXEL_PER_METER
    const val UI_X: Float = 1280f
    const val UI_Y: Float = 720f

    // Sprite sizes
    const val TILE_SIZE: Float = 16 / PIXEL_PER_METER
    const val SMALL_ENTITY_SIZE: Float = 24 / PIXEL_PER_METER
    const val MEDIUM_ENTITY_SIZE: Float = 32 / PIXEL_PER_METER
    const val LARGE_ENTITY_SIZE: Float = 64 / PIXEL_PER_METER
    const val HUGE_ENTITY_SIZE: Float = 80 / PIXEL_PER_METER

    // Entity speeds
    const val WALK_SPEED: Float = 2.5f
    const val RUN_SPEED: Float = 6f
}