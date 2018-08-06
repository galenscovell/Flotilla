package com.zurui.flotilla.processing.input

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import com.zurui.flotilla.global.EntityManager
import ktx.app.KtxInputAdapter
import ktx.box2d.KtxQueryCallback
import ktx.box2d.query

class MouseAndKeyboardHandler(private val world: World, private val camera: OrthographicCamera) : KtxInputAdapter {
    private val callback = MouseSelectCallback()

    class MouseSelectCallback : KtxQueryCallback {
        val testPoint = Vector3()

        override fun invoke(fixture: Fixture): Boolean {
            if (fixture.testPoint(testPoint.x, testPoint.y)) {
                val hitBody = fixture.body
                val entity: Entity = hitBody.userData as Entity

                EntityManager.addSelectedComponent(entity)
            }

            return true
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        println("Button: $button [X: $screenX, Y: $screenY]")

        callback.testPoint.set(screenX.toFloat(), screenY.toFloat(), 0f)
        camera.unproject(callback.testPoint)

        println("Projected: ${callback.testPoint.x}, ${callback.testPoint.y}")

        if (button == 0) {
            EntityManager.deselectCurrentEntity()

            world.query(
                callback.testPoint.x.minus(0.1f),
                callback.testPoint.y.minus(0.1f),
                callback.testPoint.x.plus(0.1f),
                callback.testPoint.y.plus(0.1f),
                callback
            )
        } else if (button == 1) {
            EntityManager.setSelectedEntityDestination(callback.testPoint.x, callback.testPoint.y)
        }

        return true
    }
}