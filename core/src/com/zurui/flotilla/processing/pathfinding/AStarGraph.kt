package com.zurui.flotilla.processing.pathfinding

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.QueryCallback
import com.badlogic.gdx.physics.box2d.World
import com.zurui.flotilla.global.Constants

class AStarGraph(world: World,
                 private val tileMap: TiledMap,
                 private var width: Int,
                 private var height: Int) {
    private val graph: Array<Array<Node?>> = Array(width) { arrayOfNulls<Node>(height) }
    private var wall: Boolean = false

    private val neighborhood: List<Vector2> = listOf(
        Vector2(-1f,  0f),
        Vector2( 1f,  0f),
        Vector2( 0f, -1f),
        Vector2( 0f,  1f),
        Vector2( 1f,  1f),
        Vector2(-1f, -1f),
        Vector2( 1f, -1f),
        Vector2(-1f,  1f)
    )

    class GraphWorldCallback : QueryCallback {
        var wall: Boolean = false
        override fun reportFixture(fixture: Fixture?): Boolean {
            return fixture?.filterData?.categoryBits == Constants.WALL_CATEGORY
        }
    }

    private val callback: GraphWorldCallback = GraphWorldCallback()

    init {
        for (y in (height - 1) downTo 0) {
            for (x in 0..width) {
                graph[y][x] = Node(x, y)
                wall = false
                world.QueryAABB(
                    callback,
                    x + 0.05f, y + 0.05f,
                    x + Constants.TILE_SIZE - 0.05f,
                    y + Constants.TILE_SIZE - 0.05f
                )

                if (callback.wall) {
                    graph[y][x]?.makeWall()
                }
            }
        }

        for (y in (height - 1) downTo 0) {
            for (x in 0..width) {
                val node: Node = graph[y][x] ?: continue

                if (!node.isWall()) {
                    // Add Connection for each valid neighbor
                    for (offset in neighborhood) {
                        val neighborX: Int = node.x + offset.x.toInt()
                        val neighborY: Int = node.y + offset.y.toInt()

                        if (neighborX in 0..(width - 1) && neighborY >= 0 && neighborY < height) {
                            val neighbor: Node = graph[neighborY][neighborX] ?: continue

                            if (!neighbor.isWall()) {
                                node.getConnections().add(neighbor)
                            }
                        }
                    }
                }
            }
        }
    }


    /********************
     *       Get       *
     ********************/
    fun getWidth(): Int {
        return width
    }

    fun getHeight(): Int {
        return height
    }

    fun getNodeAt(x: Int, y: Int): Node? {
        return graph[y][x]
    }

    fun getNodeAt(x: Float, y: Float): Node? {
        return graph[Math.round(y)][Math.round(x)]
    }

    fun getGraph(): Array<Array<Node?>> {
        return graph
    }



    /********************
     *      Debug      *
     ********************/
    fun debugPrint() {
        println()

        for (y in (height - 1) downTo 0) {
            println()

            for (x in 0..width) {
                print(graph[y][x]?.debugPrint())
            }
        }
        println()
    }
}
