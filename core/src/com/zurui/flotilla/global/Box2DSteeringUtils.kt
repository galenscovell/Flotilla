package com.zurui.flotilla.global

import com.badlogic.gdx.math.Vector2

object Box2DSteeringUtils {
    fun vectorToAngle(vector: Vector2): Float {
        return Math.atan2((-vector.x).toDouble(), vector.y.toDouble()).toFloat()
    }

    fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        outVector.x = -Math.sin(angle.toDouble()).toFloat()
        outVector.y = Math.cos(angle.toDouble()).toFloat()
        return outVector
    }
}