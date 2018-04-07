package com.zurui.flotilla.ui.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Scaling
import com.zurui.flotilla.Game
import com.zurui.flotilla.global.Constants
import com.zurui.flotilla.global.Resources
import ktx.actors.then

class LoadScreen(private val root: Game) : AbstractScreen(root) {
    private val loadingImage: Image = Image(Texture(Gdx.files.internal("texture/loading.png")))
    private val loadingMain: Table = Table()
    private val loadingTable: Table = Table()

    init {
        loadingMain.setFillParent(true)
        loadingImage.setScaling(Scaling.fillY)

        loadingTable.add(loadingImage).width(Constants.UI_X / 2).height(Constants.UI_Y / 2).expand().fill()
        loadingMain.add(loadingTable).width(Constants.UI_X).height(Constants.UI_Y).expand().fill()

        uiStage.addActor(loadingMain)
    }

    /**********************
     * Screen Operations *
     **********************/
    override fun render(delta: Float) {
        super.render(delta)

        if (Resources.assetManager.update()) {
            Resources.done()

            val sequence: Action =
                delay(0.35f) then
                parallel(
                    moveTo(Constants.UI_X * 1.4f, 0f, 0.5f) then fadeOut(0.5f)
                ) then
                delay(0.25f) then
                ToGameScreenAction(root)

            uiStage.root.addAction(sequence)
        }
    }

    override fun show() {
        Resources.load()
        uiStage.root.color.a = 0f

        val sequence: Action =
            moveTo(-Constants.UI_X * 1.4f, 0f) then
            parallel(
                moveTo(0f, 0f, 0.5f) then fadeIn(0.5f)
            )

        uiStage.root.addAction(sequence)
    }


    /***************************
     * Custom Scene2D Actions *
     ***************************/
    class ToGameScreenAction(private val root: Game) : Action() {
        private var acted = false
        override fun act(delta: Float): Boolean {
            root.setGameScreen()
            acted = true
            return true
        }
    }
}
