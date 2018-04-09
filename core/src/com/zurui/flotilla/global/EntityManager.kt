package com.zurui.flotilla.global

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.zurui.flotilla.entities.EntityCreator
import com.zurui.flotilla.entities.components.*
import com.zurui.flotilla.entities.components.dynamic.InteractiveComponent
import com.zurui.flotilla.entities.components.dynamic.RemovalComponent
import com.zurui.flotilla.entities.systems.AnimationSystem
import com.zurui.flotilla.entities.systems.CollisionSystem
import com.zurui.flotilla.entities.systems.RemovalSystem
import com.zurui.flotilla.entities.systems.RenderSystem
import com.zurui.flotilla.ui.components.EntityStage

object EntityManager {
    val engine: Engine = Engine()
    var creator: EntityCreator? = null


    /********************
     *      Update     *
     ********************/
    fun update(delta: Float) {
        engine.update(delta)
    }

    fun addInteractiveComponent(entity: Entity) {
        if (entity.getComponent(InteractiveComponent::class.java) == null) {
            entity.add(InteractiveComponent())
        }
    }

    fun addRemovalComponent(entity: Entity) {
        if (entity.getComponent(RemovalComponent::class.java) == null) {
            entity.add(RemovalComponent())
        }
    }


    /********************
     *    Creation     *
     ********************/
    fun setup(entitySpriteBatch: SpriteBatch,
              world: World,
              entityStage: EntityStage) {
        creator = EntityCreator(engine, world)

        // Handles animations
        val animationSystem = AnimationSystem(
            Family.all(
                AnimationComponent::class.java,
                StateComponent::class.java,
                TextureComponent::class.java
            ).get()
        )

        // Handles collisions
        val collisionSystem = CollisionSystem(
            Family.all(
                BodyComponent::class.java
            ).get(), world
        )

        // Handles removal of entities
        val removalSystem = RemovalSystem(
            Family.all(
                BodyComponent::class.java,
                RemovalComponent::class.java
            ).get(), world
        )

        // Handles entity graphics
        val renderSystem = RenderSystem(
            Family.all(
                BodyComponent::class.java,
                SizeComponent::class.java,
                TextureComponent::class.java
            ).get(), entitySpriteBatch, entityStage
        )

        // Add systems to engine in priority order
        animationSystem.priority = 0
        engine.addSystem(animationSystem)

        renderSystem.priority = 1
        engine.addSystem(renderSystem)

        // collisionSystem.priority = 2
        // engine.addSystem(collisionSystem)

        removalSystem.priority = 3
        engine.addSystem(removalSystem)
    }
}