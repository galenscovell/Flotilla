package com.zurui.flotilla.entities.components

import com.badlogic.ashley.core.Component
import com.zurui.flotilla.global.enums.ShaderType

class ShaderComponent : Component {
    var shaderType: ShaderType = ShaderType.NONE
}