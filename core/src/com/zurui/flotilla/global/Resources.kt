package com.zurui.flotilla.global

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.zurui.flotilla.entities.components.StateComponent
import com.zurui.flotilla.global.enums.Direction
import com.zurui.flotilla.graphics.Animation
import com.zurui.flotilla.graphics.AnimationFrame
import com.zurui.flotilla.states.State

object Resources {
    val assetManager: AssetManager = AssetManager()
    private var atlas: TextureAtlas? = null

    var labelMediumStyle: Label.LabelStyle? = null
    var labelSmallStyle: Label.LabelStyle? = null

    fun load() {
        assetManager.load("atlas/atlas.atlas", TextureAtlas::class.java)
        val resolver: FileHandleResolver = InternalFileHandleResolver()
        val fontGeneratorLoader = FreeTypeFontGeneratorLoader(resolver)
        assetManager.setLoader(FreeTypeFontGenerator::class.java, fontGeneratorLoader)
        val fontLoader = FreetypeFontLoader(resolver)
        assetManager.setLoader(BitmapFont::class.java, ".ttf", fontLoader)

        generateFont("ui/Verdana.ttf", 16, 0f, Color.WHITE, Color.BLACK, "smallFont.ttf")
        generateFont("ui/Verdana.ttf", 24, 0f, Color.WHITE, Color.BLACK, "mediumFont.ttf")
    }

    fun done() {
        atlas = assetManager.get("atlas/atlas.atlas", TextureAtlas::class.java)
        loadNinePatches()
        loadLabelStyles()
        loadButtonStyles()
        loadTextField()
        loadProgressBars()
    }

    fun dispose() {
        assetManager.dispose()
        atlas?.dispose()
    }

    /*********************************
     *  Font/UI Resource Generation *
     ********************************/
    private fun generateFont(
            fontName: String,
            size: Int,
            borderWidth: Float,
            fontColor: Color,
            borderColor: Color,
            outName: String) {
        val params = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        params.fontFileName = fontName
        params.fontParameters.size = Math.ceil(size.toDouble()).toInt()
        params.fontParameters.borderWidth = borderWidth
        params.fontParameters.borderColor = borderColor
        params.fontParameters.color = fontColor
        params.fontParameters.magFilter = Texture.TextureFilter.Linear
        params.fontParameters.minFilter = Texture.TextureFilter.Linear
        assetManager.load(outName, BitmapFont::class.java, params)
    }

    private fun loadNinePatches() {

    }

    private fun loadLabelStyles() {
        labelSmallStyle = Label.LabelStyle(assetManager.get("smallFont.ttf", BitmapFont::class.java), Color.WHITE)
        labelMediumStyle = Label.LabelStyle(assetManager.get("mediumFont.ttf", BitmapFont::class.java), Color.WHITE)
    }

    private fun loadButtonStyles() {

    }

    private fun loadTextField() {

    }

    private fun loadProgressBars() {

    }


    /*********************************
     *      Resource Generation     *
     ********************************/
    fun generateAnimationAndAddToMap(
            map: MutableMap<String, Animation>,
            name: String,
            agentState: State<StateComponent>,
            direction: Direction,
            indicesAndTimes: List<Pair<Int, Float>>,
            loop: Boolean) {
        val keyFrames = com.badlogic.gdx.utils.Array<AnimationFrame>()

        for (i in indicesAndTimes) {
            // eg. "ships/geometric_right_idle_0"
            val textureName = "${name.toLowerCase()}_" +
                    "${direction.toString().toLowerCase()}_" +
                    "${agentState.toString().toLowerCase()}_" +
                    "${i.first}"

            if (atlas != null) {
                keyFrames.add(AnimationFrame(atlas!!.findRegion(textureName), i.second))
            }
        }

        val keyName = "$agentState-$direction"
        map[keyName] = Animation(keyFrames, loop)
    }

    fun generateSprite(name: String): Sprite? {
        return atlas?.createSprite(name)
    }
}