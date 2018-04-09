package com.zurui.flotilla.entities.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.zurui.flotilla.global.Constants

import ktx.box2d.body
import ktx.box2d.circle
import ktx.box2d.filter

class BodyComponent(entity: Entity,
                    private val world: World,
                    private val startPosition: Vector2,
                    private val size: Float,
                    private val movable: Boolean,
                    private val soft: Boolean,
                    isPlayerOwned: Boolean = false) : Component {
    val body: Body
    private val fixture: Fixture

    init {
        body = createBody()
        fixture = createFixture()

        body.userData = entity

        if (isPlayerOwned) fixture.userData = "player"
        else fixture.userData = this
    }


    private fun createBody(): Body {
        return world.body {
            fixedRotation = true
            angularDamping = 1f
            linearDamping = 0.1f
            position.set(startPosition)
            type = if (movable)
                       if (soft) BodyDef.BodyType.KinematicBody
                       else BodyDef.BodyType.StaticBody
                   else BodyDef.BodyType.DynamicBody
        }
    }

    private fun createFixture(): Fixture {
        // val shape: PolygonShape = new PolygonShape
        // shape.setAsBox(size / 3, size / 3)

        return body.circle(radius = size /3) {
            density = 1f
            friction = 0.1f
            filter {
                categoryBits = Constants.ENTITY_CATEGORY
                maskBits = Constants.ENTITY_MASK
            }
        }
    }

    fun updateCollision(newCategory: Short, newMask: Short) {
        val filter: Filter = fixture.filterData
        filter.categoryBits = newCategory
        filter.maskBits = newMask
        fixture.filterData = filter
    }

    fun inMotion(): Boolean {
        return !body.linearVelocity.isZero(0.005f)
    }
}
