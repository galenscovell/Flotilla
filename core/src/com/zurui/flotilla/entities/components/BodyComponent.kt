package com.zurui.flotilla.entities.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.*
import com.zurui.flotilla.global.Constants

class BodyComponent(entity: Entity,
                    private val world: World,
                    private val posX: Float,
                    private val posY: Float,
                    private val size: Float,
                    private val movable: Boolean,
                    private val soft: Boolean,
                    isPlayer: Boolean = false) : Component {
    val body: Body = createBody()
    private val fixture: Fixture = createFixture()

    init {
        body.userData = entity

        if (isPlayer)
            fixture.userData = "player"
        else
            fixture.userData = this
    }


    private fun createBody(): Body {
        val bodyDef = BodyDef()

        if (!movable) {
            if (soft) {
                bodyDef.type = BodyDef.BodyType.KinematicBody
            } else {
                bodyDef.type = BodyDef.BodyType.StaticBody
            }
        } else {
            bodyDef.type = BodyDef.BodyType.DynamicBody
        }

        bodyDef.fixedRotation = true
        bodyDef.angularDamping = 1f
        bodyDef.linearDamping = 0.1f
        bodyDef.position.set(posX, posY)

        return world.createBody(bodyDef)
    }

    private fun createFixture(): Fixture {
        val shape: Shape = CircleShape()
        shape.radius = size / 3

        // val shape: PolygonShape = new PolygonShape
        // shape.setAsBox(size / 3, size / 3)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 1f
        fixtureDef.friction = 0.1f
        fixtureDef.filter.categoryBits = Constants.ENTITY_CATEGORY
        fixtureDef.filter.maskBits = Constants.ENTITY_MASK
        val fixture: Fixture = body.createFixture(fixtureDef)

        shape.dispose()
        return fixture
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
