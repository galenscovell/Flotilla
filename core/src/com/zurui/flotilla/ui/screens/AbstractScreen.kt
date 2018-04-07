package com.zurui.flotilla.ui.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.zurui.flotilla.Game
import com.zurui.flotilla.global.Constants
import ktx.app.KtxScreen

open class AbstractScreen(root: Game) : KtxScreen {
    private val uiCamera: OrthographicCamera =
            OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    protected val uiViewport: FitViewport =
            FitViewport(Constants.UI_X, Constants.UI_Y, uiCamera)
    protected var uiStage: Stage = Stage(uiViewport, root.uiSpriteBatch)

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        uiStage.act(delta)
        uiStage.draw()
    }

    override fun show() {
        Gdx.input.inputProcessor = uiStage
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport?.update(width, height, true)
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }

    override fun dispose() {
        uiStage.dispose()
    }

    override fun pause() { }

    override fun resume() { }
}