package com.zurui.flotilla.global

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.zurui.flotilla.entities.EntityCreator
import com.zurui.flotilla.entities.components.*
import com.zurui.flotilla.entities.components.dynamic.RemovalComponent
import com.zurui.flotilla.entities.components.dynamic.SelectedComponent
import com.zurui.flotilla.entities.components.ShaderComponent
import com.zurui.flotilla.entities.systems.*
import com.zurui.flotilla.environment.Tilemap
import com.zurui.flotilla.global.enums.ShaderType
import com.zurui.flotilla.ui.components.EntityStage

object EntityManager {
    private val movementMapper: ComponentMapper<MovementComponent> =
            ComponentMapper.getFor(MovementComponent::class.java)
    private val selectableMapper: ComponentMapper<SelectableComponent> =
            ComponentMapper.getFor(SelectableComponent::class.java)
    private val selectedMapper: ComponentMapper<SelectedComponent> =
            ComponentMapper.getFor(SelectedComponent::class.java)
    private val shaderMapper: ComponentMapper<ShaderComponent> =
            ComponentMapper.getFor(ShaderComponent::class.java)
    private val removalMapper: ComponentMapper<RemovalComponent> =
            ComponentMapper.getFor(RemovalComponent::class.java)

    val engine: Engine = Engine()
    var creator: EntityCreator? = null
    var tileMap: Tilemap? = null

    private var selectedEntity: Entity? = null


    /********************
     *      Update     *
     ********************/
    fun update(delta: Float) {
        engine.update(delta)
    }

    fun addRemovalComponent(entity: Entity) {
        if (removalMapper.get(entity) == null) {
            entity.add(RemovalComponent())
        }
    }

    fun addSelectedComponent(entity: Entity) {
        if (selectableMapper.get(entity) != null) {
            if (selectedMapper.get(entity) == null) {
                entity.add(SelectedComponent())
                shaderMapper.get(entity).shaderType = ShaderType.SELECTION
                selectedEntity = entity
            }
        }
    }

    fun deselectCurrentEntity() {
        if (selectedEntity != null) {
            selectedEntity!!.remove(SelectedComponent::class.java)
            shaderMapper.get(selectedEntity).shaderType = ShaderType.NONE
            selectedEntity = null
        }
    }

    fun setSelectedEntityDestination(x: Float, y: Float) {
        if (selectedEntity != null) {
            movementMapper.get(selectedEntity).destination = Vector2(x, y)
        }
    }


    /********************
     *    Creation     *
     ********************/
    fun setup(entitySpriteBatch: SpriteBatch,
              world: World,
              entityStage: EntityStage) {
        creator = EntityCreator(engine, world)
        tileMap = entityStage.tileMap

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

        // Handles movement
        val shipMovementSystem = ShipMovementSystem(
            Family.all(
                BodyComponent::class.java,
                MovementComponent::class.java,
                StateComponent::class.java,
                SteeringComponent::class.java,
                PathComponent::class.java
            ).get(), tileMap!!.aStarGraph
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
                ShaderComponent::class.java,
                TextureComponent::class.java
            ).get(), entitySpriteBatch, entityStage
        )

        // Handles entity selection by player
        val selectionSystem = SelectionSystem(
            Family.all(
                BodyComponent::class.java,
                SizeComponent::class.java,
                SelectableComponent::class.java,
                SelectedComponent::class.java,
                StateComponent::class.java
            ).get(), world, entitySpriteBatch, entityStage
        )

        // Add systems to engine in priority order
        selectionSystem.priority = 0
        engine.addSystem(selectionSystem)

        animationSystem.priority = 1
        engine.addSystem(animationSystem)

        renderSystem.priority = 2
        engine.addSystem(renderSystem)

        collisionSystem.priority = 3
        engine.addSystem(collisionSystem)

        removalSystem.priority = 4
        engine.addSystem(removalSystem)

        shipMovementSystem.priority = 5
        engine.addSystem(shipMovementSystem)
    }
}