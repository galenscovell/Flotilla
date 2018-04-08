package com.zurui.flotilla.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.JsonReader
import com.zurui.flotilla.entities.components.*
import com.zurui.flotilla.global.Constants
import com.zurui.flotilla.global.Resources
import com.zurui.flotilla.global.enums.Direction
import com.zurui.flotilla.graphics.Animation
import com.zurui.flotilla.states.PlayerAgent

class EntityCreator(private val engine: Engine, private val world: World) {
    private val jsonReader: JsonReader = JsonReader()


    fun createPlayer(posX: Float, posY: Float): Entity {
        val entity = Entity()
        val bodyComponent = BodyComponent(
            entity, world, posX, posY, Constants.MEDIUM_ENTITY_SIZE, movable=true, soft=true, isPlayer=true
        )

        // Assemble animation map
        val animationMap: MutableMap<String, Animation> = mutableMapOf()

        // Default
        Resources.generateAnimationAndAddToMap(animationMap, "player", PlayerAgent.DEFAULT, Direction.RIGHT,
                List(1){ Pair(0, 0f) }, loop=false)
        Resources.generateAnimationAndAddToMap(animationMap, "player", PlayerAgent.DEFAULT, Direction.DOWN,
                List(1){ Pair(0, 0f) }, loop=false)
        Resources.generateAnimationAndAddToMap(animationMap, "player", PlayerAgent.DEFAULT, Direction.LEFT,
                List(1){ Pair(0, 0f) }, loop=false)
        Resources.generateAnimationAndAddToMap(animationMap, "player", PlayerAgent.DEFAULT, Direction.UP,
                List(1){ Pair(0, 0f) }, loop=false)

        // Walk
        Resources.generateAnimationAndAddToMap(animationMap, "player", PlayerAgent.WALK, Direction.RIGHT,
                List(4){ Pair(0, 0.4f); Pair(1, 0.5f); Pair(0, 0.4f); Pair(3, 0.5f) }, loop=true)
        Resources.generateAnimationAndAddToMap(animationMap, "player", PlayerAgent.WALK, Direction.DOWN,
                List(4){ Pair(0, 0.4f); Pair(1, 0.5f); Pair(0, 0.4f); Pair(3, 0.5f) }, loop=true)
        Resources.generateAnimationAndAddToMap(animationMap, "player", PlayerAgent.WALK, Direction.LEFT,
                List(4){ Pair(0, 0.4f); Pair(1, 0.5f); Pair(0, 0.4f); Pair(3, 0.5f) }, loop=true)
        Resources.generateAnimationAndAddToMap(animationMap, "player", PlayerAgent.WALK, Direction.UP,
                List(4){ Pair(0, 0.4f); Pair(1, 0.5f); Pair(0, 0.4f); Pair(3, 0.5f) }, loop=true)

        entity.add(AnimationComponent(animationMap.toMap()))
        entity.add(bodyComponent)
        entity.add(MovementComponent())
//        entity.add(PlayerComponent())
        entity.add(SizeComponent(Constants.SMALL_ENTITY_SIZE, Constants.MEDIUM_ENTITY_SIZE))
        entity.add(TextureComponent())
//        entity.add(StateComponent(PlayerAgent.DEFAULT, Direction.DOWN))
        entity.add(SteeringComponent(bodyComponent.body, Constants.MEDIUM_ENTITY_SIZE, 10f, 10f))

        engine.addEntity(entity)
        return entity
    }
}