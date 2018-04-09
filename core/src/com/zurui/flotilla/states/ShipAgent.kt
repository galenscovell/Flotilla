package com.zurui.flotilla.states

import com.zurui.flotilla.entities.components.StateComponent


enum class ShipAgent : State<StateComponent> {
    IDLE {
        override fun enter(stateComponent: StateComponent) {
            println("Entering ${this} state")
        }

        override fun exit(stateComponent: StateComponent) {
            println("Exiting ${this} state")
        }

        override fun update(delta: Float, stateComponent: StateComponent) {

        }

        override fun getId(): Int {
            return 0
        }
    },
    MOVE {
        override fun enter(stateComponent: StateComponent) {
            println("Entering ${this} state")
        }

        override fun exit(stateComponent: StateComponent) {
            println("Exiting ${this} state")
        }

        override fun update(delta: Float, stateComponent: StateComponent) {

        }

        override fun getId(): Int {
            return 1
        }
    }
}