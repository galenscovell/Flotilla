package com.zurui.flotilla.graphics

import com.badlogic.gdx.graphics.g2d.TextureRegion

class Animation(private val frames: com.badlogic.gdx.utils.Array<AnimationFrame>,
                private val loop: Boolean) {
    private var _done: Boolean = frames.size <= 1
    private var _index: Int = 0

    private var _currentFrame: AnimationFrame? = null
    private var _currentFrameTime: Float = 0f

    init {
        setFrame()
    }

    fun update(delta: Float) {
        if (_done) return

        _currentFrameTime -= delta

        if (_currentFrameTime <= 0) {
            _index += 1

            if (_index > frames.size - 1) {
                if (loop) {
                    _index = 0
                } else {
                    _done = true
                    return
                }
            }

            setFrame()
        }
    }

    private fun setFrame() {
        _currentFrame = frames.get(_index)
        _currentFrameTime = _currentFrame?.length ?: 0f
    }

    fun getFrame(): TextureRegion? {
        return _currentFrame?.textureRegion
    }
}