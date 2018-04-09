package com.zurui.flotilla.entities.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.zurui.flotilla.entities.components.BodyComponent

class CollisionSystem(family: Family, world: World) : IteratingSystem(family), ContactListener {
    private val bodyMapper: ComponentMapper<BodyComponent> =
            ComponentMapper.getFor(BodyComponent::class.java)

    // These are grabbed from the world contact listener methods
    private var collisionEntityA: Entity? = null
    private var collisionEntityB: Entity? = null

    init {
        world.setContactListener(this)
    }


    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (collisionEntityB != null && entity == collisionEntityB) {
            val bodyComponent: BodyComponent = bodyMapper.get(entity)

            collisionEntityB = null
        }
    }


    /********************
     *      Box2D      *
     ********************/
    override fun beginContact(contact: Contact) {
        val fixtureA: Fixture = contact.fixtureA
        val fixtureB: Fixture = contact.fixtureB

        // println(s"${fixtureA.getUserData} hit ${fixtureB.getUserData}")
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        // Called after collision detection, but before collision resolution
        // Can query impact velocities of two bodies that have collided here
        // Can disable contacts here (needs to be set every update) contact.setEnabled(false)
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {
        // Can find info about applied impulse here eg. to check if size of collision response
        //  was over a given threshold (to check if object should break, etc.)
        val contactNormal: Vector2 = contact.worldManifold.normal
        val impulseAmplitude: Float = impulse.normalImpulses.toTypedArray()[0]

        val fixtureA: Fixture = contact.fixtureA
        val fixtureB: Fixture = contact.fixtureB

        val velocityA: Vector2 = fixtureA.body.linearVelocity
        val velocityB: Vector2 = fixtureB.body.linearVelocity

        // fixtureB.getBody.setLinearVelocity(contactNormal.scl(impulseAmplitude))
    }

    override fun endContact(contact: Contact) {

    }
}