package com.zurui.flotilla.processing.pathfinding

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.zurui.flotilla.global.Constants
import com.zurui.flotilla.global.Resources

class PathfindDebugRenderer(aStarGraph: AStarGraph) {
    private val graph: AStarGraph = aStarGraph


    fun render(batch: SpriteBatch) {
        for (row: Array<Node?> in graph.graph) {
            for (node: Node? in row) {
                if (node != null && node.isMarked) {
                    batch.draw(
                        Resources.generateSprite("tiles/test_pathfind"),
                        node.position.x,
                        node.position.y,
                        Constants.TILE_SIZE,
                        Constants.TILE_SIZE
                    )
                }
            }
        }
    }
}