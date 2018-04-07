package com.zurui.flotilla.environment

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World

class Physics {
    private val world: World = World(Vector2(0f, 0f), true)  // Gravity, whether to sleep or not
    private val debugWorldRenderer: Box2DDebugRenderer = Box2DDebugRenderer()

    init {
        debugWorldRenderer.isDrawVelocities = true
    }


    /********************
     *       Get       *
     ********************/
    fun getWorld(): World {
        return world
    }


    /********************
     *      Update     *
     ********************/
    fun update(timeStep: Float) {
        world.step(timeStep, 8, 3)
    }


    /********************
     *      Render     *
     ********************/
    fun debugRender(cameraMatrix: Matrix4) {
        debugWorldRenderer.render(world, cameraMatrix)
    }


    /********************
     *     Dispose     *
     ********************/
    fun dispose() {
        debugWorldRenderer.dispose()
        world.dispose()
    }
}