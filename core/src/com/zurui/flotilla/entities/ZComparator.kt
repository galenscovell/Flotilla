package com.zurui.flotilla.entities

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.zurui.flotilla.entities.components.BodyComponent

class ZComparator : Comparator<Entity> {
    private val bodyMapper: ComponentMapper<BodyComponent> =
            ComponentMapper.getFor(BodyComponent::class.java)


    override fun compare(e1: Entity, e2: Entity): Int {
        val p1: Float = bodyMapper.get(e1).body.position.y
        val p2: Float = bodyMapper.get(e2).body.position.y

        return when {
            p1 > p2 -> -1
            p1 < p2 -> 1
            else -> 0
        }
    }
}
