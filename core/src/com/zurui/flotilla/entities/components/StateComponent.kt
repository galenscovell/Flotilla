package com.zurui.flotilla.entities.components

import com.badlogic.ashley.core.Component
import com.zurui.flotilla.global.enums.Direction
import com.zurui.flotilla.states.State

class StateComponent(startState: State<StateComponent>, startDirection: Direction) : Component {
    private var previousState: State<StateComponent>? = null
    private var currentState: State<StateComponent>? = null

    var direction: Direction = startDirection
    var stateTime: Float = 0f

    init {
        setState(startState)
    }


    fun update(delta: Float) {
        stateTime += delta
        currentState?.update(delta, this)
    }

    fun setState(newState: State<StateComponent>) {
        if (currentState == newState) return

        if (currentState != null) {
            previousState = currentState
            currentState?.exit(this)
        }

        currentState = newState
        currentState?.enter(this)

        resetStateTime()
    }

    fun isInState(state: State<StateComponent>): Boolean {
        return currentState == state
    }

    fun resetStateTime() {
        stateTime = 0f
    }

    fun getCurrentState(): State<StateComponent>? {
        return currentState
    }

    fun getPreviousState(): State<StateComponent>? {
        return previousState
    }

    fun getAnimationKey(): String {
        return when {
            direction != Direction.NONE -> "$currentState-$direction"
            else -> "$currentState"
        }
    }
}
