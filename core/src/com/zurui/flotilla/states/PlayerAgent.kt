package com.zurui.flotilla.states

import com.zurui.flotilla.entities.components.StateComponent


enum class PlayerAgent : State<StateComponent> {
    DEFAULT {
        override fun enter(stateComponent: StateComponent) {

        }

        override fun exit(stateComponent: StateComponent) {

        }

        override fun update(delta: Float, stateComponent: StateComponent) {

        }

        override fun getId(): Int {
            return 0
        }
    },
    WALK {
        override fun enter(stateComponent: StateComponent) {

        }

        override fun exit(stateComponent: StateComponent) {

        }

        override fun update(delta: Float, stateComponent: StateComponent) {

        }

        override fun getId(): Int {
            return 1
        }
    },
    RUN {
        override fun enter(stateComponent: StateComponent) {

        }

        override fun exit(stateComponent: StateComponent) {

        }

        override fun update(delta: Float, stateComponent: StateComponent) {

        }

        override fun getId(): Int {
            return 2
        }
    }
}