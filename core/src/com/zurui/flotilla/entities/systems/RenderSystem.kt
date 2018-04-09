package com.zurui.flotilla.entities.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.zurui.flotilla.entities.ZComparator
import com.zurui.flotilla.entities.components.BodyComponent
import com.zurui.flotilla.entities.components.SizeComponent
import com.zurui.flotilla.entities.components.TextureComponent
import com.zurui.flotilla.ui.components.EntityStage

class RenderSystem(family: Family,
                   private val spriteBatch: SpriteBatch,
                   private val entityStage: EntityStage) : SortedIteratingSystem(family, ZComparator()) {
    private val bodyMapper: ComponentMapper<BodyComponent> =
            ComponentMapper.getFor(BodyComponent::class.java)
    private val sizeMapper: ComponentMapper<SizeComponent> =
            ComponentMapper.getFor(SizeComponent::class.java)
    private val textureMapper: ComponentMapper<TextureComponent> =
            ComponentMapper.getFor(TextureComponent::class.java)


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyComponent: BodyComponent = bodyMapper.get(entity)
        val currentX: Float = bodyComponent.body.position.x
        val currentY: Float = bodyComponent.body.position.y

        val sizeComponent: SizeComponent = sizeMapper.get(entity)
        val texture: TextureRegion = textureMapper.get(entity).region

        // Only render entities currently within the camera view
        if (entityStage.inCamera(currentX, currentY)) {
            spriteBatch.draw(
                texture,
                currentX - (sizeComponent.width / 2),
                currentY - (sizeComponent.height / 2),
                sizeComponent.width,
                sizeComponent.height
            )
        }
    }

    override fun update(deltaTime: Float) {
        forceSort()
        super.update(deltaTime)
    }
}