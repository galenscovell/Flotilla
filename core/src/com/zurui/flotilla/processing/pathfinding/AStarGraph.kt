package com.zurui.flotilla.processing.pathfinding

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.QueryCallback
import com.badlogic.gdx.physics.box2d.World
import com.zurui.flotilla.global.Constants

class AStarGraph(world: World,
                 private val cols: Int,
                 private val rows: Int) {
    val graph: Array<Array<Node?>> = Array(cols) { arrayOfNulls<Node>(rows) }

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
        for (y in (cols - 1) downTo 0) {
            for (x in 0 .. (rows - 1)) {
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

        for (y in (cols - 1) downTo 0) {
            for (x in 0 .. (rows - 1)) {
                val node: Node = graph[y][x] ?: continue

                if (!node.isWall) {
                    // Add Connection for each valid neighbor
                    for (offset in neighborhood) {
                        val neighborX: Int = node.x + offset.x.toInt()
                        val neighborY: Int = node.y + offset.y.toInt()

                        if (neighborX in 0..(rows - 1) && neighborY >= 0 && neighborY < cols) {
                            val neighbor: Node = graph[neighborY][neighborX] ?: continue

                            if (!neighbor.isWall) {
                                node.connections.add(neighbor)
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
    fun getNode(x: Int, y: Int): Node? {
        return graph[y][x]
    }

    fun getNode(x: Float, y: Float): Node? {
        return graph[Math.round(y)][Math.round(x)]
    }


    /********************
     *      Debug      *
     ********************/
    fun debugPrint() {
        println()

        for (y in (cols - 1) downTo 0) {
            println()

            for (x in 0 .. (rows - 1)) {
                print(graph[y][x]?.debugPrint())
            }
        }
        println()
    }
}
