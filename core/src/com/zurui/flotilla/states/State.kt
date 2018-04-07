package com.zurui.flotilla.states

interface State<StateComponent> {
    fun enter(stateComponent: StateComponent)

    fun exit(stateComponent: StateComponent)

    fun update(delta: Float, stateComponent: StateComponent)

    fun getName(): String

    fun getId(): Int
}