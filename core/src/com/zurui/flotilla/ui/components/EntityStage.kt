package com.zurui.flotilla.ui.components

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.zurui.flotilla.environment.Physics
import com.zurui.flotilla.global.Constants
import com.zurui.flotilla.global.EntityManager
import com.zurui.flotilla.ui.screens.GameScreen

class EntityStage(private val gameScreen: GameScreen,
                  private val entityViewport: FitViewport,
                  private val entityCamera: OrthographicCamera,
                  private val entitySpriteBatch: SpriteBatch) : Stage(entityViewport, entitySpriteBatch) {
    val physics: Physics = Physics()

    // For camera
    private val lerpPos: Vector3 = Vector3 (0f, 0f, 0f)
    private var minCamX: Float = 0f
    private var minCamY: Float = 0f
    private var maxCamX: Float = 0f
    private var maxCamY: Float = 0f

    // Box2D has a limit on velocity of 2.0 units per step
    // The max speed is 120m/s at 60fps
    private val timeStep: Float = 1 / 120.0f
    private var accumulator: Float = 0f

    init {
        EntityManager.setup(entitySpriteBatch, physics.getWorld(), this)

        EntityManager.creator?.createPlayerShip(Vector2(1f, 1f))
    }


    /********************
     *     Update      *
     ********************/
    fun render(delta: Float) {
        val frameTime: Float = Math.min(delta, 0.25f)
        accumulator += frameTime
        while (accumulator > timeStep) {
            physics.update(timeStep)
            accumulator -= timeStep

            entitySpriteBatch.begin()
            EntityManager.update(delta)
            entitySpriteBatch.end()

            gameScreen.updateFpsCounter()
        }

        updateCamera()
        entitySpriteBatch.projectionMatrix = entityCamera.combined

        physics.debugRender(entityCamera.combined)
    }

    fun update(delta: Float) {
        act(delta)
        draw()
    }


    /**********************
     *      Camera       *
     **********************/
    fun updateCamera() {
        // Find camera upper left coordinates
        minCamX = entityCamera.position.x - (entityCamera.viewportWidth / 2) * entityCamera.zoom
        minCamY = entityCamera.position.y - (entityCamera.viewportHeight / 2) * entityCamera.zoom

        // Find camera lower right coordinates
        maxCamX = minCamX + entityCamera.viewportWidth * entityCamera.zoom
        maxCamY = minCamY + entityCamera.viewportHeight * entityCamera.zoom

        entityCamera.update()
    }

    private fun centerCameraOnEntity(entityBody: Body) {
        lerpPos.x = entityBody.position.x
        lerpPos.y = entityBody.position.y

        entityCamera.position.lerp(lerpPos, 0.1f)
    }

    fun inCamera(x: Float, y: Float): Boolean {
        // Determines if a point falls within the camera
        // (+/- medium entity size to reduce chance of pop-in)
        return (x + Constants.MEDIUM_ENTITY_SIZE) >= minCamX &&
                (y + Constants.MEDIUM_ENTITY_SIZE) >= minCamY &&
                (x - Constants.MEDIUM_ENTITY_SIZE) <= maxCamX &&
                (y - Constants.MEDIUM_ENTITY_SIZE) <= maxCamY
    }

    override fun dispose() {
        super.dispose()
        physics.dispose()
    }
}