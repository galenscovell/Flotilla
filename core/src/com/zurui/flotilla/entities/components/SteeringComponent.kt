package com.zurui.flotilla.entities.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.steer.behaviors.FollowPath
import com.badlogic.gdx.ai.steer.utils.paths.LinePath
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.zurui.flotilla.processing.BaseSteerable

class SteeringComponent(body: Body,
                        boundingRadius: Float,
                        maxLinearSpeed: Float,
                        maxLinearAcceleration: Float) : Component {
    private val steerable: BaseSteerable = BaseSteerable(
        body, boundingRadius, maxLinearSpeed, maxLinearAcceleration, 0f, 0f
    )


    fun hasBehavior(): Boolean {
        return steerable.behavior != null
    }

    fun setNewFollowPath(path: LinePath<Vector2>) {
        steerable.behavior = FollowPath<Vector2, (LinePath.LinePathParam)>(steerable, path, 1f)
            .setEnabled(true)
            .setTimeToTarget(0.01f)
            .setArrivalTolerance(0.5f)
            .setDecelerationRadius(0.03f)
    }
}