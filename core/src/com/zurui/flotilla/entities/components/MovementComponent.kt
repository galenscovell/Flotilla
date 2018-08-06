package com.zurui.flotilla.entities.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class MovementComponent : Component {
    var destination: Vector2 = Vector2()


    fun hasDestination(): Boolean {
        return !destination.isZero(0.05f)
    }
}
