package com.zurui.flotilla.entities.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.zurui.flotilla.entities.components.*
import com.zurui.flotilla.global.Constants
import com.zurui.flotilla.processing.BaseSteerable
import com.zurui.flotilla.processing.pathfinding.AStarGraph
import com.zurui.flotilla.processing.pathfinding.Node
import com.zurui.flotilla.processing.pathfinding.Pathfinder
import ktx.math.div

class ShipMovementSystem(family: Family, aStarGraph: AStarGraph) : IteratingSystem(family) {
    private val bodyMapper: ComponentMapper<BodyComponent> =
            ComponentMapper.getFor(BodyComponent::class.java)
    private val movementMapper: ComponentMapper<MovementComponent> =
            ComponentMapper.getFor(MovementComponent::class.java)
    private val stateMapper: ComponentMapper<StateComponent> =
            ComponentMapper.getFor(StateComponent::class.java)
    private val steeringMapper: ComponentMapper<SteeringComponent> =
            ComponentMapper.getFor(SteeringComponent::class.java)
    private val pathMapper: ComponentMapper<PathComponent> =
            ComponentMapper.getFor(PathComponent::class.java)

    private val steerOutput: SteeringAcceleration<Vector2> = SteeringAcceleration<Vector2>(Vector2())
    private val pathfinder: Pathfinder = Pathfinder(aStarGraph)


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val body: Body = bodyMapper.get(entity).body
        val steering: SteeringComponent = steeringMapper.get(entity)

        pathfind(body, movementMapper.get(entity), stateMapper.get(entity), steering, pathMapper.get(entity))

        if (steering.hasBehavior()) {
            steering.steerable.behavior?.calculateSteering(steerOutput)
            applySteering(deltaTime, body, steering.steerable)
        } else {
            body.setLinearVelocity(0f, 0f)
        }
    }

    private fun pathfind(body: Body,
                         movement: MovementComponent,
                         stateComponent: StateComponent,
                         steeringComponent: SteeringComponent,
                         pathComponent: PathComponent) {
        if (movement.hasDestination()) {
            val entityPosition = body.position
            val targetPosition = movement.destination

            // Pathfinding happens at set intervals
            if (pathComponent.tick()) {
                // Find path to target
                val foundPath: Array<Node> = pathfinder.findPath(entityPosition, targetPosition)

                // Add path nodes to path and set first node as next target location
                if (foundPath.size > 1) {
                    pathComponent.setLinePath(foundPath)
                    steeringComponent.setNewFollowPath(pathComponent.getLinePath())
                }
            }
        }
    }

    private fun applySteering(delta: Float, body: Body, steering: BaseSteerable) {
        var anyAccelerations = false

        // Apply steeroutput to linear velocity if over threshold
        if (!steerOutput.linear.isZero(steering.zeroLinearSpeedThreshold)) {
            body.applyForceToCenter(steerOutput.linear, true)
            anyAccelerations = true
        } else {
            body.setLinearVelocity(0f, 0f)
        }

        if (steerOutput.angular != 0f) {
            body.applyTorque(steerOutput.angular, true)
            anyAccelerations = true
        } else {
            val linearVelocity: Vector2 = body.linearVelocity
            if (!linearVelocity.isZero(steering.zeroLinearSpeedThreshold)) {
                val newOrientation: Float = steering.vectorToAngle(linearVelocity)
                body.angularVelocity = newOrientation - body.angularVelocity
                body.setTransform(body.position, newOrientation)
                anyAccelerations = true
            }
        }

        if (anyAccelerations) {
            // Linear speed capping
            val velocity: Vector2 = body.linearVelocity
            val currentSpeedSquare: Float = velocity.len2()
            if (currentSpeedSquare > steering.maxLinearSpeed * steering.maxLinearSpeed) {
                body.linearVelocity = velocity.scl(
                    steering.maxLinearSpeed / Math.sqrt(
                        currentSpeedSquare.toDouble()
                    ).toFloat()
                )
            }

            // Angular speed capping
            if (body.angularVelocity > steering.maxAngularSpeed) {
                body.angularVelocity = steering.maxAngularSpeed
            }
        }
    }
}