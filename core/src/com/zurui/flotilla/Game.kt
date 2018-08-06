package com.zurui.flotilla

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.zurui.flotilla.global.Resources
import com.zurui.flotilla.ui.screens.GameScreen
import com.zurui.flotilla.ui.screens.LoadScreen
import ktx.app.KtxGame

class Game : KtxGame<Screen>() {
    private var loadScreen: LoadScreen? = null
    private var gameScreen: GameScreen? = null

    var uiSpriteBatch: SpriteBatch? = null


    override fun create() {
        uiSpriteBatch = SpriteBatch()
        loadScreen = LoadScreen(this)

        if (screens.containsKey(LoadScreen::class.java)) {
            removeScreen(LoadScreen::class.java)
        }

        addScreen(loadScreen!!)
        setScreen<LoadScreen>()
    }

    override fun dispose() {
        loadScreen?.dispose()
        gameScreen?.dispose()
        Resources.dispose()
    }

    fun setGameScreen() {
        gameScreen = GameScreen(this)

        if (screens.containsKey(GameScreen::class.java)) {
            removeScreen(GameScreen::class.java)
        }

        addScreen(gameScreen!!)
        setScreen<GameScreen>()
    }

    fun loadGame() {
        // TODO: Not yet implemented
    }

    fun quitGame() {
        Gdx.app.exit()
    }
}