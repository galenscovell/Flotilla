package com.zurui.flotilla.entities.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.zurui.flotilla.entities.components.AnimationComponent
import com.zurui.flotilla.entities.components.StateComponent
import com.zurui.flotilla.entities.components.TextureComponent
import com.zurui.flotilla.graphics.Animation

class AnimationSystem(family: Family) : IteratingSystem(family) {
    private val animationMapper: ComponentMapper<AnimationComponent> =
            ComponentMapper.getFor(AnimationComponent::class.java)
    private val stateMapper: ComponentMapper<StateComponent> =
            ComponentMapper.getFor(StateComponent::class.java)
    private val textureMapper: ComponentMapper<TextureComponent> =
            ComponentMapper.getFor(TextureComponent::class.java)


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animationComponent: AnimationComponent = animationMapper.get(entity)
        val stateComponent: StateComponent = stateMapper.get(entity)
        val textureComponent: TextureComponent = textureMapper.get(entity)

        stateComponent.update(deltaTime)

        val animation: Animation? = animationComponent.animationMap[stateComponent.getAnimationKey()]
        if (animation != null) {
            animation.update(deltaTime)

            val animationFrame: TextureRegion? = animation.getFrame()
            if (animationFrame != null && textureComponent.region != animationFrame) {
                textureComponent.region.setRegion(animationFrame)
            }
        }
    }
}