package com.zurui.flotilla.utils

import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import com.zurui.flotilla.global.Box2DSteeringUtils

class Box2DLocation(val locationPosition: Vector2) : Location<Vector2> {
    constructor() : this(Vector2())

    private var locationOrientation: Float = 0f


    override fun getPosition(): Vector2 {
        return locationPosition
    }

    override fun getOrientation(): Float {
        return locationOrientation
    }

    override fun setOrientation(orientation: Float) {
        this.locationOrientation = orientation
    }

    override fun vectorToAngle(vector: Vector2): Float {
        return Box2DSteeringUtils.vectorToAngle(vector)
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        return Box2DSteeringUtils.angleToVector(outVector, angle)
    }

    override fun newLocation(): Location<Vector2>{
        return Box2DLocation()
    }
}
