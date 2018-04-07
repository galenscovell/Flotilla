package com.zurui.flotilla.processing

import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.zurui.flotilla.global.Box2DSteeringUtils
import com.zurui.flotilla.utils.Box2DLocation

class BaseSteerable(private val body: Body,
                    private val boundingRadius: Float,
                    private var linearSpeedMax: Float,
                    private var linearAccelerationMax: Float,
                    private var angularSpeedMax: Float,
                    private var angularAccelerationMax: Float) : Steerable<Vector2> {
    constructor(body: Body, boundingRadius: Float) : this(body, boundingRadius, 0f, 0f, 0f, 0f)

    private var linearSpeedThresholdZero: Float = 0.001f
    var behavior: SteeringBehavior<Vector2>? = null


    /********************
     *       Get       *
     ********************/
    fun getBody(): Body = body
    override fun isTagged(): Boolean = isTagged

    override fun getPosition(): Vector2 = body.position
    override fun getOrientation(): Float = body.angle
    override fun getBoundingRadius(): Float = boundingRadius

    override fun getLinearVelocity(): Vector2 = body.linearVelocity
    override fun getMaxLinearSpeed(): Float = linearSpeedMax
    override fun getMaxLinearAcceleration(): Float = linearAccelerationMax

    override fun getAngularVelocity(): Float = body.angularVelocity
    override fun getMaxAngularSpeed(): Float = angularSpeedMax
    override fun getMaxAngularAcceleration(): Float = angularAccelerationMax


    /********************
     *       Set       *
     ********************/
    override fun setTagged(tagged: Boolean) {
        isTagged = tagged
    }

    override fun vectorToAngle(vector: Vector2): Float {
        return Box2DSteeringUtils.vectorToAngle(vector)
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        return Box2DSteeringUtils.angleToVector(outVector, angle)
    }

    override fun setMaxLinearSpeed(maxLinearSpeed: Float) {
        this.linearSpeedMax = maxLinearSpeed
    }

    override fun setMaxLinearAcceleration(maxLinearAcceleration: Float) {
        this.linearAccelerationMax = maxLinearAcceleration
    }

    override fun setMaxAngularSpeed(maxAngularSpeed: Float) {
        this.angularSpeedMax = maxAngularSpeed
    }

    override fun setMaxAngularAcceleration(maxAngularAcceleration: Float) {
        this.angularAccelerationMax = maxAngularAcceleration
    }

    override fun setOrientation(orientation: Float) {
        body.setTransform(body.position, orientation)
    }


    override fun newLocation(): Location<Vector2> = Box2DLocation()

    override fun getZeroLinearSpeedThreshold(): Float = linearSpeedThresholdZero

    override fun setZeroLinearSpeedThreshold(value: Float) {
        this.linearSpeedThresholdZero = value
    }
}