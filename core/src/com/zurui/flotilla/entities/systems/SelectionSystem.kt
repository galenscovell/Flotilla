package com.zurui.flotilla.entities.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.zurui.flotilla.entities.components.BodyComponent

class SelectionSystem(family: Family, private val world: World) : IteratingSystem(family) {
    private val bodyMapper: ComponentMapper<BodyComponent> =
            ComponentMapper.getFor(BodyComponent::class.java)


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val body: Body = bodyMapper.get(entity).body

        println("Entity selected: $entity")
    }
}