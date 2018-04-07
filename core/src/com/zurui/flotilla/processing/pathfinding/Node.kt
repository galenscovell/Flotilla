package com.zurui.flotilla.processing.pathfinding

import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import com.zurui.flotilla.global.Box2DSteeringUtils
import com.zurui.flotilla.utils.Box2DLocation

class Node(val x: Int, val y: Int) : Location<Vector2> {
    private var parent: Node? = null
    private var costFromStart: Double = 0.0
    private var totalCost: Double = 0.0
    private val connections: MutableList<Node> = mutableListOf()
    private var isWall: Boolean = false
    private var isMarked: Boolean = false
    private var orientation: Float = 0f
    private val position: Vector2 = Vector2(x.toFloat(), y.toFloat())


    /********************
     *       Get       *
     ********************/
    fun getCostFromStart(): Double {
        return costFromStart
    }

    fun getTotalCost(): Double {
        return totalCost
    }

    fun getParent(): Node? {
        return parent
    }

    fun getConnections(): MutableList<Node> {
        return connections
    }

    fun isWall(): Boolean {
        return isWall
    }

    override fun toString(): String {
        return "Node ($x, $y)"
    }


    /********************
     *       Set       *
     ********************/
    fun setCostFromStart(cost: Double) {
        costFromStart = cost
    }

    fun setTotalCost(cost: Double) {
        totalCost = cost
    }

    fun setParent(node: Node) {
        parent = node
    }

    fun makeWall() {
        isWall = true
    }

    fun makeFloor() {
        isWall = false
    }

    fun makeMarked() {
        isMarked = true
    }

    fun removeMarked() {
        isMarked = false
    }

    fun debugPrint(): String {
        return if (isWall) "W"
        else "."
    }


    /********************
     *    Location     *
     ********************/
    override fun getPosition(): Vector2 {
        return position
    }

    override fun getOrientation(): Float {
        return orientation
    }

    override fun setOrientation(orientation: Float) {
        this.orientation = orientation
    }

    override fun vectorToAngle(vector: Vector2): Float {
        return Box2DSteeringUtils.vectorToAngle(vector)
    }
    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        return Box2DSteeringUtils.angleToVector(outVector, angle)
    }

    override fun newLocation(): Location<Vector2> {
        return Box2DLocation()
    }
}
