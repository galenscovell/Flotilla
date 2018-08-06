package com.zurui.flotilla.entities.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.zurui.flotilla.entities.components.BodyComponent
import com.zurui.flotilla.entities.components.SizeComponent
import com.zurui.flotilla.ui.components.EntityStage



class SelectionSystem(family: Family,
                      private val world: World,
                      private val spriteBatch: SpriteBatch,
                      private val entityStage: EntityStage) : IteratingSystem(family) {
    private val bodyMapper: ComponentMapper<BodyComponent> =
            ComponentMapper.getFor(BodyComponent::class.java)
    private val sizeMapper: ComponentMapper<SizeComponent> =
            ComponentMapper.getFor(SizeComponent::class.java)


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyComponent: BodyComponent = bodyMapper.get(entity)
        val currentX: Float = bodyComponent.body.position.x
        val currentY: Float = bodyComponent.body.position.y

        val sizeComponent: SizeComponent = sizeMapper.get(entity)
    }
}