package com.zurui.flotilla.processing.pathfinding

import com.badlogic.gdx.math.Vector2

class Pathfinder(private val graph: AStarGraph) {
    private val openList: MutableList<Node> = mutableListOf()
    private val closedList: MutableList<Node> = mutableListOf()
    private val maxSearchDistance: Float = 45f


    fun resetParents() {
        for (row: Array<Node?> in graph.getGraph()) {
            for (node: Node? in row) {
                if (node != null) {
                    node.setParent(null)
                    node.removeMarked()
                }
            }
        }
    }

    fun findPath(startNodeVector: Vector2, endNodeVector: Vector2): Array<Node> {
        resetParents()
        val startNode: Node? = graph.getNodeAt(startNodeVector.x, startNodeVector.y)
        val endNode: Node? = graph.getNodeAt(endNodeVector.x, endNodeVector.y)

        if (startNode != null && endNode != null) {
            return findPath(startNode, endNode)
        }

        return emptyArray()
    }

    private fun findPath(startNode: Node, endNode: Node): Array<Node> {
        openList.clear()
        closedList.clear()

        startNode.setCostFromStart(0.0)
        startNode.setTotalCost(startNode.getCostFromStart() + heuristic(startNode, endNode))
        val startNodePosition: Vector2 = startNode.position
        openList.add(startNode)

        while (!openList.isEmpty()) {
            val current: Node? = getBestNode(endNode)

            if (current != null) {
                val distanceFromStart: Float = current.position.dst2(startNodePosition)

                if (current == endNode || distanceFromStart > maxSearchDistance) {
                    return tracePath(current)
                }

                openList.removeAt(openList.indexOf(current))
                closedList.add(current)

                for (neighborNode: Node in current.getConnections()) {
                    if (!closedList.contains(neighborNode)) {
                        neighborNode.setTotalCost(current.getCostFromStart() + heuristic(neighborNode, endNode))

                        if (!openList.contains(neighborNode)) {
                            neighborNode.setParent(current)
                            openList.add(neighborNode)
                        } else if (neighborNode.getCostFromStart() < current.getCostFromStart()) {
                            neighborNode.setCostFromStart(neighborNode.getCostFromStart())
                            neighborNode.setParent(neighborNode.getParent())
                        }
                    }
                }
            }
        }

        return emptyArray()
    }

    private fun heuristic(node: Node, endNode: Node): Float {
        val dx: Double = (endNode.x - node.x).toDouble()
        val dy: Double = (endNode.y - node.y).toDouble()
        // return Math.abs(dx) + Math.abs(dy).toFloat()          // Manhattan distance
        return Math.sqrt(dx * dx + dy * dy).toFloat()            // Euclidean distance
        // return Math.max(Math.abs(dx), Math.abs(dy)).toFloat() // Chebyshev distance
    }

    private fun getBestNode(endNode: Node): Node? {
        var minCost: Double = Double.POSITIVE_INFINITY
        var bestNode: Node? = null

        for (n: Node in openList) {
            val totalCost: Double = n.getCostFromStart() + heuristic(n, endNode)

            if (minCost > totalCost) {
                minCost = totalCost
                bestNode = n
            }
        }

        return bestNode
    }

    private fun tracePath(n: Node): Array<Node> {
        val path: MutableList<Node> = mutableListOf()

        // Skip last node added (destinationNode)
        var node: Node = n.getParent() ?: return emptyArray()

        while (node.getParent() != null) {
            path.add(node)
            node.makeMarked()
            node = node.getParent()!!
        }

        path.reverse()
        return path.toTypedArray()
    }
}