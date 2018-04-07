package com.zurui.flotilla.utils

import com.badlogic.gdx.ai.utils.Collision
import com.badlogic.gdx.ai.utils.Ray
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.RayCastCallback
import com.badlogic.gdx.physics.box2d.World

class Box2DRaycastCollisionDetector(private val world: World) : RaycastCollisionDetector<Vector2> {
    private var callback: Box2DRaycastCallback = Box2DRaycastCallback()

    override fun collides(ray: Ray<Vector2>): Boolean {
        return findCollision(null, ray)
    }

    override fun findCollision(outputCollision: Collision<Vector2>?, inputRay: Ray<Vector2>): Boolean {
        callback.collided = false

        if (!inputRay.start.epsilonEquals(inputRay.end, 0.1f)) {
            if (outputCollision != null) {
                callback.outputCollision = outputCollision
                world.rayCast(callback, inputRay.start, inputRay.end)
            }
        }

        return callback.collided
    }


    class Box2DRaycastCallback : RayCastCallback {
        var outputCollision: Collision<Vector2>? = null
        var collided: Boolean = false

        override fun reportRayFixture(fixture: Fixture, point: Vector2, normal: Vector2, fraction: Float): Float {
            if (outputCollision != null) {
                outputCollision?.set(point, normal)
            }

            collided = true
            return fraction
        }
    }
}