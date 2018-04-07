package com.zurui.flotilla.ui.screens

import aurelienribon.tweenengine.TweenManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.zurui.flotilla.Game
import com.zurui.flotilla.global.Constants
import com.zurui.flotilla.global.Resources
import com.zurui.flotilla.ui.components.EntityStage

class GameScreen(root: Game) : AbstractScreen(root) {
    private val tweenManager: TweenManager = TweenManager()

    private val inputMultiplexer = InputMultiplexer()
//    private val controllerHandler = ControllerHandler(this)
    private var paused: Boolean = false

    private val fpsLabel: Label = Label("FPS", Resources.labelSmallStyle)

    private var entityStage: EntityStage

    init {
        val entityCamera = OrthographicCamera(Constants.SCREEN_X, Constants.SCREEN_Y)
        val entityViewport = FitViewport(Constants.SCREEN_X, Constants.SCREEN_Y, entityCamera)
        entityStage = EntityStage(this, entityViewport, entityCamera, SpriteBatch())

        val mainTable = Table()
        mainTable.setFillParent(true)
        mainTable.setDebug(true, true)

        val topTable = Table()
        topTable.setDebug(true, true)
        topTable.add(fpsLabel).expand().fill().left().padLeft(16f)
        fpsLabel.setAlignment(Align.left, Align.left)

        val versionTable = Table()
        val versionLabel = Label("v0.1 Alpha", Resources.labelSmallStyle)
        versionLabel.setAlignment(Align.right, Align.right)
        versionTable.add(versionLabel).expand().fill().right().padRight(16f)

        mainTable.add(topTable).width(Constants.EXACT_X).height(80f).top().expand().fill().padTop(8f)
        mainTable.row()
        mainTable.add(versionTable).width(Constants.EXACT_X).height(32f).bottom().expand().fill().padBottom(8f)

        uiStage.addActor(mainTable)
    }


    /********************
     *       Init      *
     ********************/
    fun updateFpsCounter() {
        val fps = Gdx.graphics.framesPerSecond
        fpsLabel.setText("FPS: $fps")
    }

    fun pause(setting: Boolean) {
        paused = setting
    }


    /**********************
     *     Dynamic UI    *
     **********************/


    /**********************
     * Screen Operations *
     **********************/
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.2f, 0.29f, 0.37f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (!paused) {
            entityStage.render(delta)
            entityStage.update(delta)
        }

        uiStage.act(delta)
        uiStage.draw()

        tweenManager.update(delta)
    }

    override fun show() {
        Gdx.input.inputProcessor = uiStage
//        Controllers.clearListeners()
//        Controllers.addListener(controllerHandler)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
//        entityStage.dispose()
    }
}
