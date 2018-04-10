package com.zurui.flotilla.processing.input

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
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
                val entity = hitBody.userData

                println(entity)

                return false
            }

            return true
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        println("Button: $button [X: $screenX, Y: $screenY]")

        callback.testPoint.set(screenX.toFloat(), screenY.toFloat(), 0f)
        camera.unproject(callback.testPoint)

        world.query(
            callback.testPoint.x.minus(0.1f),
            callback.testPoint.y.minus(0.1f),
            callback.testPoint.x.plus(0.1f),
            callback.testPoint.y.plus(0.1f),
            callback
        )
        return true
    }
}