package com.zurui.flotilla.global

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.zurui.flotilla.entities.EntityCreator
import com.zurui.flotilla.entities.components.*
import com.zurui.flotilla.entities.components.dynamic.RemovalComponent
import com.zurui.flotilla.entities.components.dynamic.SelectedComponent
import com.zurui.flotilla.entities.systems.*
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

    fun addRemovalComponent(entity: Entity) {
        if (entity.getComponent(RemovalComponent::class.java) == null) {
            entity.add(RemovalComponent())
        }
    }

    fun addSelectedComponent(entity: Entity) {
        if (entity.getComponent(SelectedComponent::class.java) == null) {
            entity.add(SelectedComponent())
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

        // Handles entity graphics processing
        val renderSystem = RenderSystem(
            Family.all(
                BodyComponent::class.java,
                SizeComponent::class.java,
                TextureComponent::class.java
            ).get(), entitySpriteBatch, entityStage
        )

        // Handles entity selection by player
        val selectionSystem = SelectionSystem(
            Family.all(
                BodyComponent::class.java,
                SelectedComponent::class.java,
                StateComponent::class.java
            ).get(), world
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