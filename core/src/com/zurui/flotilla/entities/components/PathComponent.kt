package com.zurui.flotilla.entities.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.steer.utils.paths.LinePath
import com.badlogic.gdx.math.Vector2
import com.zurui.flotilla.processing.pathfinding.Node
import ktx.collections.toGdxArray

class PathComponent : Component {
    private var linePath: LinePath<Vector2>? = null
    private var ticks: Int = 0


    fun tick(): Boolean {
        ticks -= 1
        return if (ticks <= 0) {
            ticks = 30
            true
        } else {
            false
        }
    }

    fun setLinePath(nodes: Array<Node>) {
        val newLinePath: MutableList<Vector2> = mutableListOf()
        for (node: Node in nodes) {
            newLinePath.add(node.position)
        }

        linePath = LinePath(newLinePath.toGdxArray(), true)
    }

    fun hasLinePath(): Boolean {
        return linePath != null
    }

    fun getLinePath(): LinePath<Vector2> {
        return linePath!!
    }
}